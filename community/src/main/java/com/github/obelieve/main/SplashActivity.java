package com.github.obelieve.main;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.github.obelieve.community.R;
import com.github.obelieve.repository.cache.constant.PreferenceConst;
import com.github.obelieve.utils.ActivityUtil;
import com.github.obelieve.utils.AppDataUtil;
import com.zxy.frame.base.ApiBaseActivity;
import com.zxy.frame.utils.SPUtil;

/**
 * Created by Admin
 * on 2020/8/27
 */
public class SplashActivity extends ApiBaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mNeedInsetStatusBar = false;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        //第一次打开弹出提示
        boolean KEY_FIRST_ENTER = SPUtil.getInstance().getBoolean(PreferenceConst.KEY_FIRST_ENTER, true);
        if (KEY_FIRST_ENTER) {
            AppDataUtil.showPrivacyDialog(this, new AppDataUtil.OnComfirmListener() {
                @Override
                public void onConfirm() {
                    enter();
                }
            });
        } else {
            enter();
        }
    }


    void enter() {
        new Handler().postDelayed(() -> {
            ActivityUtil.gotoMainActivity(mActivity);
            finish();
        }, 1000);
    }
}
