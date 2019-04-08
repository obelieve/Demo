package com.zxy.view_lifestyle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.zxy.utility.LogUtil;

/**
 * Created by admin on 2019/4/7.
 */

public class LifecycleBaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.e();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.e();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtil.e();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtil.e();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.e();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e();
    }
}
