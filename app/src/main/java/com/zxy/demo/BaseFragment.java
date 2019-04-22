package com.zxy.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxy.utility.LogUtil;

public class BaseFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        LogUtil.e();
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtil.e();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.e();
        return new BaseView(getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LogUtil.e();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        LogUtil.e();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        LogUtil.e();
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onStart() {
        LogUtil.e();
        super.onStart();
    }

    @Override
    public void onResume() {
        LogUtil.e();
        super.onResume();
    }

    @Override
    public void onPause() {
        LogUtil.e();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        LogUtil.e();
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        LogUtil.e();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        LogUtil.e();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        LogUtil.e();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        LogUtil.e();
        super.onDetach();
    }
}
