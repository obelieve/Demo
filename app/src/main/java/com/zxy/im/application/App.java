package com.zxy.im.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.zxy.frame.http.OkHttpUtil;
import com.zxy.frame.utility.SPUtil;

import java.util.concurrent.TimeUnit;

import io.rong.imkit.RongIM;

/**
 * Created by zxy on 2018/12/12 10:06.
 */

public class App extends Application
{
    private static Context mAppContext;
    @Override
    public void onCreate()
    {
        super.onCreate();
        mAppContext = this;
        RongIM.init(this);
        OkHttpUtil.init().connectTimeout(10, TimeUnit.SECONDS);
        SPUtil.init(this,"im");
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getAppContext()
    {
        return mAppContext;
    }
}
