package com.zxy.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zxy.utility.LogUtil;

public class BaseView extends View {

    public BaseView(Context context) {
        this(context, null);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LogUtil.e();
    }

    @Override
    protected void onAttachedToWindow() {
        LogUtil.e();
        super.onAttachedToWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LogUtil.e();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        LogUtil.e();
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        LogUtil.e();
        super.onDraw(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        LogUtil.e();
        super.onDetachedFromWindow();
    }
}
