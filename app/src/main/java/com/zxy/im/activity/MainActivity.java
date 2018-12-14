package com.zxy.im.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.zxy.im.R;
import com.zxy.im.appview.ContactsView;
import com.zxy.im.appview.ConversationListView;
import com.zxy.im.appview.DiscoveryView;
import com.zxy.im.appview.MyView;
import com.zxy.im.base.BaseActivity;
import com.zxy.im.view.BottomTabView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
{
    @BindView(R.id.view_bottom_tab)
    BottomTabView mViewBottomTab;
    @BindView(R.id.vp_content)
    ViewPager mVpContent;

    PagerAdapter mPagerAdapter;
    List<View> mViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        ButterKnife.bind(this);
        mVpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                mViewBottomTab.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
        mViewBottomTab.setCallback(new BottomTabView.Callback()
        {
            @Override
            public void onSelected(int index, View view)
            {
                mVpContent.setCurrentItem(index);
            }
        });
        mViews.add(initConversationListView());
        mViews.add(new ContactsView(this));
        mViews.add(new DiscoveryView(this));
        mViews.add(new MyView(this));
        mPagerAdapter = new MainPagerAdapter(mViews);
        mVpContent.setAdapter(mPagerAdapter);
        mVpContent.setCurrentItem(0);
    }

    private View initConversationListView()
    {
        return new ConversationListView(this);
    }

    /**
     * 首页适配器
     */
    public static class MainPagerAdapter extends PagerAdapter
    {

        private List<View> mViews;

        public MainPagerAdapter(List<View> list)
        {
            mViews = list;
        }

        @Override
        public int getCount()
        {
            if (mViews != null)
                return mViews.size();
            return 0;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position)
        {
            View view = mViews.get(position);
            container.addView(view);
            return view;
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
