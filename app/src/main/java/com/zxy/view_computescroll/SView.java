package com.zxy.view_computescroll;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.zxy.utility.LogUtil;

/**
 * Created by zxy on 2018/10/20 17:36.
 */

public class SView extends FrameLayout
{
    public SView(@NonNull Context context)
    {
        super(context);
    }

    public SView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public void computeScroll()
    {
        super.computeScroll();
        LogUtil.e();
    }
}
