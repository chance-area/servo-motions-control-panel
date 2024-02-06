package ru.chancearea.servomotionscontrolpanel.ui.tabs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

public interface ITabPanel {
    void update(float _delta);
    void draw(Batch _batch, float _parentAlpha, Color _bgColor);
    int getID();
    String getTitle();
    void setID(int _newID);
    void setContentPos(float _x, float _y);
    void setContentSize(float _w, float _h);

    void dispose();
}
