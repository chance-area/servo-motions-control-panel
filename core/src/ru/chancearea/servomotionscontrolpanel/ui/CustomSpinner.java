package ru.chancearea.servomotionscontrolpanel.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.widget.VisLabel;

import java.awt.Cursor;

import ru.chancearea.servomotionscontrolpanel.GlobalAssets;
import ru.chancearea.servomotionscontrolpanel.GlobalVariables;
import ru.chancearea.servomotionscontrolpanel.ServoMotionsControlPanel;
import ru.chancearea.servomotionscontrolpanel.utils.CustomInputProcessor;
import ru.chancearea.servomotionscontrolpanel.utils.DrawingTools;
import ru.chancearea.servomotionscontrolpanel.utils.MathPlus;

public class CustomSpinner extends Actor {
    private final ShapeRenderer shapeRenderer;

    private final VisLabel titleLabel;
    private final VisLabel valueLabel;

    private final float step;
    private float value;
    private final float minValue;
    private final float maxValue;

    private final float BORDER_SIZE        = 2f;
    private final float BORDER_RADIUS      = 4f;
    private final float BUTTONS_SPACE_SIZE = GlobalVariables.isDesktop ? 30f : 42f;

    private Rectangle rectTopButton;
    private Rectangle rectBottomButton;

    public CustomSpinner(String _title, float _defaultVal, float _minVal, float _maxVal, float _step) {
        super();

        titleLabel = new VisLabel(_title);
        titleLabel.setFontScale(GlobalVariables.isDesktop ? 0.4f : 0.42f);
        titleLabel.setColor(GlobalAssets.DARK_COLOR_TABBED_TEXTS);
        titleLabel.pack();

        valueLabel = new VisLabel(String.valueOf(_defaultVal));
        valueLabel.setFontScale(GlobalVariables.isDesktop ? 0.38f : 0.4f);
        valueLabel.setColor(GlobalAssets.DARK_COLOR_WHITE);
        valueLabel.pack();

        value    = _defaultVal;
        minValue = _minVal;
        maxValue = _maxVal;
        step     = _step;

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void act(float _delta) {
        super.act(_delta);

        if (rectTopButton == null) {
            rectTopButton    = new Rectangle(getX() + getWidth() + BORDER_SIZE - BUTTONS_SPACE_SIZE, getY() + getHeight() / 2f, BUTTONS_SPACE_SIZE - BORDER_SIZE, getHeight() / 2f);
            rectBottomButton = new Rectangle(getX() + getWidth() + BORDER_SIZE - BUTTONS_SPACE_SIZE, getY(), BUTTONS_SPACE_SIZE - BORDER_SIZE, getHeight() / 2f);
        } else {
            if (rectTopButton.contains(CustomInputProcessor.vPointerPosition) || rectBottomButton.contains(CustomInputProcessor.vPointerPosition)) {
                if (GlobalVariables.isDesktop) ServoMotionsControlPanel.superDuperJFrame.setCursor(new Cursor(Cursor.HAND_CURSOR));

                if (Gdx.input.justTouched()) {
                    value += (rectTopButton.contains(CustomInputProcessor.vPointerPosition) ? step : (rectBottomButton.contains(CustomInputProcessor.vPointerPosition) ? -step : 0));
                    value = MathPlus.roundTo(value, 5);

                    if (value > maxValue)      value = maxValue;
                    else if (value < minValue) value = minValue;

                    if (getName().equals("r"))      GlobalVariables.radiusWheel              = value;
                    else if (getName().equals("b")) GlobalVariables.distanceBetweenMotors    = value;
                    else if (getName().equals("l")) GlobalVariables.maxLengthThreadUnwinding = value;

                    valueLabel.setText(String.valueOf(value));
                    valueLabel.pack();
                }
            }
        }

        titleLabel.setPosition(getX(), getY() + getHeight() + 24f);
        titleLabel.act(_delta);

        valueLabel.setPosition(getX() + (getWidth() - valueLabel.getWidth()) / 2f, getY() + (getHeight() - valueLabel.getHeight()) / 2f);
        valueLabel.act(_delta);
    }

    @Override
    public void draw(Batch _batch, float _parentAlpha) {
        super.draw(_batch, _parentAlpha);
        _batch.end();
        shapeRenderer.setProjectionMatrix(_batch.getProjectionMatrix());

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(91f / 255f, 92f / 255f, 95f / 255f, 1f);
        DrawingTools.drawRoundedRect(shapeRenderer, getX() - BORDER_SIZE, getY() - BORDER_SIZE, getWidth() + BORDER_SIZE * 2, getHeight() + BORDER_SIZE * 2, BORDER_RADIUS);
        shapeRenderer.setColor(70f / 255f, 73f / 255f, 75f / 255f, 1f);
        DrawingTools.drawRoundedRect(shapeRenderer, getX(), getY(), getWidth(), getHeight(), BORDER_RADIUS);

        shapeRenderer.setColor(91f / 255f, 92f / 255f, 95f / 255f, 1f);
        shapeRenderer.rect(getX() + getWidth() - BUTTONS_SPACE_SIZE, getY(), BORDER_SIZE, getHeight());

        shapeRenderer.setColor(91f / 255f, 92f / 255f, 95f / 255f, 1f);
        shapeRenderer.rect(getX() + getWidth() - (BUTTONS_SPACE_SIZE - BORDER_SIZE), getY() + getHeight() / 2 - 0.5f, (BUTTONS_SPACE_SIZE - BORDER_SIZE), 1);

        // Top and bottom buttons
        if (rectTopButton.contains(CustomInputProcessor.vPointerPosition)) shapeRenderer.setColor(62f / 255f, 65f / 255f, 67f / 255f, 1f);
        else shapeRenderer.setColor(65f / 255f, 68f / 255f, 70f / 255f, 1f);
        shapeRenderer.rect(rectTopButton.getX(), rectTopButton.getY(), rectTopButton.getWidth(), rectTopButton.getHeight());

        if (rectBottomButton.contains(CustomInputProcessor.vPointerPosition)) shapeRenderer.setColor(62f / 255f, 65f / 255f, 67f / 255f, 1f);
        else shapeRenderer.setColor(65f / 255f, 68f / 255f, 70f / 255f, 1f);
        shapeRenderer.rect(rectBottomButton.getX(), rectBottomButton.getY(), rectBottomButton.getWidth(), rectBottomButton.getHeight());

        // Arrows
        float butPadding = GlobalVariables.isDesktop ? 8f : 12f;
        shapeRenderer.setColor(155f / 255f, 155f / 255f, 155f / 255f, 1f);
        shapeRenderer.triangle(rectTopButton.getX() + butPadding, rectTopButton.getY() + (butPadding - butPadding / 5), rectTopButton.getX() + rectTopButton.getWidth() / 2f, rectTopButton.getY() + rectTopButton.getHeight() - (butPadding - butPadding / 7), rectTopButton.getX() + rectTopButton.getWidth() - butPadding, rectTopButton.getY() + (butPadding - butPadding / 5));
        shapeRenderer.triangle(rectBottomButton.getX() + butPadding, rectBottomButton.getY() + rectBottomButton.getHeight() - (butPadding - butPadding / 5), rectBottomButton.getX() + rectBottomButton.getWidth() / 2f, rectBottomButton.getY() + (butPadding - butPadding / 7), rectBottomButton.getX() + rectBottomButton.getWidth() - butPadding, rectBottomButton.getY() + rectBottomButton.getHeight() - (butPadding - butPadding / 5));

        shapeRenderer.end();
        _batch.begin();

        titleLabel.draw(_batch, _parentAlpha);
        valueLabel.draw(_batch, _parentAlpha);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
