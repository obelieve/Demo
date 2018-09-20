package com.zxy.demo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.zxy.utility.LogUtil;

/**
 * Created by zxy on 2018/9/20 17:24.
 */

public class VelocityView extends View
{
    VelocityTracker mVelocityTracker;

    public VelocityView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }
    long start = 0,end;
    float sX = 0,eX;
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        LogUtil.e(MotionEventUtil.name(event));

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mVelocityTracker = VelocityTracker.obtain();
                if(mVelocityTracker==null){
                    mVelocityTracker = VelocityTracker.obtain();
                }
//                else{
//                    mVelocityTracker.clear();
//                }
                mVelocityTracker.addMovement(event);
                start = System.currentTimeMillis();
                sX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(100);
                LogUtil.e("VelocityTracker x:"+mVelocityTracker.getXVelocity());
                LogUtil.e("VelocityTracker y:"+mVelocityTracker.getYVelocity());
                end = (System.currentTimeMillis() - start);
                eX = (event.getX()-sX);
                LogUtil.e(start+"耗时 ms："+end+" eX:"+eX);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.clear();
                break;
        }
        return true;
    }
}
