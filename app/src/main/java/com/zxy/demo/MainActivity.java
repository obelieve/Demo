package com.zxy.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.zxy.im.database.AppCacheManager;
import com.zxy.im.database.UserInfo;
import com.zxy.utility.LogUtil;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCacheManager.init(getApplicationContext());
        AppCacheManager.getInstance().getUserInfo("1");
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                UserInfo userInfo = AppCacheManager.getInstance().getUserInfo("1");
                LogUtil.e(userInfo + "");
            }
        }, 1000);
    }
}
