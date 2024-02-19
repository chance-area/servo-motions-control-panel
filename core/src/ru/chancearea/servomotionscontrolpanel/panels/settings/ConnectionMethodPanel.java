package ru.chancearea.servomotionscontrolpanel.panels.settings;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import ru.chancearea.servomotionscontrolpanel.GlobalAssets;
import ru.chancearea.servomotionscontrolpanel.ui.tabs.ITabPanel;

public class ConnectionMethodPanel implements ITabPanel {
    private int tabID       = -1;
    private String tabTitle = "Способ подключения к ESP32";
    private Vector2 contentPos;
    private float contentWidth;
    private float contentHeight;

    private final ShapeRenderer uiShapeRenderer;

    public ConnectionMethodPanel() {
        uiShapeRenderer = new ShapeRenderer();
    }

    @Override
    public void update(float _delta) {
        // TODO
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

        // TODO
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
    }
}
