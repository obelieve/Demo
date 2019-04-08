package com.zxy.view_lifestyle;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zxy.utility.LogUtil;

/**
 * Created by admin on 2019/4/7.
 */

public class LifecycleBaseFragment extends Fragment {


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.e();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        LogUtil.e();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.e();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        LogUtil.e();
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.e();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.e();
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.e();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtil.e();
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.e();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.e();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.e();
    }
}
