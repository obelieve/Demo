package com.news.mediaplayer.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.news.mediaplayer.R;

/**
 * Created by Admin
 * on 2020/7/13
 */
public class VerticalListSelectViewImpl implements ListSelectView.IListSelectView {

    @Override
    public BaseRecyclerViewAdapter.BaseViewHolder genViewHolder(ViewGroup parent) {
        return new ListViewHolder(parent);
    }

    @Override
    public void select(View view, boolean selected) {
        TextView tv_title = view.findViewById(R.id.tv_title);
        View view_line = view.findViewById(R.id.view_line);
        tv_title.setSelected(selected);
        view_line.setSelected(selected);
    }

    public static class ListViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<ListSelectView.IListSelectViewData> {

        TextView tv_title;
        View view_line;

        public ListViewHolder(ViewGroup parent) {
            super(parent, R.layout.view_vertical_select);
            tv_title = itemView.findViewById(R.id.tv_title);
            view_line = itemView.findViewById(R.id.view_line);
        }

        @Override
        public void bind(ListSelectView.IListSelectViewData data) {
            super.bind(data);
            tv_title.setText(data.getName());
            itemView.setSelected(data.isSelected());
        }
    }
}
