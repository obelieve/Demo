package com.zxy.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zxy.demo.R;
import com.zxy.frame.utils.log.LogUtil;

/**
 * Created by zxy
 * on 2020/12/23
 */
public class ThreeFragment extends Fragment {

    private static int count = 0;
    private final String uuid = ""+(count++);

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LogUtil.e(uuid);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e(uuid);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.e(uuid);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.e(uuid);
        return inflater.inflate(R.layout.fragment_three, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.e(uuid);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.e(uuid);
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.e(uuid);
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.e(uuid);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.e(uuid);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e(uuid);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.e(uuid);
    }
}
