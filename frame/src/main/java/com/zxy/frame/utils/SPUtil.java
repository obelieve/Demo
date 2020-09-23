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

    public SharedPreferences getSP() {
        return mSharedPreferences;
    }
}
