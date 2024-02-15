package ru.chancearea.servomotionscontrolpanel.utils;

import com.badlogic.gdx.Gdx;
import java.awt.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ru.chancearea.servomotionscontrolpanel.GlobalVariables;
import ru.chancearea.servomotionscontrolpanel.ServoMotionsControlPanel;

public abstract class DrawingTools {
    public static void drawRoundedRect(ShapeRenderer _shapeRenderer, float _x, float _y, float _width, float _height, float _radius, boolean _topLeft, boolean _topRight, boolean _bottomLeft, boolean _bottomRight) {
        _shapeRenderer.rect(_x + _radius, _y + _radius, _width - _radius * 2f, _height - _radius * 2f);

        if (_topLeft) {
            _shapeRenderer.rect(_x, _y + _radius, _radius, _height - _radius * 2f);
            _shapeRenderer.arc(_x + _radius, _y + _height - _radius, _radius, 90f, 90f);
        } else _shapeRenderer.rect(_x, _y + _radius, _radius, _height - _radius);

        if (_topRight) {
            _shapeRenderer.rect(_x + _radius, _y + _height - _radius, _width - _radius * 2f, _radius);
            _shapeRenderer.arc(_x + _width - _radius, _y + _height - _radius, _radius, 0f, 90f);
        } else _shapeRenderer.rect(_x + _radius, _y + _height - _radius, _width - _radius, _radius);

        if (_bottomLeft) {
            _shapeRenderer.rect(_x + _radius, _y, _width - _radius * 2f, _radius);
            _shapeRenderer.arc(_x + _radius, _y + _radius, _radius, 180f, 90f);
        } else _shapeRenderer.rect(_x, _y, _width - _radius, _radius);

        if (_bottomRight) {
            _shapeRenderer.rect(_x + _width - _radius, _y + _radius, _radius, _height - _radius * 2f);
            _shapeRenderer.arc(_x + _width - _radius, _y + _radius, _radius, 270f, 90f);
        } else _shapeRenderer.rect(_x + _width - _radius, _y, _radius, _height - _radius);
    }

    public static void enableGLBlend() {
        if (Gdx.app.getGraphics().isGL30Available()) {
            Gdx.gl30.glEnable(GL30.GL_BLEND);
            Gdx.gl30.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        } else {
            Gdx.gl20.glEnable(GL20.GL_BLEND);
            Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    public static void disableGLBlend() {
        if (Gdx.app.getGraphics().isGL30Available()) Gdx.gl30.glDisable(GL30.GL_BLEND);
        else Gdx.gl20.glDisable(GL20.GL_BLEND);
    }

    public static void setCursor(int _cursor) {
        if (GlobalVariables.isDesktop && ServoMotionsControlPanel.superDuperJFrame != null) ServoMotionsControlPanel.superDuperJFrame.setCursor(new Cursor(_cursor));
    }
}
