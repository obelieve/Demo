package com.zxy.demo.test_view_lifecycle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import com.zxy.demo.R;
import com.zxy.utility.LogUtil;

public class TestViewLifeCycleActivity extends BaseActivity
{

    public static void start(Activity activity)
    {
        activity.startActivity(new Intent(activity,TestViewLifeCycleActivity.class));
    }

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
        ViewGroup fl_container = findViewById(R.id.fl_container);
        MyAppView appView = new MyAppView(getActivity(), null);
        ViewUtil.toggleView(fl_container, appView);
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
