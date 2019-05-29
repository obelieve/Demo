package com.zxy.demo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.zxy.demo.R;

/**
 * Created by zxy on 2018/10/30 10:35.
 */

public class HomeFragment extends Fragment {

    private FrameLayout mFlTop;
    private LinearLayout mLlTab;
    private LinearLayout mLlHeader;
    private RecyclerView mRvContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mFlTop = view.findViewById(R.id.fl_top);
        mLlTab = view.findViewById(R.id.ll_tab);
        mLlHeader = view.findViewById(R.id.ll_header);
        mRvContent = view.findViewById(R.id.rv_content);
        return view;
    }
}
