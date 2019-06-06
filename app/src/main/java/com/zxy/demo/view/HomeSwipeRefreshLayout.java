package com.zxy.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import com.zxy.demo.R;
import com.zxy.utility.LogUtil;
import com.zxy.utility.MotionEventUtil;

public class HomeSwipeRefreshLayout extends SwipeRefreshLayout {

    public HomeSwipeRefreshLayout(@NonNull Context context) {
        this(context,null);
    }

    public HomeSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setProgressViewOffset(false, HomeTopView.getRealHeight(),HomeTopView.getRealHeight()+getProgressViewEndOffset());
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.HomeSwipeRefreshLayout);
        boolean bool  = a.getBoolean(R.styleable.HomeSwipeRefreshLayout_needTopPadding,false);
        if(bool)
            setPadding(0,HomeTopView.getRealHeight(),0,0);
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

    //Nested child

    @Override
    public boolean startNestedScroll(int axes) {
        LogUtil.e("axes="+axes);
        boolean b=super.startNestedScroll(axes);
        LogUtil.e(" return="+b);
        return b;
    }

    @Override
    public void stopNestedScroll() {
        LogUtil.e();
        super.stopNestedScroll();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        LogUtil.e("dxConsumed="+dxConsumed+" dyConsumed="+dyConsumed+" dxUnconsumed="+dxUnconsumed+" dyUnconsumed="+dyUnconsumed+" offsetInWindow="+offsetInWindow);
        boolean b=super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
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

    //nested parent

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes) {
        LogUtil.e("child=" + child + " target=" + target + " axes=" + axes);
        boolean b = super.onStartNestedScroll(child, target, axes);
        LogUtil.e("return = " + b);
        return b;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes) {
        LogUtil.e("child=" + child + " target=" + target + " axes=" + axes);
        super.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target) {
        LogUtil.e(" target=" + target);
        super.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        LogUtil.e("target=" + target + " dxConsumed=" + dxConsumed + " dyConsumed=" + dyConsumed + " dxUnconsumed=" + dxUnconsumed + " dyUnconsumed=" + dyUnconsumed);
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        LogUtil.e("target=" + target + " dx=" + dx + " dy=" + dy + " consumed=" + consumed);
        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        LogUtil.e("target=" + target + " velocityX=" + velocityX + " velocityY=" + velocityY + " consumed=" + consumed);
        boolean b = super.onNestedFling(target, velocityX, velocityY, consumed);
        LogUtil.e("return = " + b);
        return b;
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        LogUtil.e("target=" + target + " velocityX=" + velocityX + " velocityY=" + velocityY);
        boolean b = super.onNestedPreFling(target, velocityX, velocityY);
        LogUtil.e("return = " + b);
        return b;
    }

    @Override
    public int getNestedScrollAxes() {
        LogUtil.e();
        int a = super.getNestedScrollAxes();
        LogUtil.e("return = " + a);
        return a;

    }
}
