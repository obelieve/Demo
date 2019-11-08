package com.zxy.frame.base;

import android.graphics.Color;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    /**
     * Fragment中，是否要更新状态栏
     */
    private boolean mDisableStatusBarUpdate = false;

    public void setDisableStatusBarUpdate(boolean disable) {
        mDisableStatusBarUpdate = disable;
    }

    public boolean isDisableStatusBarUpdate() {
        return mDisableStatusBarUpdate;
    }

    public int settingStatusBarColor() {
        return Color.TRANSPARENT;
    }

    public boolean settingStatusBarLight() {
        return false;
    }

}
