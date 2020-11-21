package com.zxy.demo.fragment;

import android.content.Intent;
import android.graphics.Color;

import com.zxy.demo.R;
import com.zxy.demo.activity.SplashActivity;
import com.zxy.demo.databinding.FragmentSplashBinding;
import com.zxy.frame.base.ApiBaseFragment;
import com.zxy.frame.view.SplashView;

public class SplashFragment extends ApiBaseFragment<FragmentSplashBinding> {


    @Override
    protected void initView() {
        mViewBinding.viewSplash.loadData(new int[]{R.drawable.splash_1, R.drawable.splash_2, R.drawable.splash_3},R.drawable.bg_indicator_main,R.drawable.bg_indicator_cccccc,
                Color.parseColor("#37bd42"));
        mViewBinding.viewSplash.setCallback(new SplashView.Callback() {
            @Override
            public void onNext() {
                startActivity(new Intent(getActivity(), SplashActivity.class));
            }
        });
    }
}
