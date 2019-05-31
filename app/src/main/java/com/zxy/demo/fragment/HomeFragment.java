package com.zxy.demo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TableLayout;

import com.zxy.demo.R;
import com.zxy.demo.adapter.TextRecyclerViewAdapter;
import com.zxy.demo.mock_data.GoodsData;

/**
 * Created by zxy on 2018/10/30 10:35.
 */

public class HomeFragment extends Fragment {

    private final String[] mStrings = new String[]{
            "推荐", "掌柜推荐", "限时抢购", "特价专区", "人气热卖",
            "本周更新", "水果", "肉禽蛋", "海鲜水产", "粮油调味",
            "熟食卤味", "面点冰品", "牛奶面包", "酒水冲饮", "休闲零食",
            "日用清洁", "护理美妆", "进口商品", "鲜花礼品", "地方特产",
            "母婴", "宠物"};
    private FrameLayout mFlTop;
    private ConstraintLayout mClTop;
    private TabLayout mTlTab;
    private RecyclerView mRvContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mFlTop = view.findViewById(R.id.fl_top);
        mClTop = view.findViewById(R.id.cl_top);
        mTlTab = view.findViewById(R.id.tl_tab);
        mRvContent = view.findViewById(R.id.rv_content);
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        TextRecyclerViewAdapter adapter = new TextRecyclerViewAdapter();
        adapter.setList(GoodsData.RandomString.getRandomList());
        mRvContent.setAdapter(adapter);
        for (int i = 0; i < mStrings.length; i++) {
            mTlTab.addTab(mTlTab.newTab().setText(mStrings[i]));
        }
        return view;
    }
}
