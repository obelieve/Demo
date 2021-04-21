package com.zxy.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.zxy.utility.SystemUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Glide：placeholder显示高度问题
 */
public class Case1Activity extends Activity {

    String[] images = new String[]{
            "https://imagebucket-yiyi.s3-accelerate.amazonaws.com/upload/2021-04-19/dZYUZ4lJxqhXusd2SIa8.jpg",
            "https://imagebucket-yiyi.s3-accelerate.amazonaws.com/upload/2021-04-19/WudkAphLJXESFih13Kbb.jpg"};

    LLAdapter mLLAdapter;

    LinearLayout mLLContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_list);
        ImageView iv2 = findViewById(R.id.iv2);
        mLLContent = findViewById(R.id.ll_content);
        SwipeRefreshLayout srl = findViewById(R.id.srl_content);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(false);

            }
        });
        mLLAdapter = new LLAdapter(this,mLLContent);
        mLLAdapter.setList(Arrays.asList(images));
    }


    static class LLAdapter {
        private Activity mActivity;
        private LinearLayout mLL;

        public LLAdapter(Activity activity, LinearLayout ll) {
            mActivity = activity;
            mLL = ll;
            SystemUtil.init(mActivity);
        }

        public void setList(List<String> list) {
            mLL.removeAllViews();
            for (int i = 0; i < list.size(); i++) {
                ImageView iv = new ImageView(mLL.getContext());
                iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                mLL.addView(iv);
                Glide.with(mActivity).load(list.get(i))

//                        .listener(new RequestListener<Drawable>() {
//                    int width = SystemUtil.screenWidth() - SystemUtil.dp2px(30);
//
//                    @Override
//                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        iv.setImageResource(R.drawable.ic_load_loading);
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        float scale = resource.getIntrinsicWidth()/(width * 1.0f);
//                        int height = (int) (resource.getIntrinsicHeight()/scale);
//                        iv.setLayoutParams(new LinearLayout.LayoutParams(width, height));
//                        iv.setImageDrawable(resource);
//                        return true;
//                    }
//                })
//                        .placeholder(R.drawable.ic_load_loading)
                        .error(R.drawable.ic_load_loading).into(iv);
            }
        }
    }
}