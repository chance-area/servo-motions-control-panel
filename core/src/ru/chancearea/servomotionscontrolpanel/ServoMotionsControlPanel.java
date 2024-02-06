/**
 *
 * @Program: 'Servo-motions control panel'
 * @Author: Крайнов Р.В. (Chance area) <chancearea@gmal.com>
 * @Platforms: Android, Windows 10/11, MacOS
 * @License: MIT
 *
 */

package ru.chancearea.servomotionscontrolpanel;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.VisLabel;

import javax.swing.JFrame;

import ru.chancearea.servomotionscontrolpanel.panels.TabbedPanelsManager;
import ru.chancearea.servomotionscontrolpanel.panels.tabbedpanels.ConfigurationTabPanel;
import ru.chancearea.servomotionscontrolpanel.panels.tabbedpanels.DebuggingTabPanel;
import ru.chancearea.servomotionscontrolpanel.panels.tabbedpanels.EmulationTabPanel;
import ru.chancearea.servomotionscontrolpanel.panels.tabbedpanels.GraphsTabPanel;
import ru.chancearea.servomotionscontrolpanel.panels.tabbedpanels.RunTabPanel;
import ru.chancearea.servomotionscontrolpanel.utils.CustomInputProcessor;
import ru.chancearea.servomotionscontrolpanel.utils.SerialPortManager;

public class ServoMotionsControlPanel extends ApplicationAdapter {
    public static JFrame superDuperJFrame = null;

    private OrthographicCamera ortCamera;
    private Viewport extViewport;

    private SpriteBatch      rootBatch;
    private Stage            rootStage;
    private InputMultiplexer rootInputMultiplexer;

    private TabbedPanelsManager tabbedPanelsManager;

    // ------ For debug ------
    private SpriteBatch debugBatch;
    private Stage       debugStage;
    private VisLabel labelFPS;

    public ServoMotionsControlPanel() {
        GlobalVariables.isDesktop = false;
    }

    public ServoMotionsControlPanel(JFrame _jframe) {
        superDuperJFrame = _jframe; // Костыль. Зато красивый.
        GlobalVariables.isDesktop = true;
        // or...
        //GlobalVariables.isDesktop = (Gdx.app.getType() == Application.ApplicationType.Desktop);
    }

