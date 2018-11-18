package com.zxy.demo.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.zxy.demo.R;
import com.zxy.demo.fragment.DiscoverFragment;
import com.zxy.demo.fragment.MeFragment;
import com.zxy.demo.fragment.NewsFragment;
import com.zxy.demo.fragment.WalletFragment;
import com.zxy.demo.view.BottomTabView;
import com.zxy.utility.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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
                LogUtil.e(index+"");
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
            mList.add(new WalletFragment());
            mList.add(new DiscoverFragment());
            mList.add(new NewsFragment());
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

