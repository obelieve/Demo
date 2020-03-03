package com.zxy.demo;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.tabs.TabLayout;
import com.zxy.utility.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView mTv;
    @BindView(R.id.view_community_tab)
    CommunityTabView mCommunityTabView;
    @BindView(R.id.tl_google)
    TabLayout tlGoogle;
    @BindView(R.id.tl_flyco_sliding)
    SlidingTabLayout tlFlycoSliding;
    @BindView(R.id.tl_flyco_common)
    CommonTabLayout tlFlycoCommon;
    @BindView(R.id.tl_flyco_segment)
    SegmentTabLayout tlFlycoSegment;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    Class[] mClasses = new Class[]{Fragment1.class,Fragment2.class};
    String[] mTitles = new String[]{"精选","广场"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        vpContent.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                try {
                    return (Fragment) mClasses[position].newInstance();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
                return new Fragment();
            }

            @Override
            public int getCount() {
                return mClasses.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        });
        initCustomTabView();
        initFlycoSliding();
        initGoogleTab();
    }

    private void initCustomTabView() {
        mCommunityTabView.setupViewPager(vpContent);
    }

    private void initGoogleTab() {
        tlGoogle.setTabRippleColor(ColorStateList.valueOf(getResources().getColor(android.R.color.transparent)));
        tlGoogle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.tab_custom_view);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextColor(Color.parseColor("#37BD42"));
                LogUtil.e("h="+textView.getHeight()+" w:"+textView.getWidth()+" size="+textView.getTextSize());
                textView.setTextAppearance(MainActivity.this, R.style.TabLayoutTextSize_bold_23sp);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.tab_custom_view);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextColor(Color.parseColor("#333333"));
                LogUtil.e("un h="+textView.getHeight()+" w:"+textView.getWidth()+" size="+textView.getTextSize());
                textView.setTextAppearance(MainActivity.this, R.style.TabLayoutTextSize_normal_17sp);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tlGoogle.setupWithViewPager(vpContent);
    }


    private void initFlycoSliding() {
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.e();
                for (int i = 0; i < mTitles.length; i++) {
                    if (i == position) {
                        tlFlycoSliding.getTitleView(i).setTextSize(23);
                    } else {
                        tlFlycoSliding.getTitleView(i).setTextSize(17);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tlFlycoSliding.setViewPager(vpContent, mTitles);
        tlFlycoSliding.getTitleView(0).setTextSize(23);
    }

    @OnClick(R.id.tv)
    public void onViewClicked() {

    }
}
