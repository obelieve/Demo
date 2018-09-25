package com.zxy.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by zxy on 2018/9/25 10:14.
 */

public class NestedFragment extends Fragment
{
    String[] mStrings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_nested,container,false);

        final TextView tv = (TextView) view.findViewById(R.id.tv);
        tv.setText(AssetsUtil.getAssetsContent(getContext(),"nest_text"));
        mStrings = getText();
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new RecyclerView.Adapter()
        {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                TextView tv = new TextView(getContext());
                return new StickyHolder(tv);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
            {
                StickyHolder holder1 = (StickyHolder)holder;
                holder1.tv.setText(mStrings[position]);
            }

            @Override
            public int getItemCount()
            {
                return mStrings.length;
            }
        });
        final SwipeRefreshLayout srl = (SwipeRefreshLayout)view.findViewById(R.id.srl);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                Toast.makeText(getContext(), "上课讲话", Toast.LENGTH_SHORT).show();
                srl.setRefreshing(false);
            }
        });

        return view;
    }


public static class StickyHolder extends RecyclerView.ViewHolder{
    TextView tv;
    public StickyHolder(View itemView)
    {
        super(itemView);
        tv = (TextView)itemView;
    }
}

    public String[] getText()
    {
        String[] strings = new String[100];
        for (int i = 0; i < 100; i++)
        {
            strings[i] = "i:" + i;
        }
        return strings;
    }
}
