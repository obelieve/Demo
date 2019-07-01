package com.zxy.demo.adapter.viewholder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.demo.base.BaseRecyclerViewAdapter;
import com.zxy.demo.view.ShoppingCartView;

public class GoodsTypeViewHolder0 extends BaseRecyclerViewAdapter.BaseViewHolder {

    private ImageView mIvImg;
    private TextView mTvTitle;
    private TextView mTvContent;
    private TextView mTvLabel;
    private TextView mTvWeight;
    private TextView mTvCurPrice;
    private TextView mTvOriPrice;
    private ShoppingCartView mViewShopping;

    public GoodsTypeViewHolder0(ViewGroup parent) {
        super(parent, R.layout.viewholder_goods_type0);
        mIvImg = itemView.findViewById(R.id.iv_img);
        mTvTitle = itemView.findViewById(R.id.tv_title);
        mTvContent = itemView.findViewById(R.id.tv_content);
        mTvLabel = itemView.findViewById(R.id.tv_label);
        mTvWeight = itemView.findViewById(R.id.tv_weight);
        mTvCurPrice = itemView.findViewById(R.id.tv_cur_price);
        mTvOriPrice = itemView.findViewById(R.id.tv_ori_price);
        mViewShopping = itemView.findViewById(R.id.view_shopping);
    }
}
