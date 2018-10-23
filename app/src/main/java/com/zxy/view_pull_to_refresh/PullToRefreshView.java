package com.zxy.view_pull_to_refresh;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zxy on 2018/10/23 15:52.
 */

public class PullToRefreshView extends ViewGroup
{
    private Mode mMode = Mode.BOTH;
    private State mState = State.RESET;

    public PullToRefreshView(Context context)
    {
        super(context);
    }

    public PullToRefreshView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int maxWidth = 0;
        int maxHeight = getPaddingTop() + getPaddingBottom();

        for (int i = 0; i < getChildCount(); i++)
        {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
            maxWidth = Math.max(maxWidth, getChildAt(i).getMeasuredWidth());
        }
        maxHeight += getChildAt(1).getMeasuredHeight();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthMode)
        {
            case MeasureSpec.EXACTLY:
                maxWidth = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        switch (heightMode)
        {
            case MeasureSpec.EXACTLY:
                maxHeight = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        setMeasuredDimension(maxWidth, maxHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        l = getPaddingLeft();
        r = getPaddingRight();
        t = getPaddingTop();
        b = getPaddingBottom();

        View headerView = getChildAt(0);
        View contentView = getChildAt(1);
        View footerView = getChildAt(2);

        headerView.layout(l, t - headerView.getMeasuredHeight(), r + headerView.getMeasuredWidth(), t);
        contentView.layout(l, t, r + contentView.getMeasuredWidth(), b + contentView.getMeasuredHeight());
        b = b + contentView.getMeasuredHeight();
        t = b;
        footerView.layout(l, t, r + footerView.getMeasuredWidth(), b + footerView.getMeasuredWidth());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        if (getChildCount() != 1)
        {
            throw new RuntimeException("child view count of XML is only one !");
        }
        addView(headerView(), 0);
        addView(footerView(), 2);
    }

    public enum Mode
    {
        BOTH,
        FROM_HEADER,
        FROM_FOOTER
    }

    public enum State
    {
        RESET,
        PULL_TO_REFRESH,
        RELEASE_TO_REFRESH,
        REFRESHING,
        FINISH
    }

    public View headerView()
    {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, 48 * 3);
        View view = new View(getContext());
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.CYAN);
        return view;
    }

    public View footerView()
    {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, 48 * 3);
        View view = new View(getContext());
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.GREEN);
        return view;
    }

    public View contentView()
    {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        View view = new View(getContext());
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.WHITE);
        return view;
    }
}
