package ru.chancearea.servomotionscontrolpanel.ui.tabs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
import ru.chancearea.servomotionscontrolpanel.utils.CustomInputProcessor;
import ru.chancearea.servomotionscontrolpanel.utils.DrawingTools;
import ru.chancearea.servomotionscontrolpanel.utils.MathPlus;

public class TabbedPanelsManager extends Actor {
    private final ArrayList<ITabPanel> arrTabPanels;
    private final ArrayList<VisLabel> arrVisLabels;
    private final ArrayList<Rectangle> arrTabRectangles;

    private final ShapeRenderer uiShapeRenderer;
    private int selectedTabID = 0;
    private int hoverTabID    = -1;

    private final Texture texInfoIcon;

    private final float SPACE_BETWEEN_TABS     = 0.7f;
    private final float TABS_TITLES_PADDING_LR = 12f; // Left and Right
    private final float TABS_TITLES_PADDING_UB = 7f; // Up and Bottom
    private final float LINE_HEIGHT            = 1.5f;

    public TabbedPanelsManager() {
        arrTabPanels     = new ArrayList<>();
        arrVisLabels     = new ArrayList<>();
        arrTabRectangles = new ArrayList<>();

        uiShapeRenderer = new ShapeRenderer();

        texInfoIcon = GlobalAssets.getTexture(GlobalAssets.Textures.TEXTURE_INFO_ICON);

        if (getName() == null) setName("none");
    }

