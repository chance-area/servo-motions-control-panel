package ru.chancearea.servomotionscontrolpanel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;

import java.util.ArrayList;

public abstract class GlobalAssets {
    /** Colors dark theme **/
    public static final Color DARK_COLOR_BG            = new Color(22f / 255f, 23f / 255f, 27f / 255f, 1f);

    public static final Color DARK_COLOR_TABS           = new Color(29f / 255f, 30f / 255f, 33f / 255f, 1f);
    public static final Color DARK_COLOR_TAB_HOVER      = new Color(17f / 255f, 18f / 255f, 19f / 255f, 1f);
    public static final Color DARK_COLOR_TAB_SELECTED   = new Color(70f / 255f, 106f / 255f, 146f / 255f, 1f);
    public static final Color DARK_COLOR_TABBED_PANEL_1 = new Color(32f / 255f, 33f / 255f, 37f / 255f, 1f);
    public static final Color DARK_COLOR_TABBED_PANEL_2 = new Color(29f / 255f, 30f / 255f, 33f / 255f, 1f);
    public static final Color DARK_COLOR_TABBED_TEXT    = new Color(129f / 255f, 130f / 255f, 133f / 255f, 1f);

    public static final Color DARK_COLOR_SPINNER        = new Color(70f / 255f, 73f / 255f, 75f / 255f, 1f);
    public static final Color DARK_COLOR_SPINNER_BORDER = new Color(91f / 255f, 92f / 255f, 95f / 255f, 1f);
    public static final Color DARK_COLOR_SPINNER_BUTTON = new Color(65f / 255f, 68f / 255f, 70f / 255f, 1f);

    public static final Color DARK_COLOR_BUTTON               = new Color(22f / 255f, 23f / 255f, 27f / 255f, 1f);
    public static final Color DARK_COLOR_BUTTON_BORDER        = new Color(79f / 255f, 79f / 255f, 81f / 255f, 1f);
    public static final Color DARK_COLOR_BUTTON_HOVER         = new Color(29f / 255f, 30f / 255f, 35f / 255f, 1f);
    public static final Color DARK_COLOR_BUTTON_HOVER_BORDER  = new Color(58f / 255f, 90f / 255f, 127f / 255f, 1f);
    public static final Color DARK_COLOR_BUTTON_TOUCHED       = new Color(36f / 255f, 37f / 255f, 44f / 255f, 1f);

    public static final Color DARK_COLOR_GRID_3D = new Color(129f / 255f, 130f / 255f, 133f / 255f, 0.37f);
    public static final Color DARK_COLOR_GRID_2D = new Color(129f / 255f, 130f / 255f, 133f / 255f, 1f);

    public static final Color DARK_COLOR_WHITE         = new Color(187f / 255f, 187f / 255f, 187f / 255f, 1f);
    public static final Color DARK_COLOR_RED           = new Color(1f, 70f / 255f, 61f / 255f, 1f);
    public static final Color DARK_COLOR_GREEN         = new Color(1f / 255f, 176f / 255f, 117f / 255f, 1f);
    public static final Color DARK_COLOR_ORANGE        = new Color(244f / 255f, 88f / 255f, 39f / 255f, 0.9f);
    public static final Color DARK_COLOR_BLUE          = new Color(90f / 255f, 160f / 255f, 253f / 255f, 1f);
    public static final Color DARK_COLOR_PINK          = new Color(1f / 255f, 101f / 255f, 148f / 255f, 1f);
    public static final Color DARK_COLOR_YELLOW        = new Color(254f / 255f, 231f / 255f, 115f / 255f, 1f);

    public static AssetManager assetManager;

    public static float      FONT_SIZE_KOF   = 1f;
    public static BitmapFont FONT_MAIN_TEXT  = new BitmapFont(); // small font
    public static BitmapFont FONT_TITLE_TEXT = new BitmapFont(); // big font

    public enum Textures {
        TEXTURE_INFO_ICON
    }

    public enum Models {
        // ...
    }

    public static final ArrayList<AssetDescriptor<Texture>> arrAllTextures = new ArrayList<>();
    public static final ArrayList<AssetDescriptor<Model>>   arrAll3dModels = new ArrayList<>();

