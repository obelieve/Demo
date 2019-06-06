package com.zxy.demo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zxy.utility.LogUtil;
import com.zxy.utility.MotionEventUtil;

public class HomeRv extends RecyclerView {

    public HomeRv(Context context) {
        super(context);
    }

    public HomeRv(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeRv(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtil.e(MotionEventUtil.name(ev));
        boolean b=super.dispatchTouchEvent(ev);
        LogUtil.e(MotionEventUtil.name(ev)+" "+b);
        return b;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        LogUtil.e(MotionEventUtil.name(e));
        boolean b=super.onInterceptTouchEvent(e);
        LogUtil.e(MotionEventUtil.name(e)+" "+b);
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        LogUtil.e(MotionEventUtil.name(e));
        boolean b=super.onTouchEvent(e);
        LogUtil.e(MotionEventUtil.name(e)+" "+b);
        return b;
    }

    //Nested

    @Override
    public boolean startNestedScroll(int axes) {
        LogUtil.e("axes="+axes);
        boolean b=super.startNestedScroll(axes);
        LogUtil.e(" return="+b);
        return b;
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        LogUtil.e("axes="+axes+" type="+type);
        boolean b=super.startNestedScroll(axes, type);
        LogUtil.e(" return="+b);
        return b;
    }

    @Override
    public void stopNestedScroll() {
        LogUtil.e();
        super.stopNestedScroll();
    }

    @Override
    public void stopNestedScroll(int type) {
        LogUtil.e(" type="+type);
        super.stopNestedScroll(type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        LogUtil.e("dxConsumed="+dxConsumed+" dyConsumed="+dyConsumed+" dxUnconsumed="+dxUnconsumed+" dyUnconsumed="+dyUnconsumed+" offsetInWindow="+offsetInWindow);
        boolean b=super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
        LogUtil.e(" return="+b);
        return b;
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow, int type) {
        LogUtil.e("dxConsumed="+dxConsumed+" dyConsumed="+dyConsumed+" dxUnconsumed="+dxUnconsumed+" dyUnconsumed="+dyUnconsumed+" offsetInWindow="+offsetInWindow+" type="+type);
        boolean b=super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
        LogUtil.e(" return="+b);
        return b;
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow, int type) {
        LogUtil.e("dx="+dx+" dy="+dy+" consumed="+consumed+" offsetInWindow="+offsetInWindow+" type="+type);
        boolean b=super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
        LogUtil.e(" return="+b);
        return b;
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        LogUtil.e("dx="+dx+" dy="+dy+" consumed="+consumed+" offsetInWindow="+offsetInWindow);
        boolean b=super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
        LogUtil.e(" return="+b);
        return b;
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        LogUtil.e("velocityX="+velocityX+" velocityY="+velocityY+" consumed="+consumed);
        boolean b=super.dispatchNestedFling(velocityX, velocityY, consumed);
        LogUtil.e(" return="+b);
        return b;
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        LogUtil.e("velocityX="+velocityX+" velocityY="+velocityY);
        boolean b=super.dispatchNestedPreFling(velocityX, velocityY);
        LogUtil.e(" return="+b);
        return b;
    }
}
