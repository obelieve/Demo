package com.zxy.mall.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.zxy.frame.base.BaseActivity;
import com.zxy.mall.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mNeedInsetStatusBar = false;
        mLightStatusBar = false;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {

    }
}
