-optimizations !code/allocation/variable
-verbose
-dontobfuscate

-dontwarn android.support.**
-dontwarn com.badlogic.gdx.backends.android.AndroidFragmentApplication
-dontwarn com.badlogic.gdx.utils.GdxBuild
-dontwarn com.badlogic.gdx.jnigen.BuildTarget*
-dontwarn com.badlogic.gdx.graphics.g2d.freetype.FreetypeBuild
-dontwarn org.slf4j.impl.StaticLoggerBinder

-keepclassmembers class com.badlogic.gdx.backends.android.AndroidInput* {
   <init>(com.badlogic.gdx.Application, android.content.Context, java.lang.Object, com.badlogic.gdx.backends.android.AndroidApplicationConfiguration);
}

# Kryo
-dontwarn sun.reflect.**
-dontwarn java.beans.**
-dontwarn sun.nio.ch.**
-dontwarn sun.misc.**

-keepattributes !LocalVariable*,**

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,includedescriptorclasses class * {
    native <methods>;
}

-keep class com.esotericsoftware.kryo.** {*;}
-keep class com.esotericsoftware.** {*;}

-keep class java.beans.** { *; }
-keep class sun.reflect.** { *; }
-keep class sun.nio.ch.** { *; }