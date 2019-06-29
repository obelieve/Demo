package com.zxy.demo.adapter.viewholder;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.zxy.demo.R;
import com.zxy.demo.base.BaseRecyclerViewAdapter;

public class CategoryPagerViewHolder0 extends BaseRecyclerViewAdapter.BaseViewHolder {

    public final ImageView ivContent;

    public CategoryPagerViewHolder0(ViewGroup parent) {
        super(parent, R.layout.viewholder_category_pager0);
        ivContent = itemView.findViewById(R.id.iv_content);
    }

}
