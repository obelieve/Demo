package com.zxy.demo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.zxy.demo.R;
import com.zxy.demo.adapter.item_decoration.HorizontalItemDivider;
import com.zxy.demo.adapter.item_decoration.VerticalItemDivider;
import com.zxy.demo.adapter.viewholder.GoodsTypeViewHolder0;
import com.zxy.demo.adapter.viewholder.GoodsTypeViewHolder1;
import com.zxy.demo.adapter.viewholder.ShoppingCartDividerTypeViewHolder0;
import com.zxy.demo.base.BaseFragment;
import com.zxy.demo.base.BaseRecyclerViewAdapter;
import com.zxy.demo.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by zxy on 2018/10/30 10:38.
 */

public class ShoppingCartFragment extends BaseFragment {

    private FrameLayout mFlTitle;
    private LinearLayout mLlEmpty;
    private RecyclerView mRvContent;

    private BaseRecyclerViewAdapter<Integer, BaseRecyclerViewAdapter.BaseViewHolder> mAdapter;

    @Override
    public boolean settingStatusBarLight() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        mFlTitle = view.findViewById(R.id.fl_title);
        mLlEmpty = view.findViewById(R.id.ll_empty);
        mRvContent = view.findViewById(R.id.rv_content);
        StatusBarUtil.fitsSystemWindows(mFlTitle);
        mRvContent.addItemDecoration(new VerticalItemDivider(getResources().getColor(R.color.line_gray2)));
        mRvContent.addItemDecoration(new HorizontalItemDivider(getResources().getColor(R.color.line_gray2)));
        mAdapter = new BaseRecyclerViewAdapter<Integer, BaseRecyclerViewAdapter.BaseViewHolder>() {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder viewHolder = null;
                switch (viewType) {
                    case 0:
                        viewHolder = new GoodsTypeViewHolder0(parent);
                        break;
                    case 1:
                        viewHolder = new GoodsTypeViewHolder1(parent);
                        break;
                    case 2:
                    default:
                        viewHolder = new ShoppingCartDividerTypeViewHolder0(parent);
                        break;
                }
                return viewHolder;
            }

            @Override
            public void loadViewHolder(BaseViewHolder holder, int position) {

            }

            @Override
            public int getItemViewType(int position) {
                int value = mAdapter.getDataHolder().getList().get(position);
                int type = 0;
                switch (value) {
                    case Integer.MIN_VALUE:
                        type = 1;
                        break;
                    case Integer.MAX_VALUE:
                        type = 2;
                        break;
                }
                return type;
            }
        };
        mAdapter.getDataHolder().setList(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE)));
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position < 6 ? 2 : 1;
            }
        });
        mRvContent.setLayoutManager(manager);
        mRvContent.setAdapter(mAdapter);
        return view;
    }
}
