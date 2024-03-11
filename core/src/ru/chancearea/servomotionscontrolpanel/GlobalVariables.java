package ru.chancearea.servomotionscontrolpanel;

import com.badlogic.gdx.Preferences;

public abstract class GlobalVariables {
    /** Window sizes */
    public static float windowWidth  = 1280f;
    public static float windowHeight = 720f;

    /** This is Desktop? */
    public static boolean isDesktop = true;

    /** User variables */
    public static Preferences userPref           = null;
    public static float platformLength           = -1;
    public static float platformWidth            = -1;
    public static float platformHeight           = -1;
    public static float radiusWheel              = -1;
    public static float distanceBetweenMotors    = -1;
    public static float maxLengthThreadUnwinding = -1;
    public static String localESP32IP            = "none";
    public static int selectedTabID_main         = 0;
    public static int selectedTabID_settings     = 0;
}
