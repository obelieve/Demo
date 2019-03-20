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

    private float mStart = 0.0f;
    private float mEnd = 1f;

    public GradientView(Context context) {
        super(context);
        init();
    }

    public GradientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        LinearGradient gradient = new LinearGradient(0, 0, getMeasuredWidth(), 0,
                new int[]{START_COLOR, END_COLOR},
                new float[]{mStart, mEnd}, Shader.TileMode.CLAMP);
        paint.setShader(gradient);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);
    }

    public void setProgress(float left) {
        //按照gradient分割线的位置进行平分
        if (left <= 0.5f) {
            mStart = 0.0f;
            mEnd = left * 2;
        } else {
            mStart = left * 2 - 1;//left-(1-left)
            mEnd = 1;
        }
        invalidate();
    }

}
