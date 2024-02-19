package ru.chancearea.servomotionscontrolpanel.panels.tabbedpanels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import ru.chancearea.servomotionscontrolpanel.GlobalVariables;
import ru.chancearea.servomotionscontrolpanel.panels.settings.ConnectionMethodPanel;
import ru.chancearea.servomotionscontrolpanel.panels.settings.RobotSettingsPanel;
import ru.chancearea.servomotionscontrolpanel.ui.CustomSpinner;
import ru.chancearea.servomotionscontrolpanel.ui.tabs.ITabPanel;
import ru.chancearea.servomotionscontrolpanel.ui.tabs.TabbedPanelsManager;

public class ConfigurationTabPanel implements ITabPanel {
    private int tabID       = -1;
    private String tabTitle = "Конфигурирование";
    private Vector2 contentPos;
    private float contentWidth;
    private float contentHeight;

    private final ShapeRenderer shapeRenderer;

    private final TabbedPanelsManager tabbedPanelsSettings;
    private final CustomSpinner[] arrSpinners;

    public ConfigurationTabPanel() {
        shapeRenderer = new ShapeRenderer();

        tabbedPanelsSettings = new TabbedPanelsManager();
        tabbedPanelsSettings.setName("settings");
        tabbedPanelsSettings.addTabPanel(new ConnectionMethodPanel());
        tabbedPanelsSettings.addTabPanel(new RobotSettingsPanel());
        tabbedPanelsSettings.setSelectedTabID(GlobalVariables.selectedTabID_settings);

        String[] arrSpinnersTitles = new String[]{"Длина платформы L, м", "Ширина платформы W, м", "Высота платформы H, м"};
        float[] initialValues = new float[]{GlobalVariables.platformLength, GlobalVariables.platformWidth, GlobalVariables.platformHeight};

        arrSpinners = new CustomSpinner[arrSpinnersTitles.length];

        for (int i = 0; i < arrSpinners.length; i++) {
            arrSpinners[i] = new CustomSpinner(arrSpinnersTitles[i], initialValues[i], 0.001f, 5f, 0.001f);
            arrSpinners[i].setSize(GlobalVariables.isDesktop ? 370 : 440, GlobalVariables.isDesktop ? 50 : 72);
            arrSpinners[i].setName((i == 0 ? "L" : (i == 1 ? "W" : "H")));
        }
    }

    @Override
    public void update(float _delta) {
        for (int i = 0; i < arrSpinners.length; i++) {
            arrSpinners[i].setPosition(40, (GlobalVariables.windowHeight + (GlobalVariables.isDesktop ? 255 : 230)) / 2 - i * arrSpinners[i].getHeight() * 2.7f * (GlobalVariables.isDesktop ? 1.35f : 1f));
            arrSpinners[i].act(_delta);
        }

        tabbedPanelsSettings.setSize(-1, (GlobalVariables.isDesktop ? 520 : 530));
        tabbedPanelsSettings.setPosition((GlobalVariables.windowWidth - tabbedPanelsSettings.getWidth()) / 2f + (GlobalVariables.isDesktop ? 205 : 240), (GlobalVariables.windowHeight - tabbedPanelsSettings.getHeight()) / 2f - (GlobalVariables.isDesktop ? 8 : 16));
        tabbedPanelsSettings.act(_delta);
    }

    @Override
    public void draw(Batch _batch, float _parentAlpha, Color _bgColor) {
        // Draw bg tabbed panel
        _batch.end();
        shapeRenderer.setProjectionMatrix(_batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(_bgColor);
        shapeRenderer.rect(contentPos.x, contentPos.y, contentWidth, contentHeight);
        shapeRenderer.end();
        _batch.begin();

        for (CustomSpinner spinner : arrSpinners) spinner.draw(_batch, _parentAlpha);

        tabbedPanelsSettings.draw(_batch, _parentAlpha);
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
        shapeRenderer.dispose();
        tabbedPanelsSettings.dispose();
        for (CustomSpinner spinner : arrSpinners) spinner.dispose();
    }
}
