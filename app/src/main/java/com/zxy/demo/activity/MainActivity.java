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

import com.zxy.demo.utils.BottomNavigationViewHelper;
import com.zxy.demo.R;
import com.zxy.demo.fragment.DiscoverFragment;
import com.zxy.demo.fragment.MeFragment;
import com.zxy.demo.fragment.NewsFragment;
import com.zxy.demo.fragment.WalletFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    ViewPager vp_content;
    MenuItem menuItem;
    BottomNavigationView bnv_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vp_content = findViewById(R.id.vp_content);
        bnv_bottom = findViewById(R.id.bnv_bottom);
        BottomNavigationViewHelper.disableShiftMode(bnv_bottom);
        bnv_bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.nav_wallet:
                        vp_content.setCurrentItem(0);
                        break;
                    case R.id.nav_discover:
                        vp_content.setCurrentItem(1);
                        break;
                    case R.id.nav_news:
                        vp_content.setCurrentItem(2);
                        break;
                    case R.id.nav_me:
                        vp_content.setCurrentItem(3);
                        break;
                }
                return false;
            }
        });
        vp_content.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
        vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                if (menuItem != null)
                {
                    menuItem.setChecked(false);
                } else
                {
                    bnv_bottom.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bnv_bottom.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        vp_content.setAdapter(adapter);
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter
    {

        List<Fragment> mList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm)
        {
            super(fm);
            mList.add(new WalletFragment());
            mList.add(new DiscoverFragment());
            mList.add(new NewsFragment());
            mList.add(new MeFragment());
        }

        @Override
        public Fragment getItem(int position)
        {
            return mList.get(position);
        }

        @Override
        public int getCount()
        {
            return mList.size();
        }
    }
}

