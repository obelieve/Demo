package com.zxy.demo;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.WindowManager;
import android.widget.TextView;

import com.zxy.utility.LogUtil;

import java.util.Random;

public class MainActivity extends AppCompatActivity
{
    AppBarLayout mAppBar;
    TextView mTvTitle, mTvScroll;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//此FLAG可使状态栏透明，且当前视图在绘制时，从屏幕顶端开始即top = 0开始绘制，这也是实现沉浸效果的基础
        }
        setContentView(R.layout.activity_main);
        mAppBar = (AppBarLayout) findViewById(R.id.appbar);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvScroll = (TextView) findViewById(R.id.tv_scroll);
        mTvScroll.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTvTitle.setText(DataUtil.getRandomContent(1000));
        mTvScroll.setText(DataUtil.getRandomContent(3000));

        //appBarLayout.getTotalScrollRange() 表示总的可滚动高度
        //verticalOffset 表示向上滚动的偏移值 0 ~ -appBarLayout.getTotalScrollRange()
        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener()
        {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset)
            {
                int totalRange = appBarLayout.getTotalScrollRange();
                //向上到达一半以上时处理
                if (verticalOffset < (-1 * totalRange / 2))
                {
                    mTvTitle.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    if (verticalOffset == -totalRange)
                    {
                        mTvTitle.setTextColor(Color.CYAN);
                    }else{
                        mTvTitle.setTextColor(Color.BLACK);
                    }
                } else
                {
                    mTvTitle.setTextColor(Color.BLACK);
                    mTvTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                LogUtil.e("total:" + totalRange + " \n verticalOffset:" + verticalOffset);
            }
        });
    }

    public static class DataUtil
    {
        public static String getRandomContent(int count)
        {
            Random random = new Random();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < count; i++)
            {
                builder.append((char)random.nextInt(128));
            }
            return builder.toString();
        }
    }

}
