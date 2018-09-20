package com.zxy.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tv = (TextView) findViewById(R.id.tv);
        tv.setText(AssetsUtil.getAssetsContent(this,"nest_text"));
    }

    public String[] getText()
    {
        String[] strings = new String[100];
        for (int i = 0; i < 100; i++)
        {
            strings[i] = "i:" + i;
        }
        return strings;
    }
}
