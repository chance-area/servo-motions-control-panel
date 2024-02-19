package ru.chancearea.servomotionscontrolpanel;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.VisLabel;

import javax.swing.JFrame;

import ru.chancearea.servomotionscontrolpanel.panels.tabbedpanels.InfoTabPanel;
import ru.chancearea.servomotionscontrolpanel.ui.tabs.TabbedPanelsManager;
import ru.chancearea.servomotionscontrolpanel.panels.tabbedpanels.ConfigurationTabPanel;
import ru.chancearea.servomotionscontrolpanel.panels.tabbedpanels.DebuggingTabPanel;
import ru.chancearea.servomotionscontrolpanel.panels.tabbedpanels.EmulationTabPanel;
import ru.chancearea.servomotionscontrolpanel.panels.tabbedpanels.GraphsTabPanel;
import ru.chancearea.servomotionscontrolpanel.panels.tabbedpanels.RunTabPanel;
import ru.chancearea.servomotionscontrolpanel.utils.CustomInputProcessor;
import ru.chancearea.servomotionscontrolpanel.utils.DrawingTools;
import ru.chancearea.servomotionscontrolpanel.utils.MathPlus;
import ru.chancearea.servomotionscontrolpanel.utils.SerialPortManager;

public class ServoMotionsControlPanel extends ApplicationAdapter {
    public static JFrame superDuperJFrame = null;

    public static OrthographicCamera ortCamera   = null;
    public static Viewport           extViewport = null;

    private SpriteBatch      rootBatch;
    private Stage            rootStage;
    private InputMultiplexer rootInputMultiplexer;
    private ShapeRenderer shapeRenderer;

    private TabbedPanelsManager tabbedPanelsManager;

    // ------ For debug ------
    private SpriteBatch debugBatch;
    private Stage       debugStage;
    private VisLabel labelFPS;

    public ServoMotionsControlPanel() {
        GlobalVariables.isDesktop = false;
        // or...
        //GlobalVariables.isDesktop = (Gdx.app.getType() == Application.ApplicationType.Desktop);
    }

    public ServoMotionsControlPanel(JFrame _jframe) {
        GlobalVariables.isDesktop = true;
        superDuperJFrame = _jframe;
    }

