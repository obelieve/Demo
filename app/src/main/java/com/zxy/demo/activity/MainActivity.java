package com.zxy.demo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zxy.demo.R;
import com.zxy.demo.base.BaseActivity;
import com.zxy.demo.base.BaseFragment;
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
    ViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vp_content = findViewById(R.id.vp_content);
        view_bottom_tab = findViewById(R.id.view_bottom_tab);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        vp_content.setAdapter(mAdapter);
        view_bottom_tab.setCallback(new BottomTabView.Callback() {
            @Override
            public void onSelected(int index, View view) {
                vp_content.setCurrentItem(index);
            }
        });
        vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Fragment fragment = mAdapter.getItem(position);
                if (fragment instanceof BaseFragment) {
                    BaseFragment fragment1 = (BaseFragment) fragment;
                    configStatusBar(fragment1.settingStatusBarLight(), fragment1.settingStatusBarColor());
                }
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

