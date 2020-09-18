package com.news.project.plugin;

import android.app.Application;

/**
 * Created by Admin
 * on 2020/9/17
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ADInitUtil.init(this, "com.news.project","com.qian.news.main.MainActivity");
    }
}
