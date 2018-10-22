package com.zxy.view_computescroll;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.zxy.utility.LogUtil;

/**
 * Created by zxy on 2018/10/20 17:36.
 */

public class SView extends FrameLayout
{
    Scroller mScroller;

    {
        mScroller = new Scroller(getContext());
    }

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

        if (mScroller.computeScrollOffset())
        {
            LogUtil.e("computeScrollOffset()");
            scrollTo(0, mScroller.getCurrY());
//            int[] ints = new int[]{Color.BLACK, Color.WHITE, Color.BLUE, Color.RED, Color.LTGRAY};
//            setBackgroundColor(ints[new Random().nextInt(ints.length)]);//设置背景 onDraw()会调用
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        LogUtil.e();
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        LogUtil.e();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LogUtil.e();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        LogUtil.e();
    }
}
