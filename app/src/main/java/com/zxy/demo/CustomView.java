package com.zxy.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

/**
 * Created by zxy on 2018/9/14 09:55.
 */

public class CustomView extends View
{
    Paint mPaint;
    Scroller mScroller;

    {
        mScroller = new Scroller(getContext());
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
        canvas.drawCircle(0, 0, 20.f, mPaint);
    }

    private float mStartX;
    private float mStartY;

    private float mEndX;
    private float mEndY;

    @Override
    public void computeScroll()
    {
        super.computeScroll();
//        if(mScroller.computeScrollOffset()){
//
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        mStartX = event.getX();
        mStartY = event.getY();
        //scrollBy((int)mStartX,(int)mStartY);
//                invalidate();
//                scrollTo(10,10);
        scrollTo(-(int)mStartX,-(int)mStartY);
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:

//                LogUtil.e("RawX:"+event.getRawX()+" RawY:"+event.getRawY());
//                LogUtil.e("StartX:"+mStartX+" StartY:"+mStartY);
                break;
            case MotionEvent.ACTION_MOVE:
//                mEndX = event.getX();
//                mEndY = event.getY();
//                int x = -(int) (mEndX - mStartX);
//                int y = -(int) (mEndY - mStartY);
//                scrollBy(x, y);
//                LogUtil.e("x:" + x + " y:" + y);
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
//        return super.onTouchEvent(event);
        return true;
    }
}
