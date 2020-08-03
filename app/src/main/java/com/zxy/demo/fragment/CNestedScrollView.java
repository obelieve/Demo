package com.zxy.demo.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.zxy.utility.LogUtil;

public class CNestedScrollView extends NestedScrollView {

    public CNestedScrollView(@NonNull Context context) {
        super(context);
        LogUtil.e(" h=" + getMeasuredHeight() + " w=" + getMeasuredWidth());
    }

    public CNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LogUtil.e(" h=" + getMeasuredHeight() + " w=" + getMeasuredWidth());
    }

    public CNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LogUtil.e(" h=" + getMeasuredHeight() + " w=" + getMeasuredWidth());
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LogUtil.e(" h=" + getMeasuredHeight() + " w=" + getMeasuredWidth());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        LogUtil.e(" h=" + getMeasuredHeight() + " w=" + getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LogUtil.e(" h=" + getMeasuredHeight() + " w=" + getMeasuredWidth());
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        LogUtil.e(getContext()+" h=" + getMeasuredHeight() + " w=" + getMeasuredWidth());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogUtil.e(getContext()+" h=" + getMeasuredHeight() + " w=" + getMeasuredWidth());
    }
}