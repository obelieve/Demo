package com.zxy.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by zxy on 2018/10/12 09:23.
 */

public class HeadLinesActivity extends Activity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_lines);
        DeviceInfoUtil.init(this);
        FlowLayout fl = (FlowLayout) findViewById(R.id.fl);
        for (int i = 0; i < 20; i++)
        {
            fl.addView(getFrameLayout());
        }
    }

    public FrameLayout getFrameLayout()
    {
        int spacing = DeviceInfoUtil.dpToPx(5);

        FrameLayout fl = new FrameLayout(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fl.setLayoutParams(params);

        FrameLayout.LayoutParams flParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        flParams.setMargins(spacing, spacing + spacing, spacing + spacing, spacing);
        TextView tv = new TextView(this);
        tv.setLayoutParams(flParams);
        tv.setPadding(spacing, spacing + spacing, spacing + spacing, spacing);
        tv.setBackgroundColor(Color.parseColor("#A2B6CA"));
        tv.setText(new Random().nextInt(1000) + ":驱蚊器去请问其");
        fl.addView(tv);
        return fl;
    }
}
