package com.zxy.demo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxy.demo.base.BaseFragment;

/**
 * Created by zxy on 2018/10/30 10:37.
 */

public class DiscoveryFragment extends BaseFragment
{

    @Override
    public boolean settingStatusBarLight() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        TextView view = new TextView(getContext());
        view.setText("咨询");
        view.setTextColor(Color.WHITE);
        return view;
    }
}
