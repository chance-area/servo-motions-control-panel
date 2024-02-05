package ru.chancearea.servomotionscontrolpanel.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.widget.VisLabel;

import java.util.ArrayList;

import ru.chancearea.servomotionscontrolpanel.GlobalVariables;

public class TabbedPanelsManager extends Actor {
    private final ArrayList<ITabPanel> arrTabPanels;
    private final ArrayList<VisLabel> arrVisLabels;

    private final ShapeRenderer shapeRenderer;
    private int selectedTabID = -1;

    private final float TABS_TITLES_PADDING_LR = 12f; // Left and Right
    private final float TABS_TITLES_PADDING_UB = 5f; // Up and Bottom

    public TabbedPanelsManager() {
        arrTabPanels = new ArrayList<>();
        arrVisLabels = new ArrayList<>();

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void act(float _delta) {
        super.act(_delta);

        for (int i = 0; i < arrVisLabels.size(); i++) {
            arrVisLabels.get(i).act(_delta);
            arrVisLabels.get(i).setPosition(TABS_TITLES_PADDING_LR + (i > 0 ? ((arrVisLabels.get( (i - 1) ).getX() + TABS_TITLES_PADDING_LR) + arrVisLabels.get( (i - 1) ).getWidth()) : 0), (GlobalVariables.windowHeight - arrVisLabels.get(i).getHeight()) - TABS_TITLES_PADDING_UB);
        }

        for (ITabPanel tabPanel : arrTabPanels) tabPanel.update(_delta);
    }

    @Override
    public void draw(Batch _batch, float _parentAlpha) {
        super.draw(_batch, _parentAlpha);

        for (int i = 0; i < arrVisLabels.size(); i++) {
            _batch.end();

            shapeRenderer.setProjectionMatrix(_batch.getProjectionMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor((arrTabPanels.get(i).getID() == selectedTabID ? Color.RED : Color.GRAY));
            shapeRenderer.rect(arrVisLabels.get(i).getX() - TABS_TITLES_PADDING_LR, arrVisLabels.get(i).getY() - TABS_TITLES_PADDING_UB, arrVisLabels.get(i).getWidth() + TABS_TITLES_PADDING_LR * 2, arrVisLabels.get(i).getHeight() + TABS_TITLES_PADDING_UB * 2);
            shapeRenderer.end();

            _batch.begin();

            arrVisLabels.get(i).draw(_batch, _parentAlpha);
        }

        for (ITabPanel tabPanel : arrTabPanels) tabPanel.draw(_batch, _parentAlpha);
    }

    public void addTabPanel(ITabPanel _newTabPanel) {
        _newTabPanel.setID(arrTabPanels.size());
        arrTabPanels.add(_newTabPanel);

        VisLabel newVisLabel = new VisLabel(_newTabPanel.getTitle());
        newVisLabel.setFontScale(0.7f);
        newVisLabel.pack();
        arrVisLabels.add(newVisLabel);

        if (selectedTabID == -1) selectedTabID = 0;
    }

    public void dispose() {
        shapeRenderer.dispose();
        for (ITabPanel tabPanel : arrTabPanels) tabPanel.dispose();
    }
}
