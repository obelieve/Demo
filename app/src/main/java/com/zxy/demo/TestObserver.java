package com.zxy.demo;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.zxy.utility.LogUtil;

/**
 * Created by Admin
 * on 2020/11/3
 */
public class TestObserver implements DefaultLifecycleObserver {

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        LogUtil.e();
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        LogUtil.e();
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        LogUtil.e();
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        LogUtil.e();
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        LogUtil.e();
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        LogUtil.e();
    }
}
