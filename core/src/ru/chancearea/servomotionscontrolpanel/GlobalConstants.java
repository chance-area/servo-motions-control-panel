package ru.chancearea.servomotionscontrolpanel;

public abstract class GlobalConstants {
    /** On/Off debug mode */
    public static final boolean IS_DEBUG_MODE = true;

    /** Log tags */
    public static final String LOG_TAG_GL           = "GL";
    public static final String LOG_TAG_JAVA         = "JAVA";
    public static final String LOG_TAG_RAM          = "RAM";
    public static final String LOG_TAG_FATAL_ERROR  = "FATAL_ERROR";
    public static final String LOG_TAG_ERROR        = "ERROR";
    public static final String LOG_TAG_RESIZE_EVENT = "RESIZE_EVENT";

    /** App params **/
    public static final String APP_TITLE     = "Панель управления серводвижениями";
    public static final String APP_TITLE_ENG = "Servo-motions control panel";
    public static final String APP_VERSION   = "0.1 beta";

    /** Frames per second limit */
    public static final int FPS_LIMIT = 90;

    /** Save/Load */
    public static final String USER_PREFERENCES_NAME        = "user_pref.controlpanel";
    public static final String SAVE_EXCEL_FILES_FOLDER_NAME = "_excel-graphs_";
    public static final String SAVE_SCREENSHOTS_FOLDER_NAME = "_screenshots_";

    /** Save/Load -> Key names */
    public static final String KEY_PLATFORM_LENGTH          = "platform_length";
    public static final String KEY_PLATFORM_WIDTH           = "platform_width";
    public static final String KEY_PLATFORM_HEIGHT          = "platform_height";
    public static final String KEY_RADIUS_WHEEL             = "radius_wheel";
    public static final String KEY_DISTANCE_BETWEEN_MOTORS  = "distance_between_motors";
    public static final String KEY_MAX_LENGTH_UNWINDING     = "max_length_thread_unwinding";
    public static final String KEY_LOCAL_EPS32_IP           = "local_esp32_ip";
    public static final String KEY_SELECTED_TAB_ID_MAIN     = "selected_tab_id_main";
    public static final String KEY_SELECTED_TAB_ID_SETTINGS = "selected_tab_id_settings";

    /** For FreeTypeFont **/
    public static final String FONT_CHARS = "абвгдежзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
}
