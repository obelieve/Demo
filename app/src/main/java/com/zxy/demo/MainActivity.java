package com.zxy.demo;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 1.RecyclerView怎么根据LayoutManager把，ViewHolder加载进去?
 * 2.怎么复用View？
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_list);
        ButterKnife.bind(this);
        SwipeRefreshLayout srl = findViewById(R.id.srl_content);
        RecyclerView rv = findViewById(R.id.rv_content);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(false);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RecyclerView.Adapter() {
            List<String> list = new ArrayList<>();

            {
                for(int i=0;i<10;i++){
                    list.add("i="+i);
                }
            }
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_main,parent,false));
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ((BaseViewHolder)holder).bind(list.get(position));
            }

            @Override
            public int getItemCount() {
                return list.size();
            }

            class BaseViewHolder extends RecyclerView.ViewHolder{

                public BaseViewHolder(@NonNull View itemView) {
                    super(itemView);

                }

                public void bind(String s){
                    TextView tv = itemView.findViewById(R.id.tv);
                    tv.setText(s);
                }

            }
        });
    }
}
