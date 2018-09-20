package com.zxy.demo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.zxy.utility.LogUtil;

/**
 * Created by zxy on 2018/9/20 16:53.
 */

public class GestureView extends ScrollView implements GestureDetector.OnGestureListener
{
    private GestureDetector mGestureDetector;
    public GestureView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        mGestureDetector = new GestureDetector(this);
        mGestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener()
        {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e)//只有确定只是单次点击时，完成才会调用；如果是双击事件那就不会调用
            {
                LogUtil.e();
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e)//双击时，第一次down动作，只有down事件
            {
                LogUtil.e();
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e)//双击时，包含双击的down,move,up事件
            {
                LogUtil.e();
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        LogUtil.e(MotionEventUtil.name(event));
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e)
    {
        LogUtil.e();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e)
    {
        LogUtil.e();
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e)//单击完成的up事件
    {
        LogUtil.e();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
    {
        LogUtil.e();
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e)
    {
        LogUtil.e();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        LogUtil.e();
        return false;
    }
}
