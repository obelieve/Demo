package com.zxy.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.zxy.frame.utility.SPUtil;
import com.zxy.frame.utility.ToastUtil;
import com.zxy.im.base.BaseActivity;
import com.zxy.im.cache.SPConstant;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;


/**
 * Created by zxy on 2018/12/12 16:41.
 */

public class SplashActivity extends BaseActivity
{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                String token = SPUtil.getInstance().getSP().getString(SPConstant.TOKEN_STR, "");
                if (TextUtils.isEmpty(token))
                {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                } else
                {
                    RongIM.connect(token, new RongIMClient.ConnectCallback()
                    {
                        @Override
                        public void onTokenIncorrect()
                        {
                            ToastUtil.show("Token 未连接！");
                        }

                        @Override
                        public void onSuccess(String s)
                        {
                            SPUtil.getInstance().getSP().edit()
                                    .putString(SPConstant.USER_ID_STR, s).apply();
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode)
                        {
                            ToastUtil.show("连接 Error:" + errorCode.getMessage());
                        }
                    });
                }
            }
        }, 800);
    }
}
