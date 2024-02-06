package ru.chancearea.servomotionscontrolpanel.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.widget.VisLabel;

import java.awt.Cursor;
import java.util.ArrayList;

import ru.chancearea.servomotionscontrolpanel.GlobalAssets;
import ru.chancearea.servomotionscontrolpanel.GlobalConstants;
import ru.chancearea.servomotionscontrolpanel.GlobalVariables;
import ru.chancearea.servomotionscontrolpanel.ServoMotionsControlPanel;
import ru.chancearea.servomotionscontrolpanel.utils.CustomInputProcessor;

public class TabbedPanelsManager extends Actor {
    private final ArrayList<ITabPanel> arrTabPanels;
    private final ArrayList<VisLabel> arrVisLabels;
    private final ArrayList<Rectangle> arrTabRectangles;

    private final ShapeRenderer shapeRenderer;
    private int selectedTabID = -1;
    private int hoverTabID    = -1;

    private final float SPACE_BETWEEN_TABS     = 0.7f;
    private final float TABS_TITLES_PADDING_LR = 12f; // Left and Right
    private final float TABS_TITLES_PADDING_UB = 7f; // Up and Bottom
    private final float LINE_HEIGHT            = 1.5f;

    public TabbedPanelsManager() {
        arrTabPanels     = new ArrayList<>();
        arrVisLabels     = new ArrayList<>();
        arrTabRectangles = new ArrayList<>();

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void act(float _delta) {
        super.act(_delta);

        int i;
        for (i = 0; i < arrVisLabels.size(); i++) {
            float sumSpacing = TABS_TITLES_PADDING_LR + (i > 0 ? SPACE_BETWEEN_TABS : 0);

            arrVisLabels.get(i).act(_delta);
            arrVisLabels.get(i).setPosition(sumSpacing + (i > 0 ? ((arrVisLabels.get( (i - 1) ).getX() + sumSpacing) + arrVisLabels.get( (i - 1) ).getWidth()) : 0), (GlobalVariables.windowHeight - arrVisLabels.get(i).getHeight()) - TABS_TITLES_PADDING_UB + LINE_HEIGHT / 2f);

            if (Gdx.input.justTouched()) {
                if (arrTabRectangles.get(i).contains(CustomInputProcessor.vPointerPosition)) selectedTabID = i;
            }
        }

        i = 0;
        for (Rectangle rect : arrTabRectangles) {
            if (rect.contains(CustomInputProcessor.vPointerPosition)) {
                hoverTabID = arrTabPanels.get(i).getID();

                if (GlobalVariables.isDesktop) ServoMotionsControlPanel.superDuperJFrame.setCursor(new Cursor(Cursor.HAND_CURSOR));

                break;
            }

            hoverTabID = -1;
            i++;
        }

        // ------- Update tab content --------
        for (ITabPanel tabPanel : arrTabPanels) {
            if (tabPanel.getID() == selectedTabID) tabPanel.update(_delta);
        }
    }

    @Override
    public void draw(Batch _batch, float _parentAlpha) {
        super.draw(_batch, _parentAlpha);
        shapeRenderer.setProjectionMatrix(_batch.getProjectionMatrix());

        // ------ Draw tab content -------
        for (ITabPanel tabPanel : arrTabPanels) {
            if (tabPanel.getID() == selectedTabID) tabPanel.draw(_batch, _parentAlpha, GlobalVariables.windowHeight - (arrTabRectangles.get(0).getHeight() + LINE_HEIGHT), GlobalAssets.DARK_COLOR_TABBED_PANELS);
        }

        // Draw tabs
        for (int i = 0; i < arrVisLabels.size(); i++) {
            _batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            Rectangle currentRect = arrTabRectangles.get(i);

            shapeRenderer.setColor((arrTabPanels.get(i).getID() != hoverTabID ? GlobalAssets.DARK_COLOR_TABS : GlobalAssets.DARK_COLOR_TAB_HOVER));
            shapeRenderer.rect(currentRect.getX(), currentRect.getY() - LINE_HEIGHT, currentRect.getWidth(), currentRect.getHeight() + LINE_HEIGHT);

            shapeRenderer.end();
            _batch.begin();

            arrVisLabels.get(i).draw(_batch, _parentAlpha);
        }

        // Draw lines (filled rects) bottom tabs titles
        if (arrTabRectangles.size() > 0) {
            _batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            shapeRenderer.setColor(79f / 255f, 80f / 255f, 83f / 255f, 1f);
            shapeRenderer.rect(0, arrTabRectangles.get(0).getY() - LINE_HEIGHT, GlobalVariables.windowWidth, LINE_HEIGHT);

            for (int i = 0; i < arrTabPanels.size(); i++) {
                if (arrTabPanels.get(i).getID() == selectedTabID) {
                    shapeRenderer.setColor(GlobalAssets.DARK_COLOR_TAB_SELECTED);
                    shapeRenderer.rect(arrTabRectangles.get(i).getX(), arrTabRectangles.get(i).getY() - LINE_HEIGHT, arrTabRectangles.get(i).getWidth(), LINE_HEIGHT * 3f);
                }
            }

            shapeRenderer.end();
            _batch.begin();
        }
    }

    public void addTabPanel(ITabPanel _newTabPanel) {
        _newTabPanel.setID(arrTabPanels.size());
        arrTabPanels.add(_newTabPanel);

        VisLabel newVisLabel = new VisLabel(_newTabPanel.getTitle());
        newVisLabel.setColor(GlobalAssets.DARK_COLOR_TABBED_TEXTS);
        newVisLabel.setFontScale(0.7f);
        newVisLabel.pack();
        arrVisLabels.add(newVisLabel);

        Rectangle rect;
        if (arrTabRectangles.size() == 0) {
            rect = new Rectangle(0, GlobalVariables.windowHeight - (newVisLabel.getHeight() + TABS_TITLES_PADDING_UB * 2), newVisLabel.getWidth() + TABS_TITLES_PADDING_LR * 2, newVisLabel.getHeight() + TABS_TITLES_PADDING_UB * 2);
        } else {
            Rectangle lastRect = arrTabRectangles.get( (arrTabRectangles.size() - 1) );
            rect = new Rectangle(lastRect.getX() + lastRect.getWidth() + SPACE_BETWEEN_TABS, lastRect.getY(), newVisLabel.getWidth() + TABS_TITLES_PADDING_LR * 2, newVisLabel.getHeight() + TABS_TITLES_PADDING_UB * 2);
        }
        arrTabRectangles.add(rect);

        if (selectedTabID == -1) selectedTabID = 0;
    }

    public void dispose() {
        shapeRenderer.dispose();
        for (ITabPanel tabPanel : arrTabPanels) tabPanel.dispose();
    }
}