    public static void init() {
        assetManager = new AssetManager();

        //FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/gotham_medium.otf"));
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/verdana/verdana.ttf"));
        FreeTypeFontParameter parameter     = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.characters  = GlobalConstants.FONT_CHARS;
        parameter.borderWidth = 0.3f;
        parameter.borderColor = Color.WHITE;
        parameter.color       = Color.WHITE;
        parameter.spaceX      = 2; // Use 3 for 'gotham_medium.otf'
        parameter.genMipMaps  = true;
        parameter.minFilter   = Texture.TextureFilter.MipMap; // or Linear
        parameter.magFilter   = Texture.TextureFilter.MipMapLinearNearest; // or Linear / MipMapNearest
        parameter.incremental = false;

        // ### Main texts ###
        parameter.size = (GlobalVariables.isDesktop ? 20 : 27);
        FONT_MAIN_TEXT = fontGenerator.generateFont(parameter);
        FONT_MAIN_TEXT.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        FONT_MAIN_TEXT.setUseIntegerPositions(false);

        // ### Title texts ###
        parameter.size  = (int) (parameter.size * 1.5f);
        FONT_TITLE_TEXT = fontGenerator.generateFont(parameter);
        FONT_TITLE_TEXT.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        FONT_TITLE_TEXT.setUseIntegerPositions(false);

        fontGenerator.dispose();

        initVisUI();
    }

    private static void initVisUI() {
        Skin skin = new Skin();
        skin.add("small-font", FONT_MAIN_TEXT, BitmapFont.class);
        skin.add("default-font", FONT_TITLE_TEXT, BitmapFont.class);

        skin.addRegions(new TextureAtlas(Gdx.files.internal(("skin/uiskin.atlas"))));
        skin.load(Gdx.files.internal("skin/uiskin.json"));

        VisUI.load(skin);
    }

    public static void loadAllRes() {
        String folderPngJpg   = "png";
        String folder3dModels = "3d_models";

        String[] arrPathPngFiles = {
                "info_icon"
        };

        String[] arrPath3dModelFiles = {
                // ...
        };

        for (int i = 0; i < Textures.values().length; i++) {
            AssetDescriptor<Texture> newTexture = new AssetDescriptor<>(Gdx.files.internal(folderPngJpg + "/" + arrPathPngFiles[i] + ".png"), Texture.class);
            arrAllTextures.add(newTexture);
        }
        for (int i = 0; i < Models.values().length; i++) {
            AssetDescriptor<Model> newModel = new AssetDescriptor<>(Gdx.files.internal(folder3dModels + "/" + arrPath3dModelFiles[i] + ".g3db"), Model.class);
            arrAll3dModels.add(newModel);
        }

        startLoading();
    }

    private static void startLoading() {
        TextureLoader.TextureParameter textureParam = new TextureLoader.TextureParameter();
        textureParam.genMipMaps = true;
        textureParam.minFilter = Texture.TextureFilter.MipMap;
        textureParam.magFilter = Texture.TextureFilter.Nearest;
        textureParam.wrapU     = Texture.TextureWrap.ClampToEdge;
        textureParam.wrapV     = Texture.TextureWrap.ClampToEdge;

        for (AssetDescriptor<Texture> texture : arrAllTextures) {
            if (!assetManager.isLoaded(texture)) assetManager.load(texture.fileName, Texture.class, textureParam);
        }
        for (AssetDescriptor<Model> model : arrAll3dModels) {
            if (!assetManager.isLoaded(model)) assetManager.load(model);
        }

        assetManager.finishLoading();
    }

    public static Texture getTexture(Textures _textureEnum) {
        return assetManager.get(arrAllTextures.get(_textureEnum.ordinal()));
    }
    public static Model get3dModel(Models _modelEnum) {
        return assetManager.get(arrAll3dModels.get(_modelEnum.ordinal()));
    }

    public synchronized static void dispose() {
        FONT_MAIN_TEXT.dispose();
        FONT_TITLE_TEXT.dispose();
        assetManager.dispose();

        VisUI.dispose(false);
    }
}
