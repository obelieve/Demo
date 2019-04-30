package com.zxy.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * CoordinatorLayout
 * 1.super FrameLayout
 * 2.是容器，子View间交互效果
 * --
 * Behavior 交互行为
 * anchor 固定(非自己子view和非子view后裔)
 * insetEdge dogInsetEdges
 * --
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView mMyList;


//    private ViewPager mVpContent;
//    private FrameLayout mFlHeadHide;
//    private TextView mTvHeadSearch;
//    private TabLayout mTlHeadTab;
//    private LinearLayout mLlHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mVpContent = findViewById(R.id.vp_content);
//        mFlHeadHide = findViewById(R.id.fl_head_hide);
//        mTvHeadSearch = findViewById(R.id.tv_head_search);
//        mTlHeadTab = findViewById(R.id.tl_head_tab);
//        mLlHead = findViewById(R.id.ll_head);
        mMyList = findViewById(R.id.my_list);
        mMyList.setLayoutManager(new LinearLayoutManager(this));
        mMyList.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                TextView view = new TextView(parent.getContext());
                return new RecyclerView.ViewHolder(view) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                TextView tv = (TextView) holder.itemView;
                tv.setText("position:" + position);
            }

            @Override
            public int getItemCount() {
                return 100;
            }
        });
    }

}
