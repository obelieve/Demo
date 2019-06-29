package com.zxy.demo.adapter.viewholder;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.demo.base.BaseRecyclerViewAdapter;

public class CategoryViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder {

    public final ConstraintLayout clContent;
    public final View viewSelected;
    public final TextView tvTitle;

    public CategoryViewHolder(ViewGroup parent) {
        super(parent, R.layout.viewholder_category);
        clContent = itemView.findViewById(R.id.cl_content);
        viewSelected = itemView.findViewById(R.id.view_selected);
        tvTitle = itemView.findViewById(R.id.tv_title);
    }

}
