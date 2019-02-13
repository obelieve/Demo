package com.zxy.demo.test_view_lifecycle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.zxy.demo.R;
import com.zxy.utility.LogUtil;

/**
 * Created by zxy on 2019/2/13 16:55.
 */

public class MyAppView extends BaseAppView
{
    public MyAppView(Context context)
    {
        this(context, null);
        LogUtil.e();
    }

    public MyAppView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setContentView(R.layout.view_my_app);
        LogUtil.e();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState)
    {
        super.onActivityCreated(activity, savedInstanceState);
        LogUtil.e();
    }

    @Override
    public void onActivityStarted(Activity activity)
    {
        super.onActivityStarted(activity);
        LogUtil.e();
    }

    @Override
    public void onActivityResumed(Activity activity)
    {
        super.onActivityResumed(activity);
        LogUtil.e();
    }

    @Override
    public void onActivityPaused(Activity activity)
    {
        super.onActivityPaused(activity);
        LogUtil.e();
    }

    @Override
    public void onActivityStopped(Activity activity)
    {
        super.onActivityStopped(activity);
        LogUtil.e();
    }

    @Override
    public void onActivityDestroyed(Activity activity)
    {
        super.onActivityDestroyed(activity);
        LogUtil.e();
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState)
    {
        super.onActivitySaveInstanceState(activity, outState);
        LogUtil.e();
    }

    @Override
    public void onActivityRestoreInstanceState(Activity activity, Bundle savedInstanceState)
    {
        super.onActivityRestoreInstanceState(activity, savedInstanceState);
        LogUtil.e();
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        LogUtil.e();
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        LogUtil.e();
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(activity, requestCode, resultCode, data);
        LogUtil.e();
    }

    @Override
    public boolean dispatchTouchEvent(Activity activity, MotionEvent ev)
    {
        LogUtil.e();
        return super.dispatchTouchEvent(activity, ev);
    }

    @Override
    public boolean dispatchKeyEvent(Activity activity, KeyEvent event)
    {
        LogUtil.e();
        return super.dispatchKeyEvent(activity, event);
    }


}
