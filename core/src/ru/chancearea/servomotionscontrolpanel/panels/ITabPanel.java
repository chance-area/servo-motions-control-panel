package ru.chancearea.servomotionscontrolpanel.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

public interface ITabPanel {
    void update(float _delta);
    void draw(Batch _batch, float _parentAlpha, float _contentHeight, Color _bgColor);
    int getID();
    String getTitle();
    void setID(int _newID);

    void dispose();
}
