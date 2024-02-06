package ru.chancearea.servomotionscontrolpanel.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class DrawingTools {
    public static void drawRoundedRect(ShapeRenderer _shapeRenderer, float _x, float _y, float _width, float _height, float _radius) {
        _shapeRenderer.rect(_x + _radius, _y + _radius, _width - _radius * 2, _height - _radius * 2);

        _shapeRenderer.rect(_x + _radius, _y, _width - _radius * 2, _radius);
        _shapeRenderer.rect(_x + _width - _radius, _y + _radius, _radius, _height - _radius * 2);
        _shapeRenderer.rect(_x + _radius, _y + _height - _radius, _width - _radius * 2, _radius);
        _shapeRenderer.rect(_x, _y + _radius, _radius, _height - _radius * 2);

        _shapeRenderer.arc(_x + _radius, _y + _radius, _radius, 180f, 90f);
        _shapeRenderer.arc(_x + _width - _radius, _y + _radius, _radius, 270f, 90f);
        _shapeRenderer.arc(_x + _width - _radius, _y + _height - _radius, _radius, 0f, 90f);
        _shapeRenderer.arc(_x + _radius, _y + _height - _radius, _radius, 90f, 90f);
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
}
