package com.zxy.demo.adapter.viewholder;

import com.zxy.demo.databinding.ViewholderLoadRefreshBinding;
import com.zxy.demo.entity.SquarePostEntity;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;


public class LoadRefreshViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<SquarePostEntity.PostListBean,ViewholderLoadRefreshBinding> {

    public LoadRefreshViewHolder(ViewholderLoadRefreshBinding viewBinding) {
        super(viewBinding);
    }
}
