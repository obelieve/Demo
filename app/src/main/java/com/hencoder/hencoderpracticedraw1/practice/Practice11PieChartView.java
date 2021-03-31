package com.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;

import androidx.annotation.Nullable;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.zxy.utility.LogUtil;

public class Practice11PieChartView extends View {

    public Practice11PieChartView(Context context) {
        super(context);
    }

    public Practice11PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice11PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Paint linePaint = new Paint();
    Paint textPaint = new Paint();
    Paint paint = new Paint();
    RectF rect = new RectF();
    String[] strings = new String[]{
            "Lollipop",
            "Marshmallow",
            "Froyo",
            "Gingerbread",
            "Ice Cream Sandwich",
            "Jelly Bean",
            "KitKat"
    };
    int[] colors = new int[]{
            Color.parseColor("#F44133"),
            Color.parseColor("#FFC201"),
            Color.parseColor("#00000000"),
            Color.parseColor("#9D22B2"),
            Color.parseColor("#9F9F9F"),
            Color.parseColor("#009789"),
            Color.parseColor("#1B97F3")};

    {
        linePaint.setColor(Color.parseColor("#B0B3B5"));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(4);
        textPaint.setTextSize(25);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        int radius = 300;
        int width = radius * 2;
        rect.set(200, 100, 200 + width, 100 + width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        综合练习
//        练习内容：使用各种 Canvas.drawXXX() 方法画饼图
        paint.setColor(colors[0]);
        canvas.drawArc(rect, 180, 120, true, paint);
        canvas.save();
        drawLineAndText(canvas,240,strings[0]);
        canvas.translate(20, 20);
        paint.setColor(colors[1]);
        canvas.drawArc(rect, -60, 60, true, paint);
        drawLineAndText(canvas,330,strings[1]);
        paint.setColor(colors[2]);
        canvas.drawArc(rect, 0, 2, true, paint);
        drawLineAndText(canvas,1,strings[2]);
        paint.setColor(colors[3]);
        canvas.drawArc(rect, 3, 8, true, paint);
        drawLineAndText(canvas,7,strings[3]);
        paint.setColor(colors[4]);
        canvas.drawArc(rect, 14, 5, true, paint);
        drawLineAndText(canvas,16,strings[4]);
        paint.setColor(colors[5]);
        canvas.drawArc(rect, 22, 50, true, paint);
        drawLineAndText(canvas,47,strings[5]);
        paint.setColor(colors[6]);
        canvas.drawArc(rect, 74, 105, true, paint);
        drawLineAndText(canvas,126,strings[6]);
        canvas.restore();
    }

    /**
     * 绘制 扇形上的线和 名称
     * @param canvas
     * @param angle 0~360
     * @param text
     */
    public void drawLineAndText(Canvas canvas,int angle,String text){
        float radian = (float) Math.toRadians(angle);
        float currentRadius = 300;
        /**
         * 获取圆弧上坐标的点, 按坐标系以圆点为中心建立坐标系计算 0~180度，0~-180度
         */
        float x = (float) (500 + Math.cos(radian)*currentRadius);
        float y = (float) (400 + Math.sin(radian)*currentRadius);
        LogUtil.e("drawLineAndText: angle ="+angle+" x ="+x+" y = "+y+" radian = "+radian
                +"sin = "+Math.sin(radian)+" cos = "+Math.cos(radian));
        int x2 = (int) (x + (int) ( 30* Math.cos(radian)));
        int y2 = (int) (y + 30 * Math.sin(radian));
        Rect textRect = new Rect();
        linePaint.getTextBounds(text, 0, text.length(), textRect);
        int textWidth = textRect.width();
        int textHeight = textRect.height();
        Path path = new Path();
        path.rMoveTo(x, y);
        path.lineTo(x2, y2);
        int lineWidth = 60;//线宽
        int lineSpan = 20;//线和文字间距
        if(angle>90&&angle<270){
            //绘制在左边
            path.lineTo(x2 - lineWidth, y2);
            canvas.drawPath(path, linePaint);
            canvas.drawText(text,(x2-lineWidth-textWidth-lineSpan*2),y2+textHeight/2,textPaint);
        }else{
            //绘制在右边
            path.lineTo(x2 + lineWidth, y2);
            canvas.drawPath(path, linePaint);
            canvas.drawText(text,(x2+lineWidth+lineSpan),y2+textHeight/2,textPaint);
        }
    }
}
