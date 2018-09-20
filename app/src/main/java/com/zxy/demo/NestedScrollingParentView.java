package com.zxy.demo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.support.v4.view.NestedScrollingParent;

import com.zxy.utility.LogUtil;

/**
 * Created by zxy on 2018/9/19 15:42.
 */

public class NestedScrollingParentView extends LinearLayout implements NestedScrollingParent
{
    private NestedScrollingParentHelper mHelper;

    public NestedScrollingParentView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        setOrientation(VERTICAL);
        mHelper = new NestedScrollingParentHelper(this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        boolean b = super.onInterceptTouchEvent(ev);
        LogUtil.e(MotionEventUtil.name(ev)+"   "+b);
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        LogUtil.e(MotionEventUtil.name(event));
        return super.onTouchEvent(event);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes)
    {
        mHelper.onNestedScrollAccepted(child,target,axes);
    }

    @Override
    public void onStopNestedScroll(View child)
    {
        mHelper.onStopNestedScroll(child);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed)
    {
        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes)
    {
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY)
    {
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed)
    {
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }
}
