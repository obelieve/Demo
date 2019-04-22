package com.zxy.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.zxy.utility.LogUtil;

public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtil.e();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        LogUtil.e();
        super.onNewIntent(intent);
    }

    @Override
    protected void onRestart() {
        LogUtil.e();
        super.onRestart();
    }

    @Override
    protected void onStart() {
        LogUtil.e();
        super.onStart();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        LogUtil.e();
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        LogUtil.e();
        super.onResume();
    }

    @Override
    protected void onPause() {
        LogUtil.e();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        LogUtil.e();
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        LogUtil.e();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LogUtil.e();
        super.onDestroy();
    }
}
