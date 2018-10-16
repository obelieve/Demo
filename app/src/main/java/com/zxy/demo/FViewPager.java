package com.zxy.demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zxy on 2018/10/15 11:16.
 */

public class FViewPager extends FrameLayout
{
    ViewPager vp;
    LinearLayout ll;

    int mCurPosition;

    public FViewPager(@NonNull Context context)
    {
        super(context);
    }

    public FViewPager(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    {
        init();
    }

    public void init()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.f_view_pager, this, true);
        vp = (ViewPager) findViewById(R.id.vp);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DeviceInfoUtil.dpToPx(100));
        vp.setLayoutParams(params);
        ll = (LinearLayout) findViewById(R.id.ll);
        vp.setAdapter(new FPageAdapter());
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                ll.getChildAt(mCurPosition).setBackgroundResource(R.drawable.ic_page_normal);
                ll.getChildAt(position).setBackgroundResource(R.drawable.ic_page_select);
                mCurPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
        for (int i = 0; i < 3; i++)
        {
            ImageView iv = new ImageView(getContext());
            LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            childParams.setMargins(DeviceInfoUtil.dpToPx(10), 0, 0, 0);
            iv.setLayoutParams(childParams);
            iv.setBackgroundResource(R.drawable.ic_page_normal);
            ll.addView(iv);
        }

        vp.setCurrentItem(mCurPosition);
        ll.getChildAt(mCurPosition).setBackgroundResource(R.drawable.ic_page_select);
    }


    public static class FPageAdapter extends PagerAdapter
    {
        private List<Integer> list = new ArrayList<>();

        public FPageAdapter()
        {
            list.addAll(Arrays.asList(R.drawable.p1, R.drawable.p2, R.drawable.p3));
        }

        @Override
        public int getCount()
        {
            return list.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position)
        {
            ImageView iv = (ImageView) LayoutInflater.from(container.getContext()).inflate(R.layout.pager_item, container, false);
            iv.setImageResource(list.get(position));
            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object)
        {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object)
        {
            return view == object;
        }
    }
}
