package ru.chancearea.servomotionscontrolpanel;

import com.badlogic.gdx.Preferences;

public abstract class GlobalVariables {
    /** Window sizes */
    public static float windowWidth  = 1280f;
    public static float windowHeight = 720f;

    /** This is Desktop?! */
    public static boolean isDesktop = true;

    /** User variables */
    public static Preferences userPref           = null;
    public static float radiusWheel              = -1;
    public static float distanceBetweenMotors    = -1;
    public static float maxLengthThreadUnwinding = -1;
    public static String localESP32IP            = "none";
}
