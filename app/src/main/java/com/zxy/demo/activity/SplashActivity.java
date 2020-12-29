package com.zxy.demo.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.zxy.demo.databinding.ActivitySplashBinding;
import com.zxy.frame.base.ApiBaseActivity2;
import com.zxy.frame.utils.StatusBarUtil;

/**
 * Created by Admin
 * on 2020/6/23
 */
public class SplashActivity extends ApiBaseActivity2<ActivitySplashBinding> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setHideStatusBarFullScreen(mActivity);
    }

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {

    }
}
