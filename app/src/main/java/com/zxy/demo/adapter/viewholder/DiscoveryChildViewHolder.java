package com.zxy.demo.adapter.viewholder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.demo.base.BaseRecyclerViewAdapter;

public class DiscoveryChildViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder {

    public final ImageView ivImg;
    public final TextView tvTitle;
    public final TextView tvCurPrice;

    public DiscoveryChildViewHolder(ViewGroup parent) {
        super(parent, R.layout.viewholder_discovery_child);
        ivImg = itemView.findViewById(R.id.iv_img);
        tvTitle = itemView.findViewById(R.id.tv_title);
        tvCurPrice = itemView.findViewById(R.id.tv_cur_price);
    }
}
