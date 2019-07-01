package com.zxy.demo.adapter.viewholder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.demo.base.BaseRecyclerViewAdapter;
import com.zxy.demo.view.ShoppingCartView;

public class GoodsTypeViewHolder1 extends BaseRecyclerViewAdapter.BaseViewHolder {


    private TextView mTvLabel;
    private ImageView mIvImg;
    private TextView mTvTitle;
    private TextView mTvCurPrice;
    private TextView mTvOriPrice;
    private ShoppingCartView mViewShopping;

    public GoodsTypeViewHolder1(ViewGroup parent) {
        super(parent, R.layout.viewholder_goods_type1);
        mTvLabel = itemView.findViewById(R.id.tv_label);
        mIvImg = itemView.findViewById(R.id.iv_img);
        mTvTitle = itemView.findViewById(R.id.tv_title);
        mTvCurPrice = itemView.findViewById(R.id.tv_cur_price);
        mTvOriPrice = itemView.findViewById(R.id.tv_ori_price);
        mViewShopping = itemView.findViewById(R.id.view_shopping);
    }
}
