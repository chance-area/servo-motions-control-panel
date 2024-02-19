package ru.chancearea.servomotionscontrolpanel.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.widget.VisLabel;

import java.awt.Cursor;

import ru.chancearea.servomotionscontrolpanel.GlobalAssets;
import ru.chancearea.servomotionscontrolpanel.GlobalConstants;
import ru.chancearea.servomotionscontrolpanel.GlobalVariables;
import ru.chancearea.servomotionscontrolpanel.utils.CustomInputProcessor;
import ru.chancearea.servomotionscontrolpanel.utils.DrawingTools;
import ru.chancearea.servomotionscontrolpanel.utils.MathPlus;

public class CustomSpinner extends Actor {
    private final ShapeRenderer uiShapeRenderer;

    private final VisLabel titleLabel;
    private final VisLabel valueLabel;

    private final float step;
    private float value;
    private final float minValue;
    private final float maxValue;

    private final float BORDER_SIZE        = 2f;
    private final float BORDER_RADIUS      = 4f;
    private final float BUTTONS_SPACE_SIZE = GlobalVariables.isDesktop ? 32f : 44f;

    private final Rectangle rectTopButton;
    private final Rectangle rectBottomButton;

    private int touchHoldNumTicks = -1;

    public CustomSpinner(String _title, float _defaultVal, float _minVal, float _maxVal, float _step) {
        super();

        titleLabel = new VisLabel(_title);
        titleLabel.setFontScale(0.72f);
        titleLabel.setColor(GlobalAssets.DARK_COLOR_TABBED_TEXT);
        titleLabel.pack();

        valueLabel = new VisLabel(String.valueOf(_defaultVal));
        valueLabel.setFontScale(0.7f);
        valueLabel.setColor(GlobalAssets.DARK_COLOR_WHITE);
        valueLabel.pack();

        rectTopButton    = new Rectangle();
        rectBottomButton = new Rectangle();

        value    = _defaultVal;
        minValue = _minVal;
        maxValue = _maxVal;
        step     = _step;

        uiShapeRenderer = new ShapeRenderer();
    }

    @Override
    public void act(float _delta) {
        super.act(_delta);

        rectTopButton.set(getX() + getWidth() + BORDER_SIZE - BUTTONS_SPACE_SIZE, getY() + getHeight() / 2f, BUTTONS_SPACE_SIZE - BORDER_SIZE, getHeight() / 2f);
        rectBottomButton.set(getX() + getWidth() + BORDER_SIZE - BUTTONS_SPACE_SIZE, getY(), BUTTONS_SPACE_SIZE - BORDER_SIZE, getHeight() / 2f);

        if (rectTopButton.contains(CustomInputProcessor.vPointerPosition) || rectBottomButton.contains(CustomInputProcessor.vPointerPosition)) {
            DrawingTools.setCursor(Cursor.HAND_CURSOR);

            if (Gdx.input.isTouched()) touchHoldNumTicks += 1;
            else touchHoldNumTicks = -1;

            if (Gdx.input.justTouched() || touchHoldNumTicks >= _delta * 3800) {
                touchHoldNumTicks -= 5;

                float localStep = step * ( (touchHoldNumTicks >= (_delta * 3800 - 5)) ? 6f : 1f );
                value += (rectTopButton.contains(CustomInputProcessor.vPointerPosition) ? localStep : (rectBottomButton.contains(CustomInputProcessor.vPointerPosition) ? -localStep : 0));
                value = Math.max(Math.min(MathPlus.roundTo(value, 4), maxValue), minValue);

                if (getName().equals("L")) {
                    GlobalVariables.platformLength = value;
                    GlobalVariables.userPref.putFloat(GlobalConstants.KEY_PLATFORM_LENGTH, value);
                } else if (getName().equals("W")) {
                    GlobalVariables.platformWidth = value;
                    GlobalVariables.userPref.putFloat(GlobalConstants.KEY_PLATFORM_WIDTH, value);
                } else if (getName().equals("H")) {
                    GlobalVariables.platformHeight = value;
                    GlobalVariables.userPref.putFloat(GlobalConstants.KEY_PLATFORM_HEIGHT, value);
                }
                GlobalVariables.userPref.flush();

                valueLabel.setText(String.valueOf(value));
                valueLabel.pack();
            }
        }

        titleLabel.setPosition(getX() + ((getWidth() - BORDER_SIZE * 2) - titleLabel.getWidth()) / 2f, getY() + getHeight() + 24f);
        titleLabel.act(_delta);

        valueLabel.setPosition(getX() + (getWidth() - valueLabel.getWidth()) / 2f, getY() + (getHeight() - valueLabel.getHeight()) / 2f);
        valueLabel.act(_delta);
    }

