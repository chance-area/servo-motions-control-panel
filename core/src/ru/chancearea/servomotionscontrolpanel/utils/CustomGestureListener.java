package ru.chancearea.servomotionscontrolpanel.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class CustomGestureListener implements GestureListener {
    @Override
    public boolean touchDown(float _x, float _y, int _pointer, int _button) {
        return false;
    }

    @Override
    public boolean tap(float _x, float _y, int _count, int _button) {
        return false;
    }

    @Override
    public boolean longPress(float _x, float _y) {
        return false;
    }

    @Override
    public boolean fling(float _velocityX, float _velocityY, int _button) {
        return false;
    }

    @Override
    public boolean pan(float _x, float _y, float _deltaX, float _deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float _x, float _y, int _pointer, int _button) {
        return false;
    }

    @Override
    public boolean zoom(float _initialDistance, float _distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 _initialPointer1, Vector2 _initialPointer2, Vector2 _pointer1, Vector2 _pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {
        //
    }
}
