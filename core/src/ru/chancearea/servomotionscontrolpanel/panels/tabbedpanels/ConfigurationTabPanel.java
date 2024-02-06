package ru.chancearea.servomotionscontrolpanel.panels.tabbedpanels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ru.chancearea.servomotionscontrolpanel.GlobalAssets;
import ru.chancearea.servomotionscontrolpanel.GlobalConstants;
import ru.chancearea.servomotionscontrolpanel.GlobalVariables;
import ru.chancearea.servomotionscontrolpanel.panels.ITabPanel;

public class ConfigurationTabPanel implements ITabPanel {
    private int tabID       = -1;
    private String tabTitle = "Конфигурирование";

    private final ShapeRenderer shapeRenderer;

    public ConfigurationTabPanel() {
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void update(float _delta) {
        //
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
