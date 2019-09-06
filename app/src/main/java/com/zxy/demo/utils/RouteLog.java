package com.zxy.demo.utils;

import android.util.Log;

import com.zxy.demo.BuildConfig;


/**
 * Created by zxy on 2019/09/06.
 */
public class RouteLog {
    public static void log(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }
}
