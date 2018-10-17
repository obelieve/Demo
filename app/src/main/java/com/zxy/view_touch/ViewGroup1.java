package com.zxy.view_touch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.zxy.utility.LogUtil;
import com.zxy.utility.MotionEventUtil;

/**
 * Created by zxy on 2018/10/17 11:28.
 */

public class ViewGroup1 extends ViewGroup
{

    public ViewGroup1(Context context)
    {
        super(context);
    }

    public ViewGroup1(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        getChildAt(0).measure
                (
                        getChildMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), getPaddingLeft() + getPaddingRight(), widthMeasureSpec),
                        getChildMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), getPaddingTop() + getPaddingBottom(), heightMeasureSpec)
                );
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        getChildAt(0).layout(l, t, r + getChildAt(0).getMeasuredWidth(), getChildAt(0).getMeasuredHeight() + b);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        LogUtil.e(MotionEventUtil.name(ev));
        boolean  b = super.onInterceptTouchEvent(ev);
        LogUtil.e(b+" ==="+MotionEventUtil.name(ev));
        return b;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        LogUtil.e(MotionEventUtil.name(ev));
        boolean  b = super.dispatchTouchEvent(ev);
        LogUtil.e(b+" ==="+MotionEventUtil.name(ev));
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        LogUtil.e(MotionEventUtil.name(event));
        if (event.getAction() == MotionEvent.ACTION_MOVE)
        {
            LogUtil.e("点击 move");
            return true;
        }
        return false;
//        LogUtil.e(MotionEventUtil.name(event));
//        boolean b= super.onTouchEvent(event);
//        LogUtil.e(b+" ==="+MotionEventUtil.name(event));
//        return b;
    }
}
