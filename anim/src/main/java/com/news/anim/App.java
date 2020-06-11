package com.news.anim;

import android.app.Application;

import com.zxy.frame.utils.ToastUtil;
import com.zxy.utility.SystemUtil;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SystemUtil.init(this);
        ToastUtil.init(this);
    }

}
