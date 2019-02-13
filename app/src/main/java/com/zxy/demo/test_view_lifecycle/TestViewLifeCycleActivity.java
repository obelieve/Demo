package com.zxy.demo.test_view_lifecycle;

import android.os.Bundle;

import com.zxy.demo.R;
import com.zxy.utility.LogUtil;

public class TestViewLifeCycleActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        LogUtil.e();
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void init(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_test_view_life_cycle);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        LogUtil.e();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        LogUtil.e();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        LogUtil.e();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        LogUtil.e();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        LogUtil.e();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        LogUtil.e();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtil.e();
    }
}
