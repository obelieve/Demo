package com.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import androidx.annotation.Nullable;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class Practice10HistogramView extends View {

    public Practice10HistogramView(Context context) {
        super(context);
    }

    public Practice10HistogramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice10HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Paint paint = new Paint();
    int[] heights=new int[]{20,100,100,300,400,500,200};
    String[] tags=new String[]{"Froyo","GB","ICS","JB","KitKat","L","M"};
    {
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        综合练习
//        练习内容：使用各种 Canvas.drawXXX() 方法画直方图
        //1.draw Line
        canvas.drawLine(100,100,100,600,paint);
        canvas.drawLine(100,600,1000,600,paint);
        for(int i=1;i<8;i++){
            int offset = 20;
            int left=i*100+offset*i;
            paint.setColor(Color.GREEN);
            int top = 600-heights[i-1];
            //2.draw Rect
            canvas.drawRect(left,top,left+100,600,paint);
            Rect rect = new Rect();
            paint.setColor(Color.WHITE);
            paint.setTextSize(30);
            paint.getTextBounds(tags[i-1],0,tags[i-1].length(),rect);
            int w = Math.max((int)((100-rect.width())/2.0f),0);
            //3.draw Text
            canvas.drawText(tags[i-1],0,tags[i-1].length(),left+w,600+30,paint);
        }
    }
}
