package com.zxy.frame.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxy.frame.R;
import com.zxy.frame.adapter.func.Func;

import java.util.ArrayList;

public abstract  class BaseQuickRecyclerViewAdapter<DATA, VH extends BaseViewHolder> extends BaseQuickAdapter<DATA, VH>
        implements Func<DATA> {

    private Context mContext;

    public BaseQuickRecyclerViewAdapter(Context context) {
        super(R.layout.viewholder_main,new ArrayList());
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    protected void convert(@NonNull VH helper, DATA item) {
        loadViewHolder(helper,item);
    }


    @Override
    public void setEmptyView(View emptyView) {
        super.setEmptyView(emptyView);
    }

    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener listener, RecyclerView view) {
        super.setOnLoadMoreListener(new RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                listener.onLoadMore();
            }
        }, view);
    }

    @Override
    public void setOnItemClickListener(Func.OnItemClickListener<DATA> listener) {
        super.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                listener.onItemClick(view, getData().get(position), position);
            }
        });
    }

    @Override
    public void setOnItemChildClickListener(Func.OnItemChildClickListener<DATA> listener) {
        super.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                listener.onItemChildClick(view, getData().get(position), position);
            }
        });
    }

    @Override
    public void setOnItemLongClickListener(Func.OnItemLongClickListener<DATA> listener) {
        super.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                return listener.onItemLongClick(view, getData().get(position), position);
            }
        });
    }

    @Override
    public void setOnItemChildLongClickListener(Func.OnItemChildLongClickListener<DATA> listener) {
        super.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                return listener.onItemChildLongClick(view, getData().get(position), position);
            }
        });
    }

}
