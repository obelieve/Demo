package com.zxy.demo.adapter.viewholder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder {
    @BindView(R.id.tv_name)
    public TextView mTvName;
    @BindView(R.id.tv_content)
    public TextView mTvContent;
    @BindView(R.id.iv_image)
    public ImageView mIvImage;

    public MainViewHolder(ViewGroup parent, int layoutId) {
        super(parent, R.layout.viewholder_main);
        ButterKnife.bind(this,itemView);
    }
}
