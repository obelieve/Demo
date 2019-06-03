package com.zxy.demo.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zxy.utility.LogUtil;
import com.zxy.utility.MotionEventUtil;

public class MCoordinatorLayout extends CoordinatorLayout {
    public MCoordinatorLayout(Context context) {
        super(context);
    }

    public MCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.e();
        boolean b = super.onTouchEvent(event);
        LogUtil.e("onTouchEvent = "+b+" "+ MotionEventUtil.name(event));
        return b;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogUtil.e();
        boolean b = super.onInterceptTouchEvent(ev);
        LogUtil.e("onInterceptTouchEvent = "+b+" "+ MotionEventUtil.name(ev));
        return b;
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtil.e();
        boolean b = super.dispatchTouchEvent(ev);
        LogUtil.e("dispatchTouchEvent = "+b+" "+ MotionEventUtil.name(ev));
        return b;
    }
}
