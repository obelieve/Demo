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
        LogUtil.e(this + "");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtil.e(this + "");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.e(this + "");
        return LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_base, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LogUtil.e(this + "");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        LogUtil.e(this + "");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        LogUtil.e(this + "");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onStart() {
        LogUtil.e(this + "");
        super.onStart();
    }

    @Override
    public void onResume() {
        LogUtil.e(this + "");
        super.onResume();
    }

    @Override
    public void onPause() {
        LogUtil.e(this + "");
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        LogUtil.e(this + "");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        LogUtil.e(this + "");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        LogUtil.e(this + "");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        LogUtil.e(this + "");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        LogUtil.e(this + "");
        super.onDetach();
    }
}
