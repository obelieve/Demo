package com.zxy.im.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.zxy.frame.http.OkHttpUtil;
import com.zxy.frame.utility.LogUtil;
import com.zxy.frame.utility.SPUtil;
import com.zxy.frame.utility.UContext;
import com.zxy.im.imsdk.APPIMContext;

/**
 * Created by zxy on 2018/12/12 10:06.
 */

public class App extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();
        UContext.init(this);
        SPUtil.init(this);
        OkHttpUtil.init().showErrorToast(true);
        LogUtil.builder().setLogTag("Demo");

        APPIMContext.init(this);
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
