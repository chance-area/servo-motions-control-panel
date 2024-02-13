package ru.chancearea.servomotionscontrolpanel.panels.tabbedpanels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;

import ru.chancearea.servomotionscontrolpanel.GlobalVariables;
import ru.chancearea.servomotionscontrolpanel.ui.CustomSpinner;
import ru.chancearea.servomotionscontrolpanel.ui.tabs.ITabPanel;

public class ConfigurationTabPanel implements ITabPanel {
    private int tabID       = -1;
    private String tabTitle = "Конфигурирование";
    private Vector2 contentPos;
    private float contentWidth;
    private float contentHeight;

    private final ShapeRenderer shapeRenderer;

    private final CustomSpinner[] arrSpinners;

    public ConfigurationTabPanel() {
        shapeRenderer = new ShapeRenderer();

        String[] arrSpinnersTitles = new String[]{"Радиус шкива R, м", "Расстояние между моторами b, м", "Максимальная длина раскрутки нити, м"};
        float[] initialValues = new float[]{GlobalVariables.radiusWheel, GlobalVariables.distanceBetweenMotors, GlobalVariables.maxLengthThreadUnwinding};

        arrSpinners = new CustomSpinner[arrSpinnersTitles.length];

        for (int i = 0; i < arrSpinners.length; i++) {
            arrSpinners[i] = new CustomSpinner(arrSpinnersTitles[i], initialValues[i], (i == 2 ? 0.01f : 0.001f), (i == 2 ? 5 : 1), (i == 2 ? 0.01f : 0.001f));
            arrSpinners[i].setSize(GlobalVariables.isDesktop ? 520 : 658, GlobalVariables.isDesktop ? 50 : 72);
            arrSpinners[i].setPosition(80, (GlobalVariables.windowHeight + (GlobalVariables.isDesktop ? 262 : 234)) / 2 - i * arrSpinners[i].getHeight() * 2.7f * (GlobalVariables.isDesktop ? 1.35f : 1f));
            arrSpinners[i].setName((i == 0 ? "r" : (i == 1 ? "b" : "l")));
        }
    }

    @Override
    public void update(float _delta) {
        for (CustomSpinner spinner : arrSpinners) spinner.act(_delta);
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
        for (CustomSpinner spinner : arrSpinners) spinner.dispose();
    }
}
