package com.zxy.demo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.zxy.demo.aidl.IPersonManager;
import com.zxy.demo.aidl.Person;
import com.zxy.utility.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExportedService extends Service {

    public static final String START_ACTION = "com.zxy.demo.ExportedService";

    List<Person> mPersonList = new CopyOnWriteArrayList<>();//线程安全的Arraylist，支持并发操作
    //Collections.synchronizedList和CopyOnWriteArrayList 性能差异


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.e(getExportedString("onBind()"));
        return new IPersonManager.Stub() {
            @Override
            public List<Person> getPersonList() throws RemoteException {
                LogUtil.e("Service getPersonList() Thread: " + Thread.currentThread().getName());
                return mPersonList;
            }

            @Override
            public boolean addPerson(Person per) throws RemoteException {
                if (per != null) {
                    mPersonList.add(per);
                    LogUtil.e("Service getPersonList() Thread: " + Thread.currentThread().getName());
                    return true;
                }
                return false;
            }
        };
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
    public boolean onUnbind(Intent intent) {
        LogUtil.e(getExportedString("onUnbind()"));
        return super.onUnbind(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e(getExportedString("onDestroy()"));
    }

    public String getExportedString(String msg) {
        return "进程：" + getApplicationContext().getApplicationInfo().processName + " service->" + msg;
    }
}
