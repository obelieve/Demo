package com.zxy.frame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zxy.demo.R;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<DATA, VH extends BaseRecyclerViewAdapter.BaseViewHolder> extends RecyclerView.Adapter<VH> {

    private Context mContext;

    private BaseDataHolder<DATA> mDataHolder;
    private OnItemClickCallback<DATA> mItemClickCallback;
    private OnItemLongClickCallback<DATA> mItemLongClickCallback;

    public BaseRecyclerViewAdapter(Context context){
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public BaseDataHolder<DATA> getDataHolder() {
        if (mDataHolder == null) {
            mDataHolder = new BaseDataHolder<>(this);
        }
        return mDataHolder;
    }

    public abstract VH getViewHolder(ViewGroup parent, int viewDATAype);

    public abstract void loadViewHolder(VH holder, int position);

    public void setItemClickCallback(OnItemClickCallback<DATA> itemClickCallback) {
        mItemClickCallback = itemClickCallback;
    }

    public void setItemLongClickCallback(OnItemLongClickCallback<DATA> itemLongClickCallback) {
        mItemLongClickCallback = itemLongClickCallback;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewDATAype) {
        return getViewHolder(parent, viewDATAype);
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

    public interface OnItemClickCallback<DATA> {
        void onItemClick(View view, DATA t, int position);
    }

    public interface OnItemLongClickCallback<DATA> {
        boolean onItemLongClick(View view, DATA t, int position);
    }

    private static class aa extends BaseViewHolder {

        private aa(ViewGroup parent) {
            super(parent, R.layout.activity_main);
        }
    }


    public static class BaseDataHolder<DATA> {

        private List<DATA> mList = new ArrayList<>();
        private RecyclerView.Adapter mAdapter;

        public BaseDataHolder(RecyclerView.Adapter adapter) {
            mAdapter = adapter;
        }

        public List<DATA> getList() {
            return mList;
        }

        public BaseDataHolder setList(List<DATA> list) {
            if (list == null) {
                list = new ArrayList<>();
            }
            mList = list;
            return this;
        }

        public BaseDataHolder addAll(List<DATA> list) {
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
