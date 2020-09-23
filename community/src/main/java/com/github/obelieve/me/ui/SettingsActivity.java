package com.github.obelieve.me.ui;

import android.os.Bundle;
import android.view.View;

import com.github.obelieve.App;
import com.github.obelieve.community.R;
import com.github.obelieve.repository.cache.UserHelper;
import com.zxy.frame.base.ApiBaseActivity;
import com.zxy.frame.dialog.CommonDialog;
import com.zxy.frame.net.ApiBaseResponse;
import com.zxy.frame.net.ApiBaseSubscribe;
import com.zxy.frame.net.ApiService;
import com.zxy.frame.net.ApiServiceException;
import com.zxy.frame.utils.ToastUtil;

import butterknife.OnClick;

/**
 * Created by Admin
 * on 2020/9/8
 */
public class SettingsActivity extends ApiBaseActivity {
    @Override
    protected int layoutId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        setNeedNavigate();
        setMyTitle("系统设置");
    }


    private CommonDialog mLoginOutDialog;
    @OnClick(R.id.user_login_out)
    public void onViewClicked() {
        if (mLoginOutDialog == null) {
            mLoginOutDialog = new CommonDialog(mActivity);
            mLoginOutDialog.setContent("确定退出登录？");
            mLoginOutDialog.setOkListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApiService.wrap(App.getServiceInterface().logout(),String.class)
                            .subscribe(new ApiBaseSubscribe<ApiBaseResponse<String>>(mActivity) {
                                @Override
                                public void onError(ApiServiceException e) {

                                }

                                @Override
                                public void onSuccess(ApiBaseResponse<String> response, boolean isProcessed) {
                                    UserHelper.getInstance().clearData(mActivity);
                                    ToastUtil.show("退出登录成功");
                                    finish();
                                }
                            });
                    mLoginOutDialog.dismiss();
                }
            });
            mLoginOutDialog.setCancelListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLoginOutDialog.dismiss();
                }
            });
        }
        mLoginOutDialog.show();
    }
}
