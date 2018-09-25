package com.zxy.demo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.zxy.utility.LogUtil;

/**
 * Created by zxy on 2018/9/21 11:40.
 */

public class StickyLayout extends LinearLayout  implements NestedScrollingParent
{
    private int mTopHeight;
    NestedScrollingParentHelper mParentHelper;
    NestedScrollingChildHelper mChildHelper;
    ValueAnimator mValueAnimator;
    public StickyLayout(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        setOrientation(VERTICAL);
        mParentHelper = new NestedScrollingParentHelper(this);
        mChildHelper = new NestedScrollingChildHelper(this);
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
        mParentHelper.onNestedScrollAccepted(child,target,axes);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target)
    {
        mParentHelper.onStopNestedScroll(target);
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
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) target;
        RecyclerView recyclerView = (RecyclerView) swipeRefreshLayout.getChildAt(0);
        View firstChild = recyclerView.getChildAt(0);
        if (dy < 0 && getScrollY() > 0 && recyclerView.getChildAdapterPosition(firstChild) == 0)
        {
            if (dy < -getScrollY())
            {
                dy = -getScrollY();
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
        if (velocityY == 0)
            return false;
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) target;
        RecyclerView recyclerView = (RecyclerView) swipeRefreshLayout.getChildAt(0);
        View firstChild = recyclerView.getChildAt(0);
        if ((velocityY < 0 && recyclerView.getChildAdapterPosition(firstChild) <= 3) || velocityY > 0)
        {
            animalScroll(velocityY);
            return true;
        }
        return false;
    }

    public void animalScroll(float velocityY)
    {
        if (mValueAnimator == null)
        {
            mValueAnimator = new ValueAnimator();
            mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    scrollTo(0, (Integer) animation.getAnimatedValue());
                }
            });
        }
        if (mValueAnimator.isRunning())
        {
            mValueAnimator.cancel();
        }
        float distance = 0;
        if (velocityY > 0)
        {
            distance = getScrollY();
            mValueAnimator.setIntValues(getScrollY(), mTopHeight);
        } else
        {
            distance = mTopHeight - getScrollY();
            mValueAnimator.setIntValues(getScrollY(), 0);
        }
        int time = (int) (5 * (1000 * Math.abs(distance) / Math.abs(velocityY)));
        time = Math.min(time, 600);
        mValueAnimator.setDuration(time);
        mValueAnimator.start();
    }


    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY)
    {
        return false;
    }

    @Override
    public int getNestedScrollAxes()
    {
        return mParentHelper.getNestedScrollAxes();
    }
}
