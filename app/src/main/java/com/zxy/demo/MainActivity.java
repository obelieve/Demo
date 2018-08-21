package com.zxy.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.m7.imkfsdk.KfStartHelper;
import com.zxy.utility.LogUtil;
import com.zxy.utility.SecretUtil;


public class MainActivity extends AppCompatActivity
{
    private KfStartHelper mKfStartHelper;
    private String mUserName="super";
    private String mUserId="007";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void kefu(View view){
        if (mKfStartHelper == null)
        {
            mKfStartHelper = new KfStartHelper(this);
        }
        mKfStartHelper.initSdkChat(getString(R.string.qimoor_receiver_action),SecretUtil.decryptBase64AES(getString(R.string.qimoor_sdk_key).trim(),getString(R.string.app_name)), mUserName, mUserId);
    }
}
