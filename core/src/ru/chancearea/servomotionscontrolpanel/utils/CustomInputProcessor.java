package ru.chancearea.servomotionscontrolpanel.utils;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ru.chancearea.servomotionscontrolpanel.GlobalVariables;
import ru.chancearea.servomotionscontrolpanel.ServoMotionsControlPanel;

public class CustomInputProcessor implements InputProcessor {
    public static Vector2 vPointerPosition = new Vector2(-1, -1);
    public static boolean isDragged = false;

    public static Vector3 unproject(float _x, float _y) {
        if (ServoMotionsControlPanel.ortCamera != null) return ServoMotionsControlPanel.ortCamera.unproject(new Vector3(_x, _y, 0));
        else return new Vector3(-1, -1, -1);
    }

    @Override
    public boolean keyDown(int _keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int _keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char _character) {
        return false;
    }

    @Override
    public boolean touchDown(int _screenX, int _screenY, int _pointer, int _button) {
        Vector3 touchPosUnp = unproject(_screenX, _screenY);
        vPointerPosition.set(touchPosUnp.x, touchPosUnp.y);

        return false;
    }

    @Override
    public boolean touchUp(int _screenX, int _screenY, int _pointer, int _button) {
        isDragged = false;
        if (!GlobalVariables.isDesktop) vPointerPosition.set(-1, -1);

        return false;
    }

    @Override
    public boolean touchCancelled(int _screenX, int _screenY, int _pointer, int _button) {
        return false;
    }

    @Override
    public boolean touchDragged(int _screenX, int _screenY, int _pointer) {
        isDragged = true;

        return false;
    }

    @Override
    public boolean mouseMoved(int _screenX, int _screenY) {
        Vector3 touchPosUnp = unproject(_screenX, _screenY);
        vPointerPosition.set(touchPosUnp.x, touchPosUnp.y);

        return false;
    }

    @Override
    public boolean scrolled(float _amountX, float _amountY) {
        return false;
    }
}
