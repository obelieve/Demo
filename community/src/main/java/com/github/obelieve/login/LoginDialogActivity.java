package com.github.obelieve.login;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import com.github.obelieve.community.R;
import com.zxy.frame.base.ApiBaseActivity;

import butterknife.OnClick;

public class LoginDialogActivity extends ApiBaseActivity {

    @Override
    protected int layoutId() {
        return R.layout.activity_login_dialog;
    }

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = getResources().getDisplayMetrics().widthPixels;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
    }

    @OnClick({R.id.tv_login, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                LoginTypeActivity.startLoginInputPhone(this);
                finish();
                break;
            case R.id.tv_cancel:
                finish();
                break;
        }
    }
}
