package com.zxy.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import com.zxy.http.UIThreadCallback;
import com.zxy.im.R;
import com.zxy.im.base.BaseActivity;
import com.zxy.im.http.HttpInterface;
import com.zxy.im.model.LoginResponse;
import com.zxy.json.JsonUtil;
import com.zxy.utility.LogUtil;
import com.zxy.utility.SPUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zxy on 2018/12/12 16:31.
 */

public class LoginActivity extends BaseActivity
{

    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.btn_login)
    Button mBtnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked()
    {
        HttpInterface.requestLogin(mEtUsername.getText().toString(), mEtPassword.getText().toString(), new UIThreadCallback()
        {
            @Override
            public void onFailure(Exception e)
            {
                LogUtil.e(e.getLocalizedMessage());
            }

            @Override
            public void onResponse(String s)
            {
                LogUtil.e(s);
                LoginResponse loginResponse =JsonUtil.parseJson(s, LoginResponse.class);
                String id = loginResponse.getResult().getId();
                String token = loginResponse.getResult().getToken();
                SPUtil.getInstance().getSP().edit().putString("id",id).commit();
                SPUtil.getInstance().getSP().edit().putString("token",token).commit();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });
    }

}
