package com.zxy.demo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.demo.base.SimpleRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class TextRecyclerViewAdapter extends SimpleRecyclerViewAdapter {

    private List<String> mList  = new ArrayList<>();

    public void setList(List<String> list) {
        if(list!=null)
            mList = list;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getSimpleLayoutId() {
        return R.layout.viewholder_base;
    }

    @Override
    public RecyclerView.ViewHolder getSimpleViewHolder(View view) {
        return new RecyclerView.ViewHolder(view) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
    }

    @Override
    public void onSimpleBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView tv = holder.itemView.findViewById(R.id.tv);
        tv.setText(mList.get(position));
    }
}
