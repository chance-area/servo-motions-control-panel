package ru.chancearea.servomotionscontrolpanel.panels.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ru.chancearea.servomotionscontrolpanel.GlobalAssets;
import ru.chancearea.servomotionscontrolpanel.GlobalVariables;
import ru.chancearea.servomotionscontrolpanel.ServoMotionsControlPanel;
import ru.chancearea.servomotionscontrolpanel.ui.tabs.ITabPanel;
import ru.chancearea.servomotionscontrolpanel.utils.DrawingTools;

public class RobotSettingsPanel extends Actor implements ITabPanel {
    public static Stage stage3D        = null;
    public static boolean isFullScreen = false;

    private boolean isInit = false;

    private int tabID             = -1;
    private final String tabTitle = "Настройка робота";
    private Vector2 contentPos;
    private float contentWidth;
    private float contentHeight;

    private final ShapeRenderer uiShapeRenderer;

    private Viewport extViewport3D;
    private PerspectiveCamera perspectiveCamera3D;

    private ModelBuilder modelBuilder;
    private ModelBatch modelBatch;
    private ModelCache modelCache;

    private Vector3 contentPosProjected  = new Vector3();
    private Vector3 contentSizeProjected = new Vector3();

    private ModelInstance instanceGrid;
    private ModelInstance instanceXYZCoordinates;
    private ModelInstance instancePlatform;

    private Environment environment3D;
    private static CameraInputController camInputController = null;

    private Vector3 initialPlatformSize;

    private static final Vector3 ZERO_COORDINATE_OFFSET = new Vector3(0f, 0f, 0f);
    private final float   SIZE_SCALE                    = 100f;

    public RobotSettingsPanel() {
        uiShapeRenderer = new ShapeRenderer();
    }

    private void init() {
        Gdx.app.log("", "" + contentWidth);
        perspectiveCamera3D = new PerspectiveCamera(67, contentWidth / 3f, contentHeight / 3f);
        perspectiveCamera3D.near = 20f;
        perspectiveCamera3D.far = 1024f;

        extViewport3D = new ExtendViewport(perspectiveCamera3D.viewportWidth, perspectiveCamera3D.viewportHeight, perspectiveCamera3D);
        extViewport3D.apply(true);

        modelBuilder = new ModelBuilder();
        modelBatch   = new ModelBatch();
        modelCache   = new ModelCache();

        camInputController = new CameraInputController(perspectiveCamera3D);
        camInputController.zoom(0);
        camInputController.scrollFactor = -0.05f;
        camInputController.translateUnits = 300;
        camInputController.rotateAngle = 170;
        camInputController.setInvertedControls(false);
        //camInputController.target = ZERO_COORDINATE_OFFSET;
        ServoMotionsControlPanel.rootInputMultiplexer.addProcessor(camInputController);

        environment3D = new Environment();
        environment3D.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment3D.add(new DirectionalLight().set(0.6f, 0.6f, 0.6f, 0f, -85f, -5f));
        environment3D.add(new DirectionalLight().set(0.6f, 0.6f, 0.6f, 0f, 85f, 5f));
        //environment3D.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -80f, -25f, -5f));
        //environment3D.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 80f, 25f, 5f));
        //environment3D.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 200f, 200, 500f));

        initialPlatformSize = new Vector3(GlobalVariables.platformLength * SIZE_SCALE, GlobalVariables.platformHeight * SIZE_SCALE, GlobalVariables.platformWidth * SIZE_SCALE);
        initModels();

        stage3D = new Stage(extViewport3D);
        stage3D.addActor(this);

        centerPerspectiveCamera();
        perspectiveCamera3D.rotateAround(ZERO_COORDINATE_OFFSET, new Vector3(0, 1, 0), -45);
    }

    private void centerPerspectiveCamera() {
        perspectiveCamera3D.lookAt(ZERO_COORDINATE_OFFSET);
        perspectiveCamera3D.update();
    }

