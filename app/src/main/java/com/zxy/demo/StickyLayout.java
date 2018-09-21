package com.zxy.demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.zxy.utility.LogUtil;

/**
 * Created by zxy on 2018/9/21 11:40.
 */

public class StickyLayout extends LinearLayout implements NestedScrollingParent
{
    private int mTopHeight;

    public StickyLayout(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        if (getChildCount() != 2)
        {
            throw new RuntimeException("only child view count is 2 !");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mTopHeight = getChildAt(0).getMeasuredHeight();
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes)
    {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes)
    {

    }

    @Override
    public void onStopNestedScroll(@NonNull View target)
    {

    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed)
    {

    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed)
    {

        if (dy > 0 && getScrollY() < mTopHeight)
        {
            if (dy > mTopHeight)
            {
                dy = mTopHeight;
            }
            LogUtil.e("scroll " + "dx:" + dx + " dy:" + dy);
            scrollBy(0, dy);
            consumed[1] = dy;
            return;
        }
        if (dy < 0 && getScrollY() > 0)
        {
            if (dy < -mTopHeight)
            {
                dy = -mTopHeight;
            }
            LogUtil.e("scroll " + "dx:" + dx + " dy:" + dy);
            scrollBy(0, dy);
            consumed[1] = dy;
            return;
        }

    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed)
    {
        return true;
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY)
    {
        return true;
    }

    @Override
    public int getNestedScrollAxes()
    {
        return 0;
    }
}