    @Override
    public void act(float _delta) {
        super.act(_delta);

        int i;
        for (i = 0; i < arrVisLabels.size(); i++) {
            if (i == 0) {
                arrTabRectangles.get(i).set(getX(), (getHeight() + getY()) - (arrVisLabels.get(i).getHeight() + TABS_TITLES_PADDING_UB * 2), arrVisLabels.get(i).getWidth() + TABS_TITLES_PADDING_LR * 2, arrVisLabels.get(i).getHeight() + TABS_TITLES_PADDING_UB * 2);
            } else {
                Rectangle lastRect = arrTabRectangles.get( (i - 1) );
                arrTabRectangles.get(i).set(lastRect.getX() + lastRect.getWidth() + SPACE_BETWEEN_TABS, lastRect.getY(), arrVisLabels.get(i).getWidth() + TABS_TITLES_PADDING_LR * 2, arrVisLabels.get(i).getHeight() + TABS_TITLES_PADDING_UB * 2);
            }

            // Here 'In' = 'Info'
            if (arrVisLabels.get(i).getText().toString().equals("In")) arrTabRectangles.get(i).setX(GlobalVariables.windowWidth - arrTabRectangles.get(i).getWidth());

            arrTabPanels.get(i).setContentSize(getWidth(), getHeight() - (arrTabRectangles.get(0).getHeight() + LINE_HEIGHT));
            arrTabPanels.get(i).setContentPos(getX(), getY());

            arrVisLabels.get(i).setPosition(arrTabRectangles.get(i).getX() + TABS_TITLES_PADDING_LR, arrTabRectangles.get(i).getY() + TABS_TITLES_PADDING_UB);
            arrVisLabels.get(i).act(_delta);

            if (Gdx.input.justTouched()) {
                if (arrTabRectangles.get(i).contains(CustomInputProcessor.vPointerPosition)) {
                    selectedTabID = i;

                    if (getName().equals("main")) {
                        GlobalVariables.selectedTabID_main = i;
                        GlobalVariables.userPref.putInteger(GlobalConstants.KEY_SELECTED_TAB_ID_MAIN, i);
                    } else if (getName().equals("settings")) {
                        GlobalVariables.selectedTabID_settings = i;
                        GlobalVariables.userPref.putInteger(GlobalConstants.KEY_SELECTED_TAB_ID_SETTINGS, i);
                    }
                    GlobalVariables.userPref.flush();
                }
            }
        }

        i = 0;
        for (Rectangle rect : arrTabRectangles) {
            if (rect.contains(CustomInputProcessor.vPointerPosition)) {
                hoverTabID = arrTabPanels.get(i).getID();

                DrawingTools.setCursor(Cursor.HAND_CURSOR);
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
        uiShapeRenderer.setProjectionMatrix(_batch.getProjectionMatrix());

        // ------ Draw tab content -------
        for (ITabPanel tabPanel : arrTabPanels) {
            if (tabPanel.getID() == selectedTabID) tabPanel.draw(_batch, _parentAlpha, GlobalAssets.DARK_COLOR_TABBED_PANEL_1);
        }

        // Draw tabs
        for (int i = 0; i < arrVisLabels.size(); i++) {
            _batch.end();
            uiShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            Rectangle currentRect = arrTabRectangles.get(i);

            uiShapeRenderer.setColor(((arrTabPanels.get(i).getID() != hoverTabID) ? GlobalAssets.DARK_COLOR_TABS : GlobalAssets.DARK_COLOR_TAB_HOVER));
            uiShapeRenderer.rect(currentRect.getX(), (currentRect.getY() - LINE_HEIGHT), currentRect.getWidth(), currentRect.getHeight() + LINE_HEIGHT);

            uiShapeRenderer.end();
            _batch.begin();

            // Draw 'Info' icon
            if (arrVisLabels.get(i).getText().toString().equals("In")) {
                float iconSize = arrTabRectangles.get(i).getHeight() - TABS_TITLES_PADDING_UB * 2; // width = height = size
                _batch.draw(texInfoIcon, arrTabRectangles.get(i).getX() + (arrTabRectangles.get(i).getWidth() - iconSize) / 2f, arrTabRectangles.get(i).getY() + (arrTabRectangles.get(i).getHeight() - iconSize) / 2f, iconSize, iconSize);
            } else {
                arrVisLabels.get(i).draw(_batch, _parentAlpha);
            }
        }

        // Draw lines (filled rects) bottom tabs titles
        if (arrTabRectangles.size() > 0) {
            _batch.end();
            uiShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            uiShapeRenderer.setColor(79f / 255f, 80f / 255f, 83f / 255f, 1f);
            uiShapeRenderer.rect(getX(), arrTabRectangles.get(0).getY() - LINE_HEIGHT, getWidth(), LINE_HEIGHT);

            for (int i = 0; i < arrTabPanels.size(); i++) {
                if (arrTabPanels.get(i).getID() == selectedTabID) {
                    uiShapeRenderer.setColor(GlobalAssets.DARK_COLOR_TAB_SELECTED);
                    uiShapeRenderer.rect(arrTabRectangles.get(i).getX(), arrTabRectangles.get(i).getY() - LINE_HEIGHT, arrTabRectangles.get(i).getWidth(), LINE_HEIGHT * 3f);
                }
            }

            uiShapeRenderer.end();
            _batch.begin();
        }
    }

    public void addTabPanel(ITabPanel _newTabPanel) {
        _newTabPanel.setID(arrTabPanels.size());
        arrTabPanels.add(_newTabPanel);

        VisLabel newVisLabel = new VisLabel(_newTabPanel.getTitle());
        newVisLabel.setColor(GlobalAssets.DARK_COLOR_TABBED_TEXT);
        newVisLabel.setFontScale(GlobalVariables.isDesktop ? 0.88f : MathPlus.roundTo( (((float) Gdx.graphics.getWidth() / 100) * 4.72f) / 100f, 2 ));
        newVisLabel.pack();

        arrVisLabels.add(newVisLabel);
        arrTabRectangles.add(new Rectangle());
    }

    public void setSelectedTabID(int _tabID) {
        if (_tabID < arrTabPanels.size()) selectedTabID = _tabID;
    }

    @Override
    public void setSize(float _width, float _height) {
        super.setSize(_width, _height);

        if (_width == -1) {
            float widthAllTabRectangles = 0;
            for (Rectangle rect : arrTabRectangles) widthAllTabRectangles += rect.getWidth() + SPACE_BETWEEN_TABS;

            setWidth(widthAllTabRectangles);
        }
    }

    public void dispose() {
        uiShapeRenderer.dispose();
        for (ITabPanel tabPanel : arrTabPanels) tabPanel.dispose();
    }
}
