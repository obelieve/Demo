package com.zxy.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zxy on 2018/10/12 09:23.
 */

public class HeadLinesActivity extends Activity
{
    LinearLayout linear;
    int i = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_lines);
        LinearLayout linear = (LinearLayout) findViewById(R.id.ll_content);
        final TextView tv = (TextView) findViewById(R.id.tv);
        tv.setText("ok");
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                i++;
                tv.setText("djkajak啊打开即可:" + i + "");
                new Handler().postDelayed(this, 1000);
            }
        }, 1000);
        View view = LayoutInflater.from(this).inflate(R.layout.replace, linear,true);
//        linear.addView(view);
    }
}
