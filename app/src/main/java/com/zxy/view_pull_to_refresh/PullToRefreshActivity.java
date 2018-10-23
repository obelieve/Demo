package com.zxy.view_pull_to_refresh;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zxy.demo.R;

/**
 * Created by zxy on 2018/10/23 16:11.
 */

public class PullToRefreshActivity extends Activity
{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_to_refresh);
    }
}
