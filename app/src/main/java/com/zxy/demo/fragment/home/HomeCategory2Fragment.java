package com.zxy.demo.fragment.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zxy.demo.R;
import com.zxy.demo.adapter.HomeCategory2Adapter;
import com.zxy.demo.base.BaseFragment;
import com.zxy.demo.view.HomeSwipeRefreshLayout;
import com.zxy.demo.view.HomeTopView;
import com.zxy.utility.LogUtil;

public class HomeCategory2Fragment extends BaseFragment {

    private HomeSwipeRefreshLayout mSrlContent;
    private RecyclerView mRvContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_category2, container, false);
        mRvContent = view.findViewById(R.id.rv_content);
        mSrlContent = view.findViewById(R.id.srl_content);
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.setAdapter(new HomeCategory2Adapter());
        mSrlContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSrlContent.setRefreshing(false);
                    }
                },1000);
            }
        });
        return view;

    }
}
