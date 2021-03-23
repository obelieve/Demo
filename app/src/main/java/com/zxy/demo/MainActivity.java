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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        RecyclerView rv = findViewById(R.id.rv_content);
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

    @OnClick(R.id.tv)
    public void onViewClicked() {

    }
}
