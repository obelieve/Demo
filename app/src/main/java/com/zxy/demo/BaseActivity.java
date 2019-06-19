package com.zxy.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.zxy.utility.LogUtil;

public class BaseActivity extends FragmentActivity {

    /**
     * savedInstanceState如果需要恢复，需要非空判断
     * @param savedInstanceState
     */
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

    /**
     * 这个只会在savedInstanceState存在并且re-initialized时，才会调用。
     * Activity子类进行implementation时，可以决定是否调用父类super.onRestoreInstanceState(savedInstanceState)。
     * 而子类Activity中onCreate(savedInstanceState)必须调用父类的super.onCreate(savedInstanceState)。
     * @param savedInstanceState
     */
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

    /**
     * Activity可能被系统杀死时才会被调用(1.自己执行finish()不会调用；2.Activity前台转到后台时会调用；3.屏幕横竖屏切换会调用；)
     * @param outState
     */
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
