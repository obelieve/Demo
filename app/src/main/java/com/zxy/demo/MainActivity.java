package com.zxy.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    String[] mStrings;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tv = (TextView) findViewById(R.id.tv);
        tv.setText(AssetsUtil.getAssetsContent(this,"nest_text"));
        mStrings = getText();
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RecyclerView.Adapter()
        {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                TextView tv = new TextView(MainActivity.this);
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
        final SwipeRefreshLayout srl = (SwipeRefreshLayout) findViewById(R.id.srl);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                Toast.makeText(MainActivity.this, "上课讲话", Toast.LENGTH_SHORT).show();
                srl.setRefreshing(false);
            }
        });
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
