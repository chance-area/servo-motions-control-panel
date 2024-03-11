package ru.chancearea.servomotionscontrolpanel.panels.tabbedpanels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import ru.chancearea.servomotionscontrolpanel.ui.tabs.ITabPanel;

public class AnalyticsTabPanel implements ITabPanel {
    private int tabID       = -1;
    private String tabTitle = "Аналитика";
    private Vector2 contentPos;
    private float contentWidth;
    private float contentHeight;

    private final ShapeRenderer shapeRenderer;

    public AnalyticsTabPanel() {
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void update(float _delta) {
        // TODO
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
        shapeRenderer.dispose();
    }
}
