package com.zxy.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.zxy.frame.http.OkHttpUtil;
import com.zxy.frame.utility.SPUtil;
import com.zxy.frame.utility.ToastUtil;
import com.zxy.im.R;
import com.zxy.im.base.BaseActivity;
import com.zxy.im.cache.SPConstant;
import com.zxy.im.http.HttpInterface;
import com.zxy.im.model.LoginResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

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
        String username = mEtUsername.getText().toString();
        String password = mEtPassword.getText().toString();
        if (TextUtils.isEmpty(username))
        {
            ToastUtil.show("帐号为空!");
            return;
        } else if (TextUtils.isEmpty(password))
        {
            ToastUtil.show("密码为空!");
            return;
        } else
        {
            HttpInterface.requestLogin(username, password, new OkHttpUtil.MainCallback<LoginResponse>()
            {
                @Override
                public void onStart()
                {
                    super.onStart();
                    showProgressDialog("");
                }

                @Override
                public void onResponse(LoginResponse model)
                {
                    String token = model.getResult().getToken();
                    SPUtil.getInstance().getSP().edit()
                            .putString(SPConstant.TOKEN_STR, token)
                            .apply();
                    RongIM.connect(SPUtil.getInstance().getSP().getString(SPConstant.TOKEN_STR, ""), new RongIMClient.ConnectCallback()
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
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode)
                                {
                                    ToastUtil.show("连接 Error:" + errorCode.getMessage());
                                }
                            });
                }

                @Override
                public void onFinish()
                {
                    super.onFinish();
                    dismissProgressDialog();
                }
            });
        }
    }

}
