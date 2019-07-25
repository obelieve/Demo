package com.ainirobot.sdk_demo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ainirobot.sdk_demo.R;
import com.ainirobot.sdk_demo.model.BaseFragment;
import com.ainirobot.sdk_demo.module.LeadingModule;

/**
 * 专门用来展示场景的界面
 *
 * @author simon
 */
public class LeadFragment extends BaseFragment implements LeadingModule.LeadListener {
    private View mContainer;
    private TextView tvNavigation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainer = inflater.inflate(R.layout.layout_lead, container, false);
        initView();
        registCallBack();
        return mContainer;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unRegistCallBack();
    }

    private void registCallBack() {
        LeadingModule.getInstance().registerLeadListener(this);
    }
    private void unRegistCallBack() {
        LeadingModule.getInstance().unRegistLeadListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tvNavigation = mContainer.findViewById(R.id.tv_func_navitation);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void leading(String mNavPlace) {
        tvNavigation.setText(String.format("%s%s", getString(R.string.orion_nav_place), mNavPlace));
    }

    @Override
    public void leadEnd() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

}