    @Override
    public void create() {
        // ---- ### Debug info ### ----
        if (GlobalConstants.IS_DEBUG_MODE && GlobalVariables.isDesktop) {
            System.out.println("------------------------- DEBUG INFO -------------------------");
            Gdx.app.log(GlobalConstants.LOG_TAG_GL_VERSION, Gdx.graphics.getGLVersion().getMajorVersion() + "." + Gdx.graphics.getGLVersion().getMinorVersion());
            Gdx.app.log(GlobalConstants.LOG_TAG_TEXTURE_MAX_SIZE, String.valueOf(GL20.GL_MAX_TEXTURE_SIZE));
            Gdx.app.log(GlobalConstants.LOG_TAG_JAVA_VERSION, String.valueOf(System.getProperty("java.version")));
            System.out.println("----------------------- END DEBUG INFO -----------------------");
        }

        ortCamera   = new OrthographicCamera(GlobalVariables.windowWidth, GlobalVariables.windowHeight);
        extViewport = new ExtendViewport(ortCamera.viewportWidth, ortCamera.viewportHeight, ortCamera);

        extViewport.apply(true);
        ortCamera.position.set(ortCamera.viewportWidth / 2f, ortCamera.viewportHeight / 2f, 0);

        rootBatch            = new SpriteBatch();
        rootStage            = new Stage(extViewport);
        rootInputMultiplexer = new InputMultiplexer();

        rootStage.getRoot().addCaptureListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (!(event.getTarget() instanceof TextField) && !(event.getTarget() instanceof Image)) {
                    rootStage.setKeyboardFocus(null);
                    Gdx.input.setOnscreenKeyboardVisible(false);
                }

                return false;
            }
        });

        rootInputMultiplexer.addProcessor(rootStage);
        rootInputMultiplexer.addProcessor(new CustomInputProcessor());

        Gdx.input.setInputProcessor(rootInputMultiplexer);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);

        loadAllResources();
        initTabbedPanels();

        // ------ ### Only for DEBUG mode ### ------
        if (GlobalConstants.IS_DEBUG_MODE) {
            debugBatch = new SpriteBatch();
            debugStage = new Stage(rootStage.getViewport());

            //debugStage.setDebugAll(true);
            rootInputMultiplexer.addProcessor(debugStage);

            labelFPS = new VisLabel("FPS: " + GlobalConstants.FPS_LIMIT);
            labelFPS.setFontScale(0.3f);
            labelFPS.pack();
            labelFPS.setColor(GlobalAssets.DARK_COLOR_TABBED_TEXTS);

            debugStage.addActor(labelFPS);
        }
    }

    private void loadAllResources() {
        GlobalAssets.init();
        GlobalAssets.loadAllRes();
    }

    private void initTabbedPanels() {
        tabbedPanelsManager = new TabbedPanelsManager();
        tabbedPanelsManager.addTabPanel(new ConfigurationTabPanel());
        tabbedPanelsManager.addTabPanel(new EmulationTabPanel());
        tabbedPanelsManager.addTabPanel(new DebuggingTabPanel());
        tabbedPanelsManager.addTabPanel(new RunTabPanel());
        tabbedPanelsManager.addTabPanel(new GraphsTabPanel());

        rootStage.addActor(tabbedPanelsManager);
    }

    @Override
    public void resize(int _width, int _height) {
        super.resize(_width, _height);

        extViewport.update(_width, _height, true);
        ortCamera.position.set(ortCamera.viewportWidth / 2f, ortCamera.viewportHeight / 2f, 0);

        GlobalVariables.windowWidth  = extViewport.getWorldWidth();
        GlobalVariables.windowHeight = extViewport.getWorldHeight();

        rootStage.getViewport().update(_width, _height, true);
        if (GlobalConstants.IS_DEBUG_MODE) debugStage.getViewport().update(_width, _height, true);
    }

    private void update(float _deltaTime) {
        Vector3 mousePosUnp = ortCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        CustomInputProcessor.vPointerPosition.set(mousePosUnp.x, mousePosUnp.y);

        rootStage.act(_deltaTime);

        if (GlobalConstants.IS_DEBUG_MODE) {
            debugStage.act(_deltaTime);

            labelFPS.setText("FPS: " + Gdx.app.getGraphics().getFramesPerSecond());
            labelFPS.setPosition((GlobalVariables.windowWidth - labelFPS.getWidth()) - 5, (GlobalVariables.windowHeight - labelFPS.getHeight()) - 5);
        }
    }

    private void renderFrame() {
        rootStage.draw();
    }

    private void renderDebugFrame() {
        debugStage.draw();
    }

    @Override
    public void render() {
        update(Gdx.app.getGraphics().getDeltaTime());

        if (Gdx.app.getGraphics().isGL30Available()) {
            Gdx.gl30.glClearColor(GlobalAssets.DARK_COLOR_BG.r, GlobalAssets.DARK_COLOR_BG.g, GlobalAssets.DARK_COLOR_BG.b, GlobalAssets.DARK_COLOR_BG.a);
            Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL30.GL_COVERAGE_BUFFER_BIT_NV : 0));

            Gdx.gl30.glEnable(GL30.GL_BLEND);
            Gdx.gl30.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        } else {
            Gdx.gl20.glClearColor(GlobalAssets.DARK_COLOR_BG.r, GlobalAssets.DARK_COLOR_BG.g, GlobalAssets.DARK_COLOR_BG.b, GlobalAssets.DARK_COLOR_BG.a);
            Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

            Gdx.gl20.glEnable(GL20.GL_BLEND);
            Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }

        extViewport.apply();
        ortCamera.update(true);
        rootBatch.setProjectionMatrix(extViewport.getCamera().combined);

        rootBatch.begin();
        renderFrame();
        rootBatch.end();

        if (GlobalConstants.IS_DEBUG_MODE) {
            debugBatch.setProjectionMatrix(rootBatch.getProjectionMatrix());

            debugBatch.begin();
            renderDebugFrame();
            debugBatch.end();
        }

        if (Gdx.app.getGraphics().isGL30Available()) Gdx.gl30.glDisable(GL30.GL_BLEND);
        else Gdx.gl20.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void pause() {
        super.pause();
        if (GlobalConstants.IS_DEBUG_MODE) Gdx.app.log(String.format(GlobalConstants.APP_TITLE_ENG), "Paused...");
    }

    @Override
    public void resume() {
        super.resume();
        if (GlobalConstants.IS_DEBUG_MODE) Gdx.app.log(String.format(GlobalConstants.APP_TITLE_ENG), "Resumed!");
    }

    @Override
    public void dispose() {
        for (InputProcessor inputProcessor : rootInputMultiplexer.getProcessors()) rootInputMultiplexer.removeProcessor(inputProcessor);

        rootBatch.dispose();
        tabbedPanelsManager.dispose();
        rootStage.dispose();
        GlobalAssets.dispose();

        if (SerialPortManager.serialPort != null) {
            try {
                SerialPortManager.serialPort.closePort();
            } catch (Exception ignore) { }
        }

        if (GlobalConstants.IS_DEBUG_MODE) {
            debugBatch.dispose();
            debugStage.dispose();

            Gdx.app.log(String.format(GlobalConstants.APP_TITLE_ENG), "All res disposed! Exit...");
        }
    }
}
