package com.zxy.demo.adapter.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.demo.base.BaseRecyclerViewAdapter;

public class CategoryPagerViewHolder1 extends BaseRecyclerViewAdapter.BaseViewHolder {

    public final ImageView ivContent;
    public final TextView tvContent;

    public CategoryPagerViewHolder1(ViewGroup parent) {
        super(parent, R.layout.viewholder_category_pager1);
        ivContent = itemView.findViewById(R.id.iv_content);
        tvContent = itemView.findViewById(R.id.tv_content);
    }
}
