package com.zxy.demo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class HomeViewPager extends ViewPager {

    public HomeViewPager(@NonNull Context context) {
        super(context);
    }

    public HomeViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public static abstract class HomeAdapter extends FragmentStatePagerAdapter {

        protected HomeAdapter(FragmentManager fm) {
            super(fm);
        }

        public abstract RecyclerView getContentView(int position);

        protected ArrayList<Fragment> getFragments() {
            ArrayList<Fragment> list = new ArrayList<>();
            Field field = null;
            ArrayList temp = null;
            try {
                field = FragmentStatePagerAdapter.class.getDeclaredField("mFragments");
                field.setAccessible(true);
                temp = (ArrayList) field.get(this);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (temp != null) {
                for (int i = 0; i < temp.size(); i++) {
                    Object obj = temp.get(i);
                    if (obj instanceof Fragment) {
                        list.add((Fragment) obj);
                    } else {
                        return null;
                    }
                }
            }
            return list;
        }
    }


    public void setAdapter(@Nullable HomeAdapter adapter) {
        super.setAdapter(adapter);
    }

    public HomeAdapter getAdapter() {
        PagerAdapter adapter = super.getAdapter();
        return adapter instanceof HomeAdapter ? (HomeAdapter) adapter : null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(heightMeasureSpec) + HomeTopView.getDragHeight(), MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } catch (Exception e) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
