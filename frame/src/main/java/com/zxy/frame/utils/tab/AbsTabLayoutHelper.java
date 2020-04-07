package com.zxy.frame.utils.tab;

import android.content.Context;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabLayout;

import java.util.List;

public abstract class AbsTabLayoutHelper<T> implements ITabStatus<T> {

    protected ViewPager mViewPager;
    protected TabLayout mTabLayout;
    protected List<T> mList;

    @Override
    public void init(ViewPager viewPager, TabLayout tabLayout, List<T> list, int index) {
        mViewPager = viewPager;
        mTabLayout = tabLayout;
        mList = list;
        if (mViewPager == null || mViewPager.getAdapter() == null || mTabLayout == null || mList == null
                || mViewPager.getAdapter().getCount() != mList.size() || index < 0 || index >= mList.size()) {
            throw new IllegalArgumentException("初始化TabLayout 参数错误！");
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTab(tab, true);
                if (tab.getPosition() >= 0 && tab.getPosition() < mList.size()) {
                    mViewPager.setCurrentItem(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                selectTab(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        for (int i = 0; i < mList.size(); i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            if (null == tab.getCustomView()) {
                tab.setCustomView(getLayoutId());
            }
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tab.getCustomView().getLayoutParams();
            tab.getCustomView().setLayoutParams(layoutParams);
            selectTab(tab, false);
            setData(tab.getCustomView(), mList.get(i));
            tabLayout.addTab(tab, i == index);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TabLayout.Tab tab = mTabLayout.getTabAt(position);
                if (tab != null) {
                    tab.select();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void refreshData() {
        if (mTabLayout != null) {
            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                if (tab != null) {
                    setData(tab.getCustomView(), mList.get(i));
                }
            }
        }
    }
}
