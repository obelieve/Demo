package com.zxy.demo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.NestedScrollingParent2;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zxy.utility.LogUtil;
import com.zxy.utility.MotionEventUtil;

public class HomeCl extends CoordinatorLayout {
    public HomeCl(Context context) {
        super(context);
    }

    public HomeCl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeCl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtil.e(MotionEventUtil.name(ev));
        boolean b = super.dispatchTouchEvent(ev);
        LogUtil.e(MotionEventUtil.name(ev) + " " + b);
        return b;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        LogUtil.e(MotionEventUtil.name(e));
        boolean b = super.onInterceptTouchEvent(e);
        LogUtil.e(MotionEventUtil.name(e) + " " + b);
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        LogUtil.e(MotionEventUtil.name(e));
        boolean b = super.onTouchEvent(e);
        LogUtil.e(MotionEventUtil.name(e) + " " + b);
        return b;
    }

    //Nested

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        LogUtil.e("child=" + child + " target=" + target + " axes=" + axes + " type=" + type);
        boolean b = super.onStartNestedScroll(child, target, axes, type);
        LogUtil.e("return = " + b);
        return b;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        LogUtil.e("child=" + child + " target=" + target + " axes=" + axes + " type=" + type);
        super.onNestedScrollAccepted(child, target, axes, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        LogUtil.e(" target=" + target + " type=" + type);
        super.onStopNestedScroll(target, type);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        LogUtil.e("target=" + target + " type=" + type);
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        LogUtil.e("target=" + target + " dx=" + dx + " dy=" + dy + " consumed=" + consumed + " type=" + type);
        super.onNestedPreScroll(target, dx, dy, consumed, type);
    }

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
