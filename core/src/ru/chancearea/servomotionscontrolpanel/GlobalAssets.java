package ru.chancearea.servomotionscontrolpanel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
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
    public static AssetManager assetManager;

    public static float FONT_SIZE_KOF = 1f;
    public static BitmapFont FONT_MAIN_TEXT  = new BitmapFont();
    public static BitmapFont FONT_TITLE_TEXT = new BitmapFont();

    public enum AllTextures {
        /*TEXTURE_ARROW,
        TEXTURE_BG,*/
    }

    public enum All3DModels {
        /*MODEL_PLATFORM,
        MODEL_WHITE_PATH,
        MODEL_GREEN_PATH,
        MODEL_RED_PATH,
        MODEL_BLACK_PATH,
        MODEL_GRIPPER_PATH,*/
    }

    public static final ArrayList<AssetDescriptor<Texture>> arrAllTextures = new ArrayList<>();
    public static final ArrayList<AssetDescriptor<Model>>   arrAll3dModels = new ArrayList<>();

    public static void init() {
        assetManager = new AssetManager();

        //FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/gotham_medium.otf"));
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/verdana/verdana.ttf"));
        FreeTypeFontParameter parameter     = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.characters  = GlobalConstants.FONT_CHARS;
        parameter.borderWidth = 0.6f;
        parameter.borderColor = Color.WHITE;
        parameter.color       = Color.WHITE;
        parameter.spaceX      = 2; // Use 3 for 'gotham_medium.otf'
        parameter.genMipMaps  = true;
        parameter.minFilter   = Texture.TextureFilter.Linear;
        parameter.magFilter   = Texture.TextureFilter.MipMapLinearNearest; // or Linear
        parameter.incremental = false;

        // ### Main texts ###
        parameter.size = 40; // ha-ha-ha... not cool
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
        skin.add("font", FONT_MAIN_TEXT, BitmapFont.class);
        skin.add("title", FONT_TITLE_TEXT, BitmapFont.class);

        skin.addRegions(new TextureAtlas(Gdx.files.internal(("skins/neutralizer/neutralizer-ui.atlas"))));
        skin.load(Gdx.files.internal("skins/neutralizer/neutralizer-ui.json"));

        VisUI.load(skin);
        //VisUI.load();
    }

    public static void loadAllRes() {
        String _folderPngJpg   = "png_jpg";
        String _folder3dModels = "3d_models";

        String[] arrPathPngJpg = {
                // Lol, nothing
        };

        String[] arrPath3dModels = {
                // In future...
        };

        for (int i = 0; i < AllTextures.values().length; i++) {
            AssetDescriptor<Texture> newTexture = new AssetDescriptor<>(Gdx.files.internal(_folderPngJpg + "/" + arrPathPngJpg[i] + ".png"), Texture.class);
            arrAllTextures.add(newTexture);
        }
        for (int i = 0; i < All3DModels.values().length; i++) {
            AssetDescriptor<Model> newModel = new AssetDescriptor<>(Gdx.files.internal(_folder3dModels + "/" + arrPath3dModels[i] + ".g3db"), Model.class);
            arrAll3dModels.add(newModel);
        }

        startLoading();
    }

    private static void startLoading() {
        for (AssetDescriptor<Texture> texture : arrAllTextures) {
            if (!assetManager.isLoaded(texture)) assetManager.load(texture);
        }
        for (AssetDescriptor<Model> model : arrAll3dModels) {
            if (!assetManager.isLoaded(model)) assetManager.load(model);
        }

        assetManager.finishLoading();
    }

    public static AssetDescriptor<Texture> getTextureDescriptor(int index) { return arrAllTextures.get(index); }
    public static AssetDescriptor<Model>   get3dModelDescriptor(int index) { return arrAll3dModels.get(index); }

    public synchronized static void dispose() {
        FONT_MAIN_TEXT.dispose();
        FONT_TITLE_TEXT.dispose();
        assetManager.dispose();
        VisUI.dispose(false);
    }
}
