package com.smile.studio.libsmilestudio.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
    public static final String name = "smile_studio";

    public static void clear(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        preferences.edit().remove(key).commit();
    }

    public static void clearAll(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(name, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key, String defaultVaule) {
        String result = null;
        SharedPreferences settings = context.getSharedPreferences(name, 0);
        result = settings.getString(key, defaultVaule);
        return result;
    }

    public static void putLong(Context context, String key, long value) {
        SharedPreferences settings = context.getSharedPreferences(name, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLong(Context context, String key) {
        long result;
        SharedPreferences settings = context.getSharedPreferences(name, 0);
        result = settings.getLong(key, 0);
        return result;
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(name, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(Context context, String key) {
        int result;
        SharedPreferences settings = context.getSharedPreferences(name, 0);
        result = settings.getInt(key, 0);
        return result;
    }

    public static void putBoolean(Context context, String key, Boolean value) {
        SharedPreferences settings = context.getSharedPreferences(name, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static Boolean getBoolean(Context context, String key) {
        Boolean result;
        SharedPreferences settings = context.getSharedPreferences(name, 0);
        result = settings.getBoolean(key, false);
        return result;
    }

    public static int getInt(Context context, String key, int defaultValue) {
        int result;
        SharedPreferences settings = context.getSharedPreferences(name, 0);
        result = settings.getInt(key, defaultValue);
        return result;
    }
}
