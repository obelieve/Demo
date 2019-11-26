package com.zxy.frame.base;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    /**
     * Fragment中，是否要更新状态栏
     */
    private boolean mDisableStatusBarUpdate = false;

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId(), container, false);
        mUnbinder = ButterKnife.bind(this,view);
        initView();
        return view;
    }

    public abstract int layoutId();

    protected abstract void initView();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

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
