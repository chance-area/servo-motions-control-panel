package ru.chancearea.servomotionscontrolpanel;

import com.badlogic.gdx.graphics.Color;

public abstract class GlobalConstants {
    /** On/Off debug mode */
    public static final boolean IS_DEBUG_MODE = true;

    /** Log tags */
    public static final String LOG_TAG_GL_VERSION       = "[GL] Version";
    public static final String LOG_TAG_JAVA_VERSION     = "[JAVA] Version";
    public static final String LOG_TAG_TEXTURE_MAX_SIZE = "[GL] Max texture size";
    public static final String LOG_TAG_FATAL_ERROR      = "FATAL_ERROR";
    public static final String LOG_TAG_ERROR            = "ERROR";

    /** App params **/
    public static final String APP_TITLE     = "Панель управления серводвижениями";
    public static final String APP_TITLE_ENG = "Servo-motions control panel";
    public static final String APP_VERSION   = "0.1 beta";

    /** Frames per second limit */
    public static final int FPS_LIMIT = 60;

    /** Save/Load */
    public static final String SAVE_FILE_NAME               = "save.controlpanel";
    public static final String SAVE_FILE_NAME_BACKUP        = "save.controlpanel_backup";
    public static final String SAVE_FOLDER_NAME             = "_saves_";
    public static final String SAVE_EXCEL_FILES_FOLDER_NAME = "_excel-graphs_";
    public static final String SAVE_SCREENSHOTS_FOLDER_NAME = "_screenshots_";

    /** For FreeTypeFont **/
    public static final String FONT_CHARS = "абвгдежзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";

    /** Colors dark theme **/
    public static final Color THEME_COLOR_BG            = new Color(22f / 255f, 23f / 255f, 27f / 255f, 1f);
    public static final Color THEME_COLOR_PANELS        = new Color(29f / 255f, 30f / 255f, 33f / 255f, 1f);
    public static final Color THEME_COLOR_TABBED_PANELS = new Color(32f / 255f, 33f / 255f, 37f / 255f, 1f);
    public static final Color THEME_COLOR_TEXTS         = new Color(82f / 255f, 83f / 255f, 87f / 255f, 1f);
    public static final Color THEME_COLOR_MOVERS        = new Color(82f / 255f, 83f / 255f, 87f / 255f, 90f / 255f);
    public static final Color THEME_COLOR_TABBED_TEXTS  = new Color(129f / 255f, 130f / 255f, 133f / 255f, 1f);
    public static final Color THEME_COLOR_WHITE         = new Color(239f / 255f, 241f / 255f, 242f / 255f, 1f);
    public static final Color THEME_COLOR_RED           = new Color(1, 70f / 255f, 61f / 255f, 1f);
    public static final Color THEME_COLOR_GREEN         = new Color(1, 176f / 255f, 117f / 255f, 1f);
    public static final Color THEME_COLOR_ORANGE        = new Color(244f / 255f, 88f / 255f, 39f / 255f, 1f);
    public static final Color THEME_COLOR_BLUE          = new Color(90f / 255f, 160f / 255f, 253f / 255f, 1f);
    public static final Color THEME_COLOR_PINK          = new Color(1f, 101f / 255f, 148f / 255f, 1f);
    public static final Color THEME_COLOR_YELLOW        = new Color(254f / 255f, 231f / 255f, 115f / 255f, 1f);
}
