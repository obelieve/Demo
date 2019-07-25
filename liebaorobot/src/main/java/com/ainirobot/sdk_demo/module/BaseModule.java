package com.ainirobot.sdk_demo.module;

import android.util.Log;

public class BaseModule {
    private final String TAG = getClass().getSimpleName();

    /**
     * 生命周期 开始
     */
    public void start(String params) {
        Log.d(TAG, "start");

    }
    /**
     * 生命周期 更新数据
     */
    public void update(String params){
        Log.d(TAG, "update " +params);
    }
    /**
     * 生命周期 结束
     */
    public void stop(){
        Log.d(TAG, "stop");

    }
}
