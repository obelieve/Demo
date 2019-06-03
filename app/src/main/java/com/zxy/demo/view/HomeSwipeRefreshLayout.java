package com.zxy.demo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zxy.utility.LogUtil;
import com.zxy.utility.MotionEventUtil;

public class HomeSwipeRefreshLayout extends SwipeRefreshLayout {
    public HomeSwipeRefreshLayout(@NonNull Context context) {
        this(context, null);
    }

    public HomeSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        float density = context.getResources().getDisplayMetrics().density;
        setProgressViewOffset(false, 0, (int) (density * 48));//消除撤销刷新和tabLayout重叠阴影
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtil.e();
        boolean b = super.dispatchTouchEvent(ev);
        LogUtil.e("dispatchTouchEvent=" + b+" "+ MotionEventUtil.name(ev));
        return b;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogUtil.e();
        boolean b = super.onInterceptTouchEvent(ev);
        LogUtil.e("onInterceptTouchEvent=" + b+" "+ MotionEventUtil.name(ev));
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        LogUtil.e();
        boolean b = super.onTouchEvent(ev);
        LogUtil.e("onTouchEvent=" + b+" "+ MotionEventUtil.name(ev));
        return b;
    }
}
