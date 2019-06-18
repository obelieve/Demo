package com.zxy.demo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zxy.demo.R;
import com.zxy.demo.base.BaseActivity;
import com.zxy.demo.fragment.CategoryFragment;
import com.zxy.demo.fragment.MeFragment;
import com.zxy.demo.fragment.ShoppingCartFragment;
import com.zxy.demo.fragment.DiscoveryFragment;
import com.zxy.demo.fragment.HomeFragment;
import com.zxy.demo.view.BottomTabView;
import com.zxy.utility.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    ViewPager vp_content;
    BottomTabView view_bottom_tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vp_content = findViewById(R.id.vp_content);
        view_bottom_tab = findViewById(R.id.view_bottom_tab);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        vp_content.setAdapter(adapter);
        view_bottom_tab.setCallback(new BottomTabView.Callback() {
            @Override
            public void onSelected(int index, View view) {
                vp_content.setCurrentItem(index);
            }
        });
        vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                view_bottom_tab.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> mList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            mList.add(new HomeFragment());
            mList.add(new CategoryFragment());
            mList.add(new DiscoveryFragment());
            mList.add(new ShoppingCartFragment());
            mList.add(new MeFragment());
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }
}

