package com.zxy.view_touch;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zxy.utility.LogUtil;
import com.zxy.utility.MotionEventUtil;

/**
 * Created by zxy on 2018/10/17 11:27.
 */

public class View1 extends View
{
    public View1(Context context)
    {
        super(context);
    }

    public View1(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        LogUtil.e(MotionEventUtil.name(event));
        boolean  b = super.dispatchTouchEvent(event);
        LogUtil.e(b+" ==="+MotionEventUtil.name(event));
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
//        LogUtil.e(MotionEventUtil.name(event));
//        if(event.getAction() == MotionEvent.ACTION_DOWN){
//            LogUtil.e("点击");
//            return true;
//        }
//        return false;
        LogUtil.e(MotionEventUtil.name(event));
        boolean b= super.onTouchEvent(event);
        LogUtil.e(b+" ==="+MotionEventUtil.name(event));
        return b;
    }
}
