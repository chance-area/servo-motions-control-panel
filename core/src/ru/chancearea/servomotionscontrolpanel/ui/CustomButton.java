package ru.chancearea.servomotionscontrolpanel.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.widget.VisLabel;

import java.awt.Cursor;

import ru.chancearea.servomotionscontrolpanel.GlobalAssets;
import ru.chancearea.servomotionscontrolpanel.utils.CustomInputProcessor;
import ru.chancearea.servomotionscontrolpanel.utils.DrawingTools;

public class CustomButton extends Actor {
    private final ShapeRenderer uiShapeRenderer;

    private final Rectangle buttonRect;
    private final VisLabel nameLabel;
    private final ITouchEvent touchEvent;

    private final float BORDER_SIZE     = 2f;
    private final float TEXT_PADDING_LR = 30f; // Left-Right
    private final float TEXT_PADDING_UB = 5f; // Up-Bottom

    public CustomButton(String _text, ITouchEvent _touchEvent) {
        super();

        uiShapeRenderer = new ShapeRenderer();
        touchEvent = _touchEvent;

        nameLabel = new VisLabel(_text);
        nameLabel.setFontScale(0.85f);
        nameLabel.setColor(GlobalAssets.DARK_COLOR_TABBED_TEXT);
        nameLabel.pack();

        buttonRect = new Rectangle(getX(), getY(), nameLabel.getWidth() + (TEXT_PADDING_LR + BORDER_SIZE) * 2, nameLabel.getHeight() + (TEXT_PADDING_UB + BORDER_SIZE) * 2);
        setSize(buttonRect.getWidth(), buttonRect.getHeight());
    }

    @Override
    public void act(float _delta) {
        super.act(_delta);

        buttonRect.setPosition(getX(), getY());
        nameLabel.setPosition(buttonRect.getX() + (TEXT_PADDING_LR + BORDER_SIZE), buttonRect.getY() + (TEXT_PADDING_UB + BORDER_SIZE));

        if (buttonRect.contains(CustomInputProcessor.vPointerPosition)) {
            DrawingTools.setCursor(Cursor.HAND_CURSOR);

            if (Gdx.input.justTouched()) touchEvent.onTouch();
        }
    }

    @Override
    public void draw(Batch _batch, float _parentAlpha) {
        super.draw(_batch, _parentAlpha);

        _batch.end();
        uiShapeRenderer.setProjectionMatrix(_batch.getProjectionMatrix());

        uiShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // border
        if (buttonRect.contains(CustomInputProcessor.vPointerPosition)) uiShapeRenderer.setColor(GlobalAssets.DARK_COLOR_BUTTON_HOVER_BORDER);
        else uiShapeRenderer.setColor(GlobalAssets.DARK_COLOR_BUTTON_BORDER);
        DrawingTools.drawRoundedRect(uiShapeRenderer, buttonRect.getX() - BORDER_SIZE, buttonRect.getY() - BORDER_SIZE, buttonRect.getWidth() + BORDER_SIZE * 2, buttonRect.getHeight() + BORDER_SIZE * 2, 5f, true, true, true, true);

        // button
        if (buttonRect.contains(CustomInputProcessor.vPointerPosition)) {
            if (Gdx.input.isTouched()) uiShapeRenderer.setColor(GlobalAssets.DARK_COLOR_BUTTON_TOUCHED);
            else uiShapeRenderer.setColor(GlobalAssets.DARK_COLOR_BUTTON_HOVER);
        }
        else uiShapeRenderer.setColor(GlobalAssets.DARK_COLOR_BUTTON);
        DrawingTools.drawRoundedRect(uiShapeRenderer, buttonRect.getX(), buttonRect.getY(), buttonRect.getWidth(), buttonRect.getHeight(), 5f, true, true, true, true);

        uiShapeRenderer.end();

        _batch.begin();
        nameLabel.draw(_batch, _parentAlpha);
    }

    public void dispose() {
        uiShapeRenderer.dispose();
    }
}