    @Override
    public void create() {
        // ---- ### Debug info (Desktop) ### ----
        if (GlobalConstants.IS_DEBUG_MODE && GlobalVariables.isDesktop) {
            System.out.println("\n------------------------- DEBUG INFO -------------------------");

            Gdx.app.log(GlobalConstants.LOG_TAG_GL, "Version " + Gdx.graphics.getGLVersion().getMajorVersion() + "." + Gdx.graphics.getGLVersion().getMinorVersion());
            Gdx.app.log(GlobalConstants.LOG_TAG_GL, "Max texture size " + (Gdx.graphics.getGL20().GL_MAX_TEXTURE_SIZE));
            Gdx.app.log(GlobalConstants.LOG_TAG_JAVA, "Version " + System.getProperty("java.version"));

            long ram   = Runtime.getRuntime().maxMemory();
            boolean gb = (ram >= 1024 * 1024 * 1024);
            Gdx.app.log(GlobalConstants.LOG_TAG_RAM, "Available " + (Math.floor((gb ? ram / 1024f / 1024 / 1024f : ram / 1024f / 1024f) * 10)) / 10 + " " + (gb ? "GB" : "MB"));

            System.out.println("----------------------- END DEBUG INFO -----------------------\n");
        }

        // Load user preferences
        loadUserPref();

        ortCamera   = new OrthographicCamera(GlobalVariables.windowWidth, GlobalVariables.windowHeight);
        extViewport = new ExtendViewport(ortCamera.viewportWidth, ortCamera.viewportHeight, ortCamera);

        extViewport.apply(true);
        ortCamera.position.set(ortCamera.viewportWidth / 2f, ortCamera.viewportHeight / 2f, 0);

        rootBatch            = new SpriteBatch();
        rootStage            = new Stage(extViewport);
        rootInputMultiplexer = new InputMultiplexer();
        shapeRenderer = new ShapeRenderer();

        rootStage.getRoot().addCaptureListener(new InputListener() {
            public boolean touchDown (InputEvent _event, float _x, float _y, int _pointer, int _button) {
                if (!(_event.getTarget() instanceof TextField) && !(_event.getTarget() instanceof Image)) {
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

            debugStage.setDebugAll(false);
            rootInputMultiplexer.addProcessor(debugStage);

            labelFPS = new VisLabel("FPS: " + GlobalConstants.FPS_LIMIT);
            labelFPS.setColor(GlobalAssets.DARK_COLOR_TABBED_TEXT);
            labelFPS.setFontScale(0.42f);
            labelFPS.pack();

            debugStage.addActor(labelFPS);
        }
    }

    private void loadAllResources() {
        GlobalAssets.init();
        GlobalAssets.loadAllRes();
    }

    private void loadUserPref() {
        GlobalVariables.userPref = Gdx.app.getPreferences(GlobalConstants.USER_PREFERENCES_NAME);

        GlobalVariables.selectedTabID_main     = GlobalVariables.userPref.getInteger(GlobalConstants.KEY_SELECTED_TAB_ID_MAIN, 0);
        GlobalVariables.selectedTabID_settings = GlobalVariables.userPref.getInteger(GlobalConstants.KEY_SELECTED_TAB_ID_SETTINGS, 0);

        GlobalVariables.platformLength = GlobalVariables.userPref.getFloat(GlobalConstants.KEY_PLATFORM_LENGTH, 1.5f);
        GlobalVariables.platformWidth  = GlobalVariables.userPref.getFloat(GlobalConstants.KEY_PLATFORM_WIDTH, 0.5f);
        GlobalVariables.platformHeight = GlobalVariables.userPref.getFloat(GlobalConstants.KEY_PLATFORM_HEIGHT, 0.05f);

        GlobalVariables.radiusWheel              = GlobalVariables.userPref.getFloat(GlobalConstants.KEY_RADIUS_WHEEL, 0.03f);
        GlobalVariables.distanceBetweenMotors    = GlobalVariables.userPref.getFloat(GlobalConstants.KEY_DISTANCE_BETWEEN_MOTORS, 0.24f);
        GlobalVariables.maxLengthThreadUnwinding = GlobalVariables.userPref.getFloat(GlobalConstants.KEY_MAX_LENGTH_UNWINDING, 1.0f);
        GlobalVariables.localESP32IP             = GlobalVariables.userPref.getString(GlobalConstants.KEY_LOCAL_EPS32_IP, "255.255.255.255");
    }

    private void initTabbedPanels() {
        tabbedPanelsManager = new TabbedPanelsManager();
        tabbedPanelsManager.setName("main");

        tabbedPanelsManager.addTabPanel(new ConfigurationTabPanel());
        tabbedPanelsManager.addTabPanel(new EmulationTabPanel());
        tabbedPanelsManager.addTabPanel(new DebuggingTabPanel());
        tabbedPanelsManager.addTabPanel(new RunTabPanel());
        tabbedPanelsManager.addTabPanel(new GraphsTabPanel());
        tabbedPanelsManager.addTabPanel(new InfoTabPanel());

        tabbedPanelsManager.setSelectedTabID(GlobalVariables.selectedTabID_main);
        rootStage.addActor(tabbedPanelsManager);
    }

    private void update(float _deltaTime) {
        tabbedPanelsManager.setSize(GlobalVariables.windowWidth, GlobalVariables.windowHeight);
        tabbedPanelsManager.setPosition(0, 0);

        rootStage.act(_deltaTime);

        if (GlobalConstants.IS_DEBUG_MODE) {
            debugStage.act(_deltaTime);

            labelFPS.setText("FPS: " + Gdx.app.getGraphics().getFramesPerSecond());
            labelFPS.setPosition((GlobalVariables.windowWidth - labelFPS.getWidth()) - (GlobalVariables.isDesktop ? 6 : 26), 7);
        }
    }

    private void renderFrame() {
        rootStage.draw();

        rootBatch.end();
        shapeRenderer.setProjectionMatrix(rootBatch.getProjectionMatrix());

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        DrawingTools.enableGLBlend();

        shapeRenderer.setColor(GlobalAssets.DARK_COLOR_BG.r, GlobalAssets.DARK_COLOR_BG.g, GlobalAssets.DARK_COLOR_BG.b, 0.5f);
        shapeRenderer.rect(0, 0, GlobalVariables.windowWidth, (GlobalVariables.isDesktop ? 30 : 34));

        shapeRenderer.end();
        DrawingTools.disableGLBlend();
        rootBatch.begin();
    }

    private void renderDebugFrame() {
        debugStage.draw();
    }

    @Override
    public void resize(int _width, int _height) {
        super.resize(_width, _height);

        extViewport.update(_width, _height, true);
        ortCamera.position.set(ortCamera.viewportWidth / 2f, ortCamera.viewportHeight / 2f, 0);

        GlobalVariables.windowWidth  = extViewport.getWorldWidth();
        GlobalVariables.windowHeight = extViewport.getWorldHeight();
        rootStage.getViewport().update(_width, _height, true);

        if (GlobalConstants.IS_DEBUG_MODE) {
            debugStage.getViewport().update(_width, _height, true);
            Gdx.app.log(GlobalConstants.LOG_TAG_RESIZE_EVENT, "w: " + _width + "; h = " + _height + "\n               viewport size: " + (int) GlobalVariables.windowWidth + "x" + (int) GlobalVariables.windowHeight + "; ratio: " + MathPlus.roundTo(GlobalVariables.windowWidth / GlobalVariables.windowHeight, 4));
        }
    }

    @Override
    public void render() {
        update(Gdx.app.getGraphics().getDeltaTime());

        extViewport.apply();
        ortCamera.update(true);

        if (Gdx.app.getGraphics().isGL30Available()) {
            Gdx.gl30.glClearColor(GlobalAssets.DARK_COLOR_BG.r, GlobalAssets.DARK_COLOR_BG.g, GlobalAssets.DARK_COLOR_BG.b, GlobalAssets.DARK_COLOR_BG.a);
            Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL30.GL_COVERAGE_BUFFER_BIT_NV : 0));
        } else {
            Gdx.gl20.glClearColor(GlobalAssets.DARK_COLOR_BG.r, GlobalAssets.DARK_COLOR_BG.g, GlobalAssets.DARK_COLOR_BG.b, GlobalAssets.DARK_COLOR_BG.a);
            Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
        }

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
        shapeRenderer.dispose();
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
