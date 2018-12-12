package com.zxy.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.zxy.im.base.BaseActivity;
import com.zxy.utility.LogUtil;
import com.zxy.utility.SPUtil;

/**
 * Created by zxy on 2018/12/12 16:41.
 */

public class WelActivity extends BaseActivity
{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        String token = SPUtil.getInstance().getSP().getString("token","");
        Class class1 = null;
        LogUtil.e("Token",token);
        if(TextUtils.isEmpty(token)){
            class1 = LoginActivity.class;
        }else{
            class1 = MainActivity.class;
        }
        startActivity(new Intent(WelActivity.this,class1));
    }
}
