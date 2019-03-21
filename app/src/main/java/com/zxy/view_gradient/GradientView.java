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

    public static final int LEFT_START_COLOR = Color.parseColor("#5458AE");
    public static final int LEFT_END_COLOR = Color.parseColor("#4E82F0");
    public static final int RIGHT_START_COLOR = Color.parseColor("#F34173");
    public static final int RIGHT_END_COLOR = Color.parseColor("FE6F9C");

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
                new int[]{LEFT_START_COLOR, RIGHT_END_COLOR},
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
        //gradient中间位置划分
        if (left == 0.5f)
        {
            start = 0.0f;
            end = 1.0f;
            mGradient = new LinearGradient(0, 0, getMeasuredWidth(), 0,
                    new int[]{LEFT_START_COLOR, RIGHT_END_COLOR},
                    new float[]{start, end}, Shader.TileMode.CLAMP);
        } else if (left < 0.5f)
        {
            start = 0.0f;
            end = left * 2;
            mGradient = new LinearGradient(0, 0, getMeasuredWidth(), 0,
                    new int[]{LEFT_START_COLOR, RIGHT_START_COLOR, RIGHT_END_COLOR},
                    new float[]{start, end, 1}, Shader.TileMode.CLAMP);
        } else
        {
            start = left * 2 - 1;//left-(1-left)
            end = 1;
            mGradient = new LinearGradient(0, 0, getMeasuredWidth(), 0,
                    new int[]{LEFT_START_COLOR, LEFT_END_COLOR, RIGHT_END_COLOR},
                    new float[]{0, start, end}, Shader.TileMode.CLAMP);
        }

        invalidate();
    }


}
