package com.github.obelieve.plugin;

import android.app.Application;

import com.zxy.admodule.ADInitUtil;

/**
 * Created by Admin
 * on 2020/9/17
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            ADInitUtil.init(this, Class.forName("com.zxy.demo.MainActivity"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
