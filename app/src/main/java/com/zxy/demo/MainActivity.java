package com.zxy.demo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.zxy.demo.fragment.MainFragment;
import com.zxy.frame.base.BaseActivity;
import com.zxy.frame.view.BottomTabView;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tl_tab)
    TabLayout mTlTab;
    @BindView(R.id.vp_content)
    ViewPager mVpContent;

    @BindView(R.id.view_bottom_tab)
    BottomTabView mBottomTabView;

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        mVpContent.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {


            @Override
            public Fragment getItem(int position) {
                Fragment fragment;
                try {
                    fragment = (Fragment) MainEnum.values()[position].getClazz().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    fragment = new MainFragment();
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return MainEnum.values().length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return MainEnum.values()[position].getName();
            }
        });
        mTlTab.setupWithViewPager(mVpContent);
        mBottomTabView.setupWithViewPager(mVpContent);
    }

}



