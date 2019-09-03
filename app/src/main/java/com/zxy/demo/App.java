package com.zxy.demo;

import android.app.Application;

import com.zxy.demo.utils.RouteMapUtil;

/**
 * Created by zxy on 2019/08/30.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RouteMapUtil.init(getApplicationContext());
    }
}
