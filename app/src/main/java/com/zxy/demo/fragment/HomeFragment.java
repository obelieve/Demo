package com.zxy.demo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxy.demo.R;
import com.zxy.demo.fragment.home.HomeCategory1Fragment;
import com.zxy.demo.fragment.home.HomeCategory2Fragment;
import com.zxy.demo.fragment.home.HomeCategory3Fragment;
import com.zxy.demo.view.HomeTopView;

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

    private HomeTopView mClTop;
    private TabLayout mTlTab;
    private ViewPager mVpContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mClTop = view.findViewById(R.id.cl_top);
        mTlTab = view.findViewById(R.id.tl_tab);
        mVpContent = view.findViewById(R.id.vp_content);
        mVpContent.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment;
                switch (position) {
                    case 0:
                        fragment = new HomeCategory1Fragment();
                        break;
                    case 1:
                        fragment = new HomeCategory2Fragment();
                        break;
                    case 2:
                        fragment = new HomeCategory3Fragment();
                        break;
                    default:
                        fragment = new HomeCategory3Fragment();
                        break;

                }
                return fragment;
            }

            @Override
            public int getCount() {
                return 5;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mStrings[position];
            }
        });
//        for (int i = 0; i < mStrings.length; i++) {
//            mTlTab.addTab(mTlTab.newTab().setText(mStrings[i]));
//        }
        mTlTab.setupWithViewPager(mVpContent);
        return view;
    }
}
