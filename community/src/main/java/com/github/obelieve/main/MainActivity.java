package com.github.obelieve.main;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.github.obelieve.community.R;
import com.zxy.frame.base.ApiBaseActivity;

/**
 * Created by Admin
 * on 2020/9/10
 */
public class MainActivity extends ApiBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mNeedInsetStatusBar = false;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_content,new MainFragment(),MainFragment.class.getSimpleName())
                .commitAllowingStateLoss();
    }
}
