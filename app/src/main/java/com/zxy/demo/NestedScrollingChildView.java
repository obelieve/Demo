package com.zxy.demo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.zxy.utility.LogUtil;

/**
 * Created by zxy on 2018/9/19 16:30.
 */

public class NestedScrollingChildView extends LinearLayout implements NestedScrollingChild
{
    private NestedScrollingChildHelper mHelper;

    public NestedScrollingChildView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        mHelper = new NestedScrollingChildHelper(this);
    }


    @Override
    public void setNestedScrollingEnabled(boolean enabled)
    {
        mHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled()
    {
        return mHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes)
    {
        return mHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll()
    {
        mHelper.stopNestedScroll();
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow)
    {
        return mHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow)
    {
        return mHelper.dispatchNestedScroll(dxConsumed,dyConsumed,dxUnconsumed,dyUnconsumed,offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY)
    {
        return mHelper.dispatchNestedPreFling(velocityX,velocityY);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed)
    {
        return mHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        LogUtil.e(MotionEventUtil.name(event));
        return super.onTouchEvent(event);
//        return true;
    }
}
