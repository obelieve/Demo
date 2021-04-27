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

import com.zxy.utility.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 1.RecyclerView怎么根据LayoutManager把，ViewHolder加载进去?
 * 2.怎么复用View？
 * <p>
 * Update RecyclerView布局流程：
 * 从->onMeasure()/onLayout()
 * ->dispatchLayout()
 * ｛
 * dispatchLayoutStep1(); //一些初始化操作，记录状态
 * dispatchLayoutStep2();
 * ｛
 * #mLayoutManager.onLayoutChildren(mRecycler, mState);
 * {
 * fill(recycler, mLayoutState, state, false);//*测量和布局
 * //有一个while轮询语句块
 * }
 * ｝
 * dispatchLayoutStep3();
 * ｝
 **/
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
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        rv.setAdapter(new RecyclerView.Adapter() {
            List<String> list = new ArrayList<>();

            {
                for (int i = 0; i < 30; i++) {
                    list.add("i=" + i);
                }
            }
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_main, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ((BaseViewHolder) holder).bind(list.get(position),position);
            }

            @Override
            public int getItemCount() {
                return list.size();
            }

            class BaseViewHolder extends RecyclerView.ViewHolder {

                public BaseViewHolder(@NonNull View itemView) {
                    super(itemView);
                    LogUtil.e("BaseViewHolder 创建 " + this);
                }

                public void bind(String s, int pos) {
                    LogUtil.e("BaseViewHolder pos = " + pos + " 绑定 " + this);
                    TextView tv = itemView.findViewById(R.id.tv);
                    tv.setText(s);
                }

            }
        });
    }
}
