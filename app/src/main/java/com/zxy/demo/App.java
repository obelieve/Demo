package com.zxy.demo;

import android.app.Application;

import com.zxy.admodule.ADInitUtil;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ADInitUtil.init(this,MainActivity.class);
    }


}
