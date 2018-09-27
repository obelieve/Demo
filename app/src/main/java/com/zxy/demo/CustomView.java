package com.zxy.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Scroller;

import com.zxy.utility.LogUtil;

/**
 * Created by zxy on 2018/9/14 09:55.
 */

public class CustomView extends View
{
    Paint mPaint;
    Scroller mScroller;

    {
        mScroller = new Scroller(getContext());
        mScroller.startScroll(0, 0, 300, 0, 9000);
        invalidate();
    }

    public CustomView(Context context)
    {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        canvas.drawColor(Color.GRAY);
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        canvas.drawCircle(dm.widthPixels/2, dm.heightPixels/2, 100.f, mPaint);

    }

    private float mStartX;
    private float mStartY;

    private float mEndX;
    private float mEndY;

    @Override
    public void computeScroll()
    {
        if (mScroller.computeScrollOffset())
        {
            LogUtil.e("scrollOffset:StartX" + mScroller.getStartX() + " StartY:" + mScroller.getStartY()
                    + "\n CurVelocity:" + mScroller.getCurrVelocity()
                    + "\n CurX:" + mScroller.getCurrX() + " CurY:" + mScroller.getCurrY()
                    + "\n FinalX:" + mScroller.getFinalX() + " FinalY:" + mScroller.getFinalY()
                    + "\n dura:" + mScroller.getDuration());
            scrollTo(-mScroller.getCurrX(),mScroller.getCurrY());
        }
        super.computeScroll();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        mStartX = event.getX();
        mStartY = event.getY();
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }
}
