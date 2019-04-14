package com.zxy.app_new_process;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zxy.demo.aidl.IPersonManager;
import com.zxy.demo.aidl.Person;
import com.zxy.utility.LogUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    IPersonManager mIPersonManager;//进程间通信获取数据
    boolean threadInterrupt;//线程退出标识

    Handler mHandler = new Handler();

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIPersonManager = IPersonManager.Stub.asInterface(service);
            LogUtil.e();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.e();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!threadInterrupt) {
                    if (mIPersonManager != null) {
                        Person person = new Person();
                        person.setName("name:" + SystemClock.elapsedRealtime());
                        person.setGender(SystemClock.elapsedRealtime() % 2 == 0 ? 1 : 0);
                        LogUtil.i("time:" + SystemClock.elapsedRealtime() + "插入数据： " + person);
                        try {
                            mIPersonManager.addPerson(person);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mIPersonManager != null) {
                    try {
                        List<Person> list = mIPersonManager.getPersonList();
                        LogUtil.e("time:" + SystemClock.elapsedRealtime() + " 获取通信进程数据：size=" + list.size() +" "+ list);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                mHandler.postDelayed(this, 5000);
            }
        }, 5000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.e();
        configBindService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.e();
        threadInterrupt = true;
        configUnBindService();
    }

    private void configBindService() {
        Intent intent = new Intent();
        intent.setAction("com.zxy.demo.ExportedService");
        intent.setPackage("com.zxy.demo");//Android 5.0以后，必须明确packageName
        LogUtil.e("" + bindService(intent, mServiceConnection, BIND_AUTO_CREATE));
    }

    private void configUnBindService() {
        unbindService(mServiceConnection);
    }

    private void configStartService() {
        Intent intent = new Intent();
        intent.setAction("com.zxy.demo.ExportedService");
        intent.setPackage("com.zxy.demo");//Android 5.0以后，必须明确packageName
        startService(intent);
    }

    private void configStopService() {
        Intent intent = new Intent();
        intent.setAction("com.zxy.demo.ExportedService");
        intent.setPackage("com.zxy.demo");//Android 5.0以后，必须明确packageName
        stopService(intent);
    }
}
