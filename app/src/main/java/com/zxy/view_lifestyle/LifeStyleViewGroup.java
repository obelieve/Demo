package com.zxy.view_lifestyle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.zxy.utility.LogUtil;

/**
 * Created by zxy on 2018/10/20 10:15.
 */

public class LifeStyleViewGroup extends ViewGroup
{

    public LifeStyleViewGroup(Context context)
    {
        super(context);
    }

    public LifeStyleViewGroup(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        LogUtil.e();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LogUtil.e();
        for (int i = 0; i < getChildCount(); i++)
        {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        LogUtil.e();
        l = 0;
        t = 0;
        b = 0;
        for (int i = 0; i < getChildCount(); i++)
        {
            b = t + getChildAt(i).getMeasuredHeight();
            getChildAt(i).layout(l, t, r + getChildAt(i).getMeasuredWidth(), b);
            t = b;
            LogUtil.e("l:" + l + "t:" + t + "r:" + r + "b:" + b);
        }
    }
}
