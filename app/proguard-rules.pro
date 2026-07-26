# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontobfuscate
-keep class org.libsdl3.app.** { *; }
-keepclassmembers class org.libsdl3.app.SDLActivity {
    public static <methods>;
}
-keep class com.opentouchgaming.saffal.** { *; }
-keepclassmembers class com.opentouchgaming.saffal.** {
    public <methods>;
    public static <methods>;
}
-keep class com.mobilerpgpack.phone.translator.** { *; }
-keepclassmembers class com.mobilerpgpack.phone.translator.TranslationManager {
    public <methods>;
    public static <methods>;
}

-dontwarn java.awt.**
-keep class com.sun.jna.* { *; }
-keep class * extends com.sun.jna.* { *; }
-keepclassmembers class * extends com.sun.jna.* { public *; }

-dontwarn org.fmod.**
-keep class org.fmod.* { *; }
-keep class * extends org.fmod.* { *; }
-keepclassmembers class * extends org.fmod.* { public *; public static *;}

-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.