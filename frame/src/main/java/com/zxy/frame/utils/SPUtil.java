package com.zxy.frame.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/11/9.
 */

public class SPUtil {

    private SharedPreferences mSharedPreferences;
    private static Context mContext;
    private static String mSPName;
    private static SPUtil sSPUtil;

    private SPUtil() {
        if (mContext == null) {
            throw new IllegalArgumentException("SPUtil uninitialized init(Context) !");
        }
        mSharedPreferences = mContext.getSharedPreferences(mSPName, Context.MODE_PRIVATE);
    }

    public static void init(Context context, String spFileName) {
        mContext = context;
        mSPName = spFileName;
    }

    public static SPUtil getInstance() {
        if (sSPUtil == null) {
            synchronized (SPUtil.class) {
                if (sSPUtil == null) {
                    sSPUtil = new SPUtil();
                }
            }
        }
        return sSPUtil;
    }
    
    public String getString(String key) {
        return mSharedPreferences.getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    /**
     * 设置配置参数的值
     *

     * @param key
     * @param value
     */
    public void putString(String key, String value) {


        mSharedPreferences.edit().putString(key, value).apply();
    }

    public boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }


    public float getFloat(String key, float defValue) {
        return mSharedPreferences.getFloat(key, defValue);
    }

    public void putFloat(String key, float value) {
        mSharedPreferences.edit().putFloat(key, value).apply();
    }

    public long getLong(String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }

    public void putLong(String key, long value) {
        mSharedPreferences.edit().putLong(key, value).apply();
    }

    public int getInteger(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public void putInteger(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }
}
