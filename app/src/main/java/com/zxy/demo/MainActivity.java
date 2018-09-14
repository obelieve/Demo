package com.zxy.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zxy.utility.LogUtil;

/**
 * Scroll 视图的滚动
 * 1.坐标系(0,0)，所有的滚动都相对于坐标系
 * 2.视图绘制：1.绘制背景；2.绘制内容；3.绘制子视图；4.绘制褪色效果；5.绘制额外的区域，滚动条等；
 * 3.滚动通过位移产生，canvas.translate(x-sx,y-sy);sx,sy为mScrollX,mScrollY 所以mScrollX<0向右滚，mScrollX>0向左滚；mScrollY<0向下滚，mScrollY>0向上滚
 * 4.通过scrollBy在基础上再进行滚动，scrollTo直接滚动；
 * 5.Scroller ->提供滚动的变化值，来调用；
 * 6.VelocityTracker 提供了速率，来进行 fling效果，也就是说事件后可以再滑动；
 * 7.ViewConfigure提供了一个Slop和Velocity的默认值来判断滚动距离和速度；
 */
public class MainActivity extends AppCompatActivity
{
    CustomView mCustomView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCustomView = (CustomView)findViewById(R.id.cv);
        mCustomView.setScrollX(-100);
        mCustomView.setScrollY(-100);
    }

    public void btn(View view)
    {
        LogUtil.e("getTop:"+mCustomView.getTop()+" getBottom:"+mCustomView.getBottom());
        //mCustomView.scrollBy(1*10,1*10);
        mCustomView.scrollTo(0,0);
        LogUtil.e("after getTop:"+mCustomView.getTop()+" getBottom:"+mCustomView.getBottom());
    }
}
