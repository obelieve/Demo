package com.hencoder.hencoderpracticedraw2.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice09StrokeCapView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public Practice09StrokeCapView(Context context) {
        super(context);
    }

    public Practice09StrokeCapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice09StrokeCapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setStrokeWidth(40);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 使用 Paint.setStrokeCap() 来设置端点形状
        paint.setStrokeCap(Paint.Cap.BUTT);
        // 第一个：BUTT
        canvas.drawLine(50, 50, 400, 50, paint);
        paint.setStrokeCap(Paint.Cap.ROUND);
        // 第二个：ROUND
        canvas.drawLine(50, 150, 400, 150, paint);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        // 第三个：SQUARE
        canvas.drawLine(50, 250, 400, 250, paint);
    }
}
