package com.zxy.demo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zxy.demo.R;
import com.zxy.demo.adapter.item_decoration.HorizontalItemDivider;
import com.zxy.demo.adapter.item_decoration.VerticalItemDivider;
import com.zxy.demo.adapter.viewholder.DiscoveryChildViewHolder;
import com.zxy.demo.adapter.viewholder.DiscoveryViewHolder;
import com.zxy.demo.base.BaseFragment;
import com.zxy.demo.base.BaseRecyclerViewAdapter;
import com.zxy.demo.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zxy on 2018/10/30 10:37.
 */

public class DiscoveryFragment extends BaseFragment {

    private LinearLayout mLlContent;
    private SwipeRefreshLayout mSrlContent;
    private RecyclerView mRvContent;
    private BaseRecyclerViewAdapter<Integer, DiscoveryViewHolder> mAdapter;

    @Override
    public boolean settingStatusBarLight() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery, container, false);
        mLlContent = view.findViewById(R.id.ll_content);
        mSrlContent = view.findViewById(R.id.srl_content);
        mRvContent = view.findViewById(R.id.rv_content);
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new VerticalItemDivider(true, 20, getResources().getColor(R.color.line_gray2)).dividerToTop(true));
        StatusBarUtil.fitsSystemWindows(mLlContent);
        mSrlContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSrlContent.setRefreshing(false);
            }
        });
        mAdapter = new BaseRecyclerViewAdapter<Integer, DiscoveryViewHolder>() {
            @Override
            public DiscoveryViewHolder getViewHolder(ViewGroup parent, int viewType) {
                return new DiscoveryViewHolder(parent);
            }

            @Override
            public void loadViewHolder(DiscoveryViewHolder holder, int position) {
                BaseRecyclerViewAdapter<Integer, DiscoveryChildViewHolder> adapter = new BaseRecyclerViewAdapter<Integer, DiscoveryChildViewHolder>() {
                    @Override
                    public DiscoveryChildViewHolder getViewHolder(ViewGroup parent, int viewType) {
                        return new DiscoveryChildViewHolder(parent);
                    }

                    @Override
                    public void loadViewHolder(DiscoveryChildViewHolder holder, int position) {

                    }
                };
                adapter.getDataHolder().setList(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)));
                holder.rvContent.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                holder.rvContent.addItemDecoration(new HorizontalItemDivider(true, 4, Color.WHITE));
                holder.rvContent.setAdapter(adapter);
            }
        };
        mRvContent.setAdapter(mAdapter);
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        mAdapter.getDataHolder().setList(list).notifyDataSetChanged();
        return view;
    }
}
