package com.github.obelieve.repository.cache;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by zhangzhiqiang_dian91 on 2015/11/25.
 */
public class PreferenceUtil {

    /**
     * 配置文件名称
     */
    public static final String SYSTEM_SETTING_NAME = "SYSTEM_SETTING";// SharedPreferences

    /**
     * 取配置参数的值
     *
     * @param ctx
     * @param key
     * @return
     */
    public static String getString(Context ctx, String key) {
        if (ctx == null) {
            return "";
        }
        return getSharedPreferences(ctx).getString(key, "");
    }

    public static String getString(Context ctx, String key, String defaultValue) {
        if (ctx == null) {
            return defaultValue;
        }
        return getSharedPreferences(ctx).getString(key, defaultValue);
    }

    /**
     * 设置配置参数的值
     *
     * @param ctx
     * @param key
     * @param value
     */
    public static void putString(Context ctx, String key, String value) {
        if (ctx == null) {
            return;
        }
        getSharedPreferences(ctx).edit().putString(key, value).apply();
    }

    public static boolean getBoolean(Context ctx, String key) {
        if (ctx == null) {
            return false;
        }
        return getSharedPreferences(ctx).getBoolean(key, false);
    }

    public static boolean getBoolean(Context ctx, String key, boolean defValue) {
        if (ctx == null) {
            return false;
        }
        return getSharedPreferences(ctx).getBoolean(key, defValue);
    }

    public static void putBoolean(Context ctx, String key, boolean value) {
        if (ctx == null) {
            return;
        }
        getSharedPreferences(ctx).edit().putBoolean(key, value).apply();
    }

    public static SharedPreferences getSharedPreferences(Context ctx) {
        return ctx.getSharedPreferences(SYSTEM_SETTING_NAME, AppCompatActivity.MODE_PRIVATE);
    }

    public static SharedPreferences getSharedPreferences(Context ctx, String filename) {
        return ctx.getSharedPreferences(filename, AppCompatActivity.MODE_PRIVATE);
    }


    public static float getFloat(Context ctx, String key, float defValue) {
        if (ctx == null) {
            return defValue;
        }
        return getSharedPreferences(ctx).getFloat(key, defValue);
    }

    public static void putFloat(Context ctx, String key, float value) {
        if (ctx == null) {
            return;
        }
        getSharedPreferences(ctx).edit().putFloat(key, value).apply();
    }

    public static long getLong(Context ctx, String key, long defValue) {
        if (ctx == null) {
            return defValue;
        }
        return getSharedPreferences(ctx).getLong(key, defValue);
    }

    public static void putLong(Context ctx, String key, long value) {
        if (ctx == null) {
            return;
        }
        getSharedPreferences(ctx).edit().putLong(key, value).apply();
    }

    public static int getInteger(Context ctx, String key, int defValue) {
        if (ctx == null) {
            return defValue;
        }
        return getSharedPreferences(ctx).getInt(key, defValue);
    }

    public static void putInteger(Context ctx, String key, int value) {
        if (ctx == null) {
            return;
        }
        getSharedPreferences(ctx).edit().putInt(key, value).apply();
    }
}
