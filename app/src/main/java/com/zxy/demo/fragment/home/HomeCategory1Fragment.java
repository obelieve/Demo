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

import com.zxy.demo.R;
import com.zxy.demo.adapter.TextRecyclerViewAdapter;
import com.zxy.demo.base.BaseFragment;
import com.zxy.demo.mock_data.GoodsData;

public class HomeCategory1Fragment extends BaseFragment {

    SwipeRefreshLayout mSrlContent;
    RecyclerView mRvContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_category1,container,false);
        mSrlContent = view.findViewById(R.id.srl_content);
        mRvContent = view.findViewById(R.id.rv_content);
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        TextRecyclerViewAdapter adapter = new TextRecyclerViewAdapter();
        adapter.setList(GoodsData.RandomString.getRandomList());
        mRvContent.setAdapter(adapter);
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
