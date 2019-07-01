package com.zxy.demo.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zxy.demo.R;
import com.zxy.demo.base.BaseRecyclerViewAdapter;

public class DiscoveryViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder {

    public final ImageView ivContent;
    public final RecyclerView rvContent;

    public DiscoveryViewHolder(ViewGroup parent) {
        super(parent, R.layout.viewholder_discovery);
        ivContent = itemView.findViewById(R.id.iv_content);
        rvContent = itemView.findViewById(R.id.rv_content);
    }
}
