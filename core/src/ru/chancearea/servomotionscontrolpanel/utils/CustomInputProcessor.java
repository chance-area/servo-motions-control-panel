package ru.chancearea.servomotionscontrolpanel.utils;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import ru.chancearea.servomotionscontrolpanel.GlobalVariables;

public class CustomInputProcessor implements InputProcessor {
    public static Vector2 vPointerPosition = new Vector2();

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        vPointerPosition.set(screenX, screenY);

        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
