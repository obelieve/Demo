package com.zxy.app_new_process;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configStartService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        configStopService();
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