    @Override
    public void draw(Batch _batch, float _parentAlpha) {
        super.draw(_batch, _parentAlpha);
        _batch.end();
        uiShapeRenderer.setProjectionMatrix(_batch.getProjectionMatrix());

        uiShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // border
        uiShapeRenderer.setColor(GlobalAssets.DARK_COLOR_SPINNER_BORDER);
        DrawingTools.drawRoundedRect(uiShapeRenderer, getX() - BORDER_SIZE, getY() - BORDER_SIZE, getWidth() + BORDER_SIZE * 2, getHeight() + BORDER_SIZE * 2, BORDER_RADIUS, true, true, true, true);
        // spinner bg
        uiShapeRenderer.setColor(GlobalAssets.DARK_COLOR_SPINNER);
        DrawingTools.drawRoundedRect(uiShapeRenderer, getX(), getY(), getWidth(), getHeight(), BORDER_RADIUS, true, true, true, true);

        uiShapeRenderer.setColor(GlobalAssets.DARK_COLOR_SPINNER_BORDER);
        uiShapeRenderer.rect(getX() + getWidth() - BUTTONS_SPACE_SIZE, getY(), BORDER_SIZE, getHeight());

        // Top and bottom buttons (rectangles)
        uiShapeRenderer.setColor(GlobalAssets.DARK_COLOR_SPINNER_BUTTON);
        /*if (rectTopButton.contains(CustomInputProcessor.vPointerPosition)) shapeRenderer.setColor(62f / 255f, 65f / 255f, 67f / 255f, 1f);
        else shapeRenderer.setColor(65f / 255f, 68f / 255f, 70f / 255f, 1f);*/
        DrawingTools.drawRoundedRect(uiShapeRenderer, rectTopButton.getX(), rectTopButton.getY(), rectTopButton.getWidth(), rectTopButton.getHeight(), (BORDER_RADIUS - BORDER_SIZE), false, true, false, false);

        /*if (rectBottomButton.contains(CustomInputProcessor.vPointerPosition)) shapeRenderer.setColor(62f / 255f, 65f / 255f, 67f / 255f, 1f);
        else shapeRenderer.setColor(65f / 255f, 68f / 255f, 70f / 255f, 1f);*/
        DrawingTools.drawRoundedRect(uiShapeRenderer, rectBottomButton.getX(), rectBottomButton.getY(), rectBottomButton.getWidth(), rectBottomButton.getHeight(), (BORDER_RADIUS - BORDER_SIZE), false, false, false, true);

        // Arrows
        float butPadding = GlobalVariables.isDesktop ? 9f : 12f;

        if (rectTopButton.contains(CustomInputProcessor.vPointerPosition)) uiShapeRenderer.setColor(GlobalAssets.DARK_COLOR_WHITE);
        else uiShapeRenderer.setColor(155f / 255f, 155f / 255f, 155f / 255f, 1f);
        uiShapeRenderer.triangle(rectTopButton.getX() + butPadding, rectTopButton.getY() + (butPadding - butPadding / 5), rectTopButton.getX() + rectTopButton.getWidth() / 2f, rectTopButton.getY() + rectTopButton.getHeight() - (butPadding - butPadding / 7), rectTopButton.getX() + rectTopButton.getWidth() - butPadding, rectTopButton.getY() + (butPadding - butPadding / 5));

        if (rectBottomButton.contains(CustomInputProcessor.vPointerPosition)) uiShapeRenderer.setColor(GlobalAssets.DARK_COLOR_WHITE);
        else uiShapeRenderer.setColor(155f / 255f, 155f / 255f, 155f / 255f, 1f);
        uiShapeRenderer.triangle(rectBottomButton.getX() + butPadding, rectBottomButton.getY() + rectBottomButton.getHeight() - (butPadding - butPadding / 5), rectBottomButton.getX() + rectBottomButton.getWidth() / 2f, rectBottomButton.getY() + (butPadding - butPadding / 7), rectBottomButton.getX() + rectBottomButton.getWidth() - butPadding, rectBottomButton.getY() + rectBottomButton.getHeight() - (butPadding - butPadding / 5));

        uiShapeRenderer.end();
        _batch.begin();

        titleLabel.draw(_batch, _parentAlpha);
        valueLabel.draw(_batch, _parentAlpha);
    }

    public void dispose() {
        uiShapeRenderer.dispose();
    }
}
