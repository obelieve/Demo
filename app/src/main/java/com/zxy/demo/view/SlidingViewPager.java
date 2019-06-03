package com.zxy.demo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 限制滑动ViewPager
 */
public class SlidingViewPager extends ViewPager {

    private boolean mIsSliding = false;

    public void setSliding(boolean sliding) {
        mIsSliding = sliding;
    }

    public boolean isSliding() {
        return mIsSliding;
    }

    public SlidingViewPager(@NonNull Context context) {
        super(context);
    }

    public SlidingViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mIsSliding;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mIsSliding;
    }
}
