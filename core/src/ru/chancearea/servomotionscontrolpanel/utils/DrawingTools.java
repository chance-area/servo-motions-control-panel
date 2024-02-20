package ru.chancearea.servomotionscontrolpanel.utils;

import com.badlogic.gdx.Gdx;
import java.awt.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ru.chancearea.servomotionscontrolpanel.GlobalAssets;
import ru.chancearea.servomotionscontrolpanel.GlobalVariables;
import ru.chancearea.servomotionscontrolpanel.ServoMotionsControlPanel;

public abstract class DrawingTools {
    public static void drawRoundedRect(ShapeRenderer _shapeRenderer, float _x, float _y, float _width, float _height, float _radius, boolean _topLeft, boolean _topRight, boolean _bottomLeft, boolean _bottomRight) {
        _shapeRenderer.rect(_x + _radius, _y + _radius, _width - _radius * 2f, _height - _radius * 2f);

        if (_topLeft) {
            _shapeRenderer.rect(_x, _y + _radius, _radius, _height - _radius * 2f);
            _shapeRenderer.arc(_x + _radius, _y + _height - _radius, _radius, 90f, 90f);
        } else _shapeRenderer.rect(_x, _y + _radius, _radius, _height - _radius);

        if (_topRight) {
            _shapeRenderer.rect(_x + _radius, _y + _height - _radius, _width - _radius * 2f, _radius);
            _shapeRenderer.arc(_x + _width - _radius, _y + _height - _radius, _radius, 0f, 90f);
        } else _shapeRenderer.rect(_x + _radius, _y + _height - _radius, _width - _radius, _radius);

        if (_bottomLeft) {
            _shapeRenderer.rect(_x + _radius, _y, _width - _radius * 2f, _radius);
            _shapeRenderer.arc(_x + _radius, _y + _radius, _radius, 180f, 90f);
        } else _shapeRenderer.rect(_x, _y, _width - _radius, _radius);

        if (_bottomRight) {
            _shapeRenderer.rect(_x + _width - _radius, _y + _radius, _radius, _height - _radius * 2f);
            _shapeRenderer.arc(_x + _width - _radius, _y + _radius, _radius, 270f, 90f);
        } else _shapeRenderer.rect(_x + _width - _radius, _y, _radius, _height - _radius);
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

    public static void enableModelBlend(ModelInstance _instanceModel) {
        if (_instanceModel.materials != null && _instanceModel.materials.size > 0) {
            if (Gdx.app.getGraphics().isGL30Available())
                _instanceModel.materials.get(0).set(new BlendingAttribute(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA));
            else
                _instanceModel.materials.get(0).set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
        }
    }

    public static Model createXYZCoordinates(float _axisLength_r, float _axisLength_g, float _axisLength_b, float _capLength, float _stemThickness, int _divisions, int _primitiveType, Material _material, long _attributes) {
        ModelBuilder mb = new ModelBuilder();

        mb.begin();
        MeshPartBuilder partBuilder;

        partBuilder = mb.part("xyz", _primitiveType, _attributes, _material);
        partBuilder.setColor(GlobalAssets.DARK_COLOR_RED);
        partBuilder.arrow(0, 0, 0, _axisLength_r, 0, 0, _capLength, _stemThickness, _divisions);
        partBuilder.setColor(GlobalAssets.DARK_COLOR_GREEN);
        partBuilder.arrow(0, 0, 0, 0, _axisLength_g, 0, _capLength, _stemThickness, _divisions);
        partBuilder.setColor(GlobalAssets.DARK_COLOR_BLUE);
        partBuilder.arrow(0, 0, 0, 0, 0, _axisLength_b, _capLength, _stemThickness, _divisions);

        return mb.end();
    }

    public static void setCursor(int _cursor) {
        if (GlobalVariables.isDesktop && ServoMotionsControlPanel.superDuperJFrame != null) ServoMotionsControlPanel.superDuperJFrame.setCursor(new Cursor(_cursor));
    }
}
