package com.zxy.view_one;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.zxy.demo.R;

import java.util.Random;

/**
 * Created by zxy on 2018/10/26 16:11.
 */

public class OneActivity extends Activity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        final PKProgressBar progress_pk = findViewById(R.id.progress_pk);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                int left = new Random().nextInt(1000000);
                int right = new Random().nextInt(1000000);
                progress_pk.setLeftAndRightProgress(left, right);
                new Handler().postDelayed(this, 1000);

            }
        }, 1000);

    }
}
