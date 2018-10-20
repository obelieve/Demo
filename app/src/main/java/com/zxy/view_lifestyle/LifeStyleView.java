package com.zxy.view_lifestyle;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zxy.utility.LogUtil;

/**
 * Created by zxy on 2018/10/20 10:15.
 */

public class LifeStyleView extends View
{

    public LifeStyleView(Context context)
    {
        super(context);
    }

    public LifeStyleView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        LogUtil.e();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LogUtil.e();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        LogUtil.e();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        LogUtil.e();
    }

    @Override
    public void computeScroll()
    {
        super.computeScroll();//onDraw(Canvas);会调用computeScroll();
        LogUtil.e();
    }
}
