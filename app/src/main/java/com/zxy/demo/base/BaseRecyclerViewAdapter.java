package com.zxy.demo.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxy.demo.R;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T, VH extends BaseRecyclerViewAdapter.BaseViewHolder> extends RecyclerView.Adapter<VH> {

    private BaseDataHolder<T> mDataHolder;
    private OnItemClickCallback<T> mItemClickCallback;
    private OnItemLongClickCallback<T> mItemLongClickCallback;

    public BaseDataHolder<T> getDataHolder() {
        if (mDataHolder == null) {
            mDataHolder = new BaseDataHolder<>(this);
        }
        return mDataHolder;
    }

    public abstract VH getViewHolder(ViewGroup parent, int viewType);

    public abstract void loadViewHolder(VH holder, int position);

    public void setItemClickCallback(OnItemClickCallback<T> itemClickCallback) {
        mItemClickCallback = itemClickCallback;
    }

    public void setItemLongClickCallback(OnItemLongClickCallback<T> itemLongClickCallback) {
        mItemLongClickCallback = itemLongClickCallback;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final int pos = position;
        loadViewHolder(holder, position);
        if (mItemClickCallback != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickCallback.onItemClick(v, getDataHolder().getList().get(pos), pos);
                }
            });
        }
        if (mItemLongClickCallback != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mItemLongClickCallback.onItemLongClick(v, getDataHolder().getList().get(pos), pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return getDataHolder().getList().size();
    }

    public interface OnItemClickCallback<T> {
        void onItemClick(View view, T t, int position);
    }

    public interface OnItemLongClickCallback<T> {
        boolean onItemLongClick(View view, T t, int position);
    }

    private static class aa extends BaseViewHolder {

        private aa(ViewGroup parent) {
            super(parent, R.layout.activity_main);
        }
    }


    public static class BaseDataHolder<T> {

        private List<T> mList = new ArrayList<>();
        private RecyclerView.Adapter mAdapter;

        public BaseDataHolder(RecyclerView.Adapter adapter) {
            mAdapter = adapter;
        }

        public List<T> getList() {
            return mList;
        }

        public BaseDataHolder setList(List<T> list) {
            if (list == null) {
                list = new ArrayList<>();
            }
            mList = list;
            return this;
        }

        public BaseDataHolder addAll(List<T> list) {
            if (list != null) {
                mList.addAll(list);
            }
            return this;
        }

        public void notifyItemChanged(int position) {
            mAdapter.notifyItemChanged(position);
        }

        public void notifyDataSetChanged() {
            mAdapter.notifyDataSetChanged();
        }
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(ViewGroup parent, int layoutId) {
            super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        }
    }


}
