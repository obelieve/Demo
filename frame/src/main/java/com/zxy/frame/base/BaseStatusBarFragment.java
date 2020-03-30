package com.zxy.frame.base;

import android.view.ViewGroup;

import com.zxy.frame.utils.StatusBarUtil;

/**
 * 状态栏透明时，控制状态栏Light切换方式 Fragment
 */
public abstract class BaseStatusBarFragment extends BaseFragment implements IBaseStatusBar {

    @Override
    protected void initView() {
        try {
            switchStatusLight();
            ViewGroup.LayoutParams params = statusBarView().getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = StatusBarUtil.getStatusBarHeight(getActivity());
            statusBarView().setLayoutParams(params);
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
    public boolean statusBarLight() {
        return true;
    }
}
