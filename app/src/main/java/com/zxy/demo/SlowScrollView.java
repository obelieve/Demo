package com.zxy.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by zxy on 2018/9/25 10:24.
 */

public class SlowScrollView extends ScrollView
{
    public SlowScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public void fling(int velocityY)
    {
        super.fling(velocityY*2);
    }
}
