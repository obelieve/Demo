package com.qiniu.droid.niuplayer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;


public class ScrollEnableViewPager extends ViewPager {
    private boolean mScrollEnable = true;

    public ScrollEnableViewPager(Context context) {
        super(context);
    }

    public ScrollEnableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollEnable(boolean scrollEnable) {
        mScrollEnable = scrollEnable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mScrollEnable && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mScrollEnable) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }
}