package com.news.anim;

import android.app.Application;

import com.zxy.frame.utils.ToastUtil;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.init(this);
    }

}