    private void initModels() {
        Model model;

        // -------- Grid and XYZ coordinates lines ---------
        model = modelBuilder.createLineGrid(80, 80, SIZE_SCALE / 5, SIZE_SCALE / 5,
                new Material(ColorAttribute.createDiffuse(GlobalAssets.DARK_COLOR_GRID_3D)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked);
        instanceGrid = new ModelInstance(model);
        DrawingTools.enableModelBlend(instanceGrid);

        model = DrawingTools.createXYZCoordinates(100f, 100f, 100f, 0.07f, 0.1f, 10, GL20.GL_TRIANGLES, new Material(), 3);
        instanceXYZCoordinates = new ModelInstance(model);

        // -------- Platform ---------
        model = modelBuilder.createBox(initialPlatformSize.x, initialPlatformSize.y, initialPlatformSize.z,
                new Material(ColorAttribute.createDiffuse(GlobalAssets.DARK_COLOR_ORANGE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instancePlatform = new ModelInstance(model);
        DrawingTools.enableModelBlend(instancePlatform);

        //instanceXYZCoordinates.transform.setToTranslation(ZERO_COORDINATE_OFFSET);
        //instancePlatform.transform.setToTranslation(ZERO_COORDINATE_OFFSET);
    }

    @Override
    public void update(float _delta) {
        if (perspectiveCamera3D != null) {
            contentPosProjected = ServoMotionsControlPanel.ortCamera.project(new Vector3(contentPos.x, contentPos.y, 0));
            contentSizeProjected = ServoMotionsControlPanel.ortCamera.project(new Vector3(contentWidth, contentHeight, 0));
            perspectiveCamera3D.viewportWidth  = contentSizeProjected.x;
            perspectiveCamera3D.viewportHeight = contentSizeProjected.y;

            perspectiveCamera3D.update();
            camInputController.update();

            extViewport3D.update((int) contentSizeProjected.x, (int) contentSizeProjected.y);
            extViewport3D.setScreenPosition((int) contentPosProjected.x, (int) contentPosProjected.y);
        }
    }

    @Override
    public void act(float _delta) {
        super.act(_delta);

        // ----------- Transform instances ------------
        Vector3 v3PlatformScale = new Vector3((GlobalVariables.platformLength * SIZE_SCALE) / initialPlatformSize.x, (GlobalVariables.platformHeight * SIZE_SCALE) / initialPlatformSize.y, (GlobalVariables.platformWidth * SIZE_SCALE) / initialPlatformSize.z);

        instanceXYZCoordinates.transform.setToTranslationAndScaling(ZERO_COORDINATE_OFFSET, new Vector3(1f, 1f, 1f));
        instancePlatform.transform.setToTranslationAndScaling(ZERO_COORDINATE_OFFSET, v3PlatformScale);
    }

    @Override
    public void draw(Batch _batch, float _parentAlpha, Color _bgColor) {
        if (!isFullScreen) {
            // Draw bg tabbed panel
            _batch.end();
            uiShapeRenderer.setProjectionMatrix(_batch.getProjectionMatrix());
            uiShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            uiShapeRenderer.setColor(GlobalAssets.DARK_COLOR_TABBED_PANEL_2);
            uiShapeRenderer.rect(contentPos.x, contentPos.y, contentWidth, contentHeight);
            uiShapeRenderer.end();
            _batch.begin();
        }
    }

    @Override
    public void draw(Batch _batch, float _parentAlpha) {
        super.draw(_batch, _parentAlpha);

        _batch.end();

        if (!isFullScreen) {
            if (Gdx.app.getGraphics().isGL30Available()) Gdx.gl30.glEnable(GL30.GL_SCISSOR_TEST);
            else Gdx.gl20.glEnable(GL20.GL_SCISSOR_TEST);
            HdpiUtils.glScissor((int) contentPosProjected.x, (int) contentPosProjected.y, (int) contentSizeProjected.x, (int) contentSizeProjected.y);
        }

        modelCache.begin(perspectiveCamera3D);
        modelCache.add(instanceGrid);
        modelCache.add(instanceXYZCoordinates);
        modelCache.add(instancePlatform);
        modelCache.end();

        modelBatch.begin(perspectiveCamera3D);
        modelBatch.render(modelCache, environment3D);
        modelBatch.end();

        if (!isFullScreen) {
            if (Gdx.app.getGraphics().isGL30Available()) Gdx.gl30.glDisable(GL30.GL_SCISSOR_TEST);
            else Gdx.gl20.glDisable(GL20.GL_SCISSOR_TEST);
        }

        _batch.begin();
    }

    @Override
    public int getID() {
        return tabID;
    }

    @Override
    public String getTitle() {
        return tabTitle;
    }

    @Override
    public void setID(int _newID) {
        tabID = _newID;
    }

    @Override
    public void setContentPos(float _x, float _y) {
        contentPos = new Vector2(_x, _y);
    }

    @Override
    public void setContentSize(float _w, float _h) {
        contentWidth  = _w;
        contentHeight = _h;

        // Костыль...
        if (!isInit) {
            init();
            isInit = true;
        }
    }

    @Override
    public void dispose() {
        uiShapeRenderer.dispose();

        stage3D.dispose();
    }
}
