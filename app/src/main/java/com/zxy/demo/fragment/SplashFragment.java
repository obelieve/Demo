package com.zxy.demo.fragment;

import android.content.Intent;
import android.graphics.Color;

import com.zxy.demo.R;
import com.zxy.demo.activity.SplashActivity;
import com.zxy.frame.base.ApiBaseFragment;
import com.zxy.frame.view.SplashView;

import butterknife.BindView;

public class SplashFragment extends ApiBaseFragment {

    @BindView(R.id.view_splash)
    SplashView viewSplash;

    @Override
    public int layoutId() {
        return R.layout.fragment_splash;
    }

    @Override
    protected void initView() {
        viewSplash.loadData(new int[]{R.drawable.splash_1, R.drawable.splash_2, R.drawable.splash_3},R.drawable.bg_indicator_main,R.drawable.bg_indicator_cccccc,
                Color.parseColor("#37bd42"));
        viewSplash.setCallback(new SplashView.Callback() {
            @Override
            public void onNext() {
                startActivity(new Intent(getActivity(), SplashActivity.class));
            }
        });
    }
}
