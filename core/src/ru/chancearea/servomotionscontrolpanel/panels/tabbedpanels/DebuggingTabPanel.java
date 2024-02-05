package ru.chancearea.servomotionscontrolpanel.panels.tabbedpanels;

import com.badlogic.gdx.graphics.g2d.Batch;

import ru.chancearea.servomotionscontrolpanel.panels.ITabPanel;

public class DebuggingTabPanel implements ITabPanel {
    private int tabID = -1;
    private String tabTitle = "Отладка";

    @Override
    public void update(float _delta) {
        //
    }

    @Override
    public void draw(Batch _batch, float _parentAlpha) {
        //
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

    }
}
