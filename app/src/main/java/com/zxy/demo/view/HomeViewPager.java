package com.zxy.demo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


public class HomeViewPager extends ViewPager {

    public HomeViewPager(@NonNull Context context) {
        super(context);
    }

    public HomeViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public static abstract class HomeAdapter extends FragmentStatePagerAdapter {

        public HomeAdapter(FragmentManager fm) {
            super(fm);
        }

        public abstract RecyclerView getContentView(int position);
    }


    public void setAdapter(@Nullable HomeAdapter adapter) {
        super.setAdapter(adapter);
    }

    public HomeAdapter getAdapter() {
        PagerAdapter adapter = super.getAdapter();
        return adapter instanceof HomeAdapter ? (HomeAdapter) adapter : null;
    }
}
