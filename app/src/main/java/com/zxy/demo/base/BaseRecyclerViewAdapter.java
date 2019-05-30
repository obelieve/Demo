package com.zxy.demo.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxy.demo.R;

public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter {


    public abstract int getBaseLayoutId();

    public abstract RecyclerView.ViewHolder getBaseViewHolder(View view);

    public abstract void onBaseBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getBaseLayoutId(), parent, false);
        return getBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        onBaseBindViewHolder(holder,position);
    }
}
