package ru.chancearea.servomotionscontrolpanel.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ru.chancearea.servomotionscontrolpanel.GlobalVariables;

public class TemplateTabPanel implements ITabPanel {
    private int tabID       = -1;
    private String tabTitle = "Шаблон";

    private final ShapeRenderer shapeRenderer;

    public TemplateTabPanel() {
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void update(float _delta) {
        // TODO
    }

    @Override
    public void draw(Batch _batch, float _parentAlpha, float _contentHeight, Color _bgColor) {
        // Draw bg tabbed panel
        _batch.end();
        shapeRenderer.setProjectionMatrix(_batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(_bgColor);
        shapeRenderer.rect(0, 0, GlobalVariables.windowWidth, _contentHeight);
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
    public void dispose() {
        shapeRenderer.dispose();
    }
}
