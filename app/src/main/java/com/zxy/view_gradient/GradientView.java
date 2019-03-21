package com.zxy.view_gradient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by admin on 2019/3/20.
 */

public class GradientView extends View {

    public static final int START_COLOR = Color.parseColor("#274A8E");
    public static final int END_COLOR = Color.parseColor("#F3497C");

    private Paint mPaint;
    private LinearGradient mGradient;

    public GradientView(Context context)
    {
        super(context);
        init();
    }

    public GradientView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public void init()
    {
        mPaint = new Paint();
        mGradient = new LinearGradient(0, 0, getMeasuredWidth(), 0,
                new int[]{START_COLOR, END_COLOR},
                new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        mPaint.setShader(mGradient);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
    }

    public void setProgress(float left)
    {
        float start;
        float end;
        //按照gradient分割线的位置进行平分
        if (left <= 0.5f)
        {
            start = 0.0f;
            end = left * 2;
        } else
        {
            start = left * 2 - 1;//left-(1-left)
            end = 1;
        }
        mGradient = new LinearGradient(0, 0, getMeasuredWidth(), 0,
                new int[]{START_COLOR, END_COLOR},
                new float[]{start, end}, Shader.TileMode.CLAMP);
        invalidate();
    }


}
