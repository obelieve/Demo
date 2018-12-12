package com.zxy.im.application;

import android.support.multidex.MultiDexApplication;

import io.rong.imkit.RongIM;

/**
 * Created by zxy on 2018/12/12 10:06.
 */

public class App extends MultiDexApplication
{

    @Override
    public void onCreate()
    {
        super.onCreate();
        RongIM.init(this);
    }
}
