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
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ru.chancearea.servomotionscontrolpanel.GlobalAssets;
import ru.chancearea.servomotionscontrolpanel.GlobalVariables;
import ru.chancearea.servomotionscontrolpanel.ServoMotionsControlPanel;
import ru.chancearea.servomotionscontrolpanel.ui.tabs.ITabPanel;
import ru.chancearea.servomotionscontrolpanel.utils.DrawingTools;

public class RobotSettingsPanel implements ITabPanel {
    private int tabID       = -1;
    private final String tabTitle = "Настройка робота";
    private Vector2 contentPos;
    private float contentWidth;
    private float contentHeight;

    private final ShapeRenderer uiShapeRenderer;

    private final ModelBuilder modelBuilder;
    private final ModelBatch modelBatch;
    private final ModelCache modelCache;

    private ModelInstance instanceGrid;
    private ModelInstance instanceXYZCoordinates;
    private ModelInstance instancePlatform;

    public static PerspectiveCamera perspectiveCamera = null;
    private final Environment environment3D;
    public static CameraInputController camInputController = null;
    final float[] startPos = {-50f, 90f, 130f};

    private final float SIZE_SCALE = 100f;

    private final Vector3 initialPlatformSize;

    public RobotSettingsPanel() {
        uiShapeRenderer = new ShapeRenderer();

        modelBuilder = new ModelBuilder();
        modelBatch   = new ModelBatch();
        modelCache   = new ModelCache();

        //turnOnAngles(100, 100, 100);

        perspectiveCamera = new PerspectiveCamera(67, GlobalVariables.windowWidth, GlobalVariables.windowHeight);
        perspectiveCamera.position.set(startPos[0], startPos[1], startPos[2]);
        perspectiveCamera.lookAt(0, 0, 0);
        perspectiveCamera.near = 20f;
        perspectiveCamera.far = 1024f;
        perspectiveCamera.update();
        perspectiveCamera.translate(-GlobalVariables.platformLength * SIZE_SCALE / 2f + 10, 10f, 0f);

        camInputController = new CameraInputController(perspectiveCamera);
        camInputController.zoom(0);
        camInputController.scrollFactor = -0.05f;
        camInputController.translateUnits = 300;
        camInputController.rotateAngle = 170;
        camInputController.setInvertedControls(false);
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
    }

    private void initModels() {
        Model model;

        // -------- Grid and XYZ coordinates lines ---------
        model = modelBuilder.createLineGrid(80, 80, 10f, 10f,
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
    }

    @Override
    public void update(float _delta) {
        perspectiveCamera.update(true);
        camInputController.update();

        instancePlatform.transform.setToScaling((GlobalVariables.platformLength * SIZE_SCALE) / initialPlatformSize.x, (GlobalVariables.platformHeight * SIZE_SCALE) / initialPlatformSize.y, (GlobalVariables.platformWidth * SIZE_SCALE) / initialPlatformSize.z);
    }

    @Override
    public void draw(Batch _batch, float _parentAlpha, Color _bgColor) {
        // Draw bg tabbed panel
        _batch.end();
        uiShapeRenderer.setProjectionMatrix(_batch.getProjectionMatrix());
        uiShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        uiShapeRenderer.setColor(GlobalAssets.DARK_COLOR_TABBED_PANEL_2);
        uiShapeRenderer.rect(contentPos.x, contentPos.y, contentWidth, contentHeight);
        uiShapeRenderer.end();
        _batch.begin();

        // ....

        _batch.end();

        Vector3 contentPosProjected  = ServoMotionsControlPanel.ortCamera.project(new Vector3(contentPos.x, contentPos.y, 0));
        Vector3 contentSizeProjected = ServoMotionsControlPanel.ortCamera.project(new Vector3(contentWidth, contentHeight, 0));

        if (Gdx.app.getGraphics().isGL30Available()) Gdx.gl30.glEnable(GL30.GL_SCISSOR_TEST);
        else Gdx.gl20.glEnable(GL20.GL_SCISSOR_TEST);
        HdpiUtils.glScissor((int) contentPosProjected.x, (int) contentPosProjected.y, (int) contentSizeProjected.x, (int) contentSizeProjected.y);

        modelCache.begin(perspectiveCamera);
        modelCache.add(instanceGrid);
        modelCache.add(instanceXYZCoordinates);
        modelCache.add(instancePlatform);
        modelCache.end();

        modelBatch.begin(perspectiveCamera);
        modelBatch.render(modelCache, environment3D);
        modelBatch.end();

        if (Gdx.app.getGraphics().isGL30Available()) Gdx.gl30.glDisable(GL30.GL_SCISSOR_TEST);
        else Gdx.gl20.glDisable(GL20.GL_SCISSOR_TEST);

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
    }

    @Override
    public void dispose() {
        uiShapeRenderer.dispose();
        modelBatch.dispose();
        modelCache.dispose();
    }
}
