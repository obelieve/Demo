package com.zxy.demo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zxy.utility.LogUtil;

public class ExportedService extends Service {

    public static final String START_ACTION = "com.zxy.demo.ExportedService";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.e(getExportedString("onCreate()"));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e(getExportedString("onStartCommand()"));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e(getExportedString("onDestroy()"));
    }

    public String getExportedString(String msg) {
        return "进程："+getApplicationContext().getApplicationInfo().processName+" service->"+msg;
    }
}
