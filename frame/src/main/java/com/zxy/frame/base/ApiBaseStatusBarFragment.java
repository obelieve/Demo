package com.zxy.frame.base;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewbinding.ViewBinding;

import com.zxy.frame.utils.StatusBarUtil;

/**
 * 状态栏透明时，控制状态栏Light切换方式 Fragment
 */
public abstract class ApiBaseStatusBarFragment<T extends ViewBinding> extends ApiBaseFragment<T> implements IBaseStatusBar {

    @Override
    protected void initView() {
        try {
            switchStatusLight();
            View view = statusBarView();
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = StatusBarUtil.getStatusBarHeight(getActivity());
            view.setLayoutParams(params);
            view.setBackgroundColor(statusBarColor());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchStatusLight() {
        StatusBarUtil.setWindowLightStatusBar(getActivity(), statusBarLight());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            switchStatusLight();
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        if (!isHidden()) {
            switchStatusLight();
        }
        super.onResume();
    }

    @Override
    public int statusBarColor() {
        return Color.WHITE;
    }

    @Override
    public boolean statusBarLight() {
        return true;
    }
}
