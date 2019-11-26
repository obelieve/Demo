package com.zxy.frame.adapter.func;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;

public interface Func<DATA> {

    void loadViewHolder(BaseViewHolder holder, DATA data);

    void setEmptyView(View view);

    void setOnLoadMoreListener(OnLoadMoreListener listener, RecyclerView view);

    void setOnItemClickListener(OnItemClickListener<DATA> listener);

    void setOnItemChildClickListener(OnItemChildClickListener<DATA> listener);

    void setOnItemLongClickListener(OnItemLongClickListener<DATA> listener);

    void setOnItemChildLongClickListener(OnItemChildLongClickListener<DATA> listener);

    public interface OnItemChildClickListener<DATA> {
        void onItemChildClick(View view, DATA t, int position);
    }

    public interface OnItemClickListener<DATA> {
        void onItemClick(View view, DATA t, int position);
    }

    public interface OnItemLongClickListener<DATA> {
        boolean onItemLongClick(View view, DATA t, int position);
    }

    public interface OnItemChildLongClickListener<DATA> {
        boolean onItemChildLongClick(View view, DATA t, int position);
    }

    public interface OnLoadMoreListener {

        void onLoadMore();
    }
}
