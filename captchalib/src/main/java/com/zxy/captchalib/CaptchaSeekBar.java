package com.zxy.captchalib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSeekBar;


public class CaptchaSeekBar extends AppCompatSeekBar {


    private Paint textPaint;

    public CaptchaSeekBar(Context context) {
        this(context, null, 0);
    }

    public CaptchaSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaptchaSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        int textSize = (int)context.getResources().getDisplayMetrics().density*14;
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("#333333"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (getHeight() / 2 - top / 2 - bottom / 2);//基线中间点的y轴计算公式
        canvas.drawText("向右滑动滑块完成拼图", getWidth() / 2, baseLineY, textPaint);
    }
}
