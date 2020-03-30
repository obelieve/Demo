package com.zxy.demo._issue;

import com.zxy.demo.R;
import com.zxy.demo.view.SplashView;
import com.zxy.frame.base.BaseFragment;
import com.zxy.frame.utils.ToastUtil;

import butterknife.BindView;

public class SplashFragment extends BaseFragment {

    @BindView(R.id.view_splash)
    SplashView viewSplash;

    @Override
    public int layoutId() {
        return R.layout.fragment_splash;
    }

    @Override
    protected void initView() {
        viewSplash.setCallback(new SplashView.Callback() {
            @Override
            public void onNext() {
                ToastUtil.show("Next");
            }
        });
    }
}
