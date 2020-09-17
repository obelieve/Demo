package com.github.obelieve.plugin;

import android.app.Application;

/**
 * Created by Admin
 * on 2020/9/17
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ADInitUtil.init(this, "com.zxy.demo","com.zxy.demo.MainActivity");
    }
}
