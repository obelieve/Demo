package com.zxy.demo;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Case1Activity extends Activity {

    String[] images= new String[]{
            "https://imagebucket-yiyi.s3-accelerate.amazonaws.com/upload/2021-04-19/dZYUZ4lJxqhXusd2SIa8.jpg",
            "https://imagebucket-yiyi.s3-accelerate.amazonaws.com/upload/2021-04-19/WudkAphLJXESFih13Kbb.jpg"};

    Case1Adapter mCase1Adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_list);
        ImageView iv2 = findViewById(R.id.iv2);
        mCase1Adapter = new Case1Adapter();
        RecyclerView rv = findViewById(R.id.rv_content);
        SwipeRefreshLayout srl = findViewById(R.id.srl_content);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(false);
                mCase1Adapter.setList(Arrays.asList(images));

            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mCase1Adapter);
        mCase1Adapter.setList(Arrays.asList(images));
    }
    
    static class Case1Adapter extends RecyclerView.Adapter<Case1Adapter.ItemViewHolder>{

        private List<String> mList = new ArrayList<>();

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_case1,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull Case1Activity.Case1Adapter.ItemViewHolder holder, int position) {
            holder.bind(mList.get(position));
        }


        public void setList(List<String> list) {
            this.mList = list;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
        
        class ItemViewHolder extends RecyclerView.ViewHolder{

            ImageView imageView;

            public ItemViewHolder(@NonNull  View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.iv);

            }

            public void bind(String data){
                //Glide placeholder高度被限制
                Glide.with(itemView).load(data).placeholder(R.drawable.ic_load_loading).error(R.drawable.ic_load_loading).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.e("Case1Activity","width="+resource.getIntrinsicWidth()+" height="+resource.getIntrinsicHeight());
                        imageView.setImageDrawable(resource);
                        return true;
                    }
                }).into(imageView);
            }
        }
    }
}
