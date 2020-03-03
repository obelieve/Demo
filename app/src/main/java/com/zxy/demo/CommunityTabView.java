package com.zxy.demo;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommunityTabView extends FrameLayout {


    @BindView(R.id.tv_title1)
    TextView tvTitle1;
    @BindView(R.id.view_indicator1)
    View viewIndicator1;
    @BindView(R.id.ll_tab1)
    LinearLayout llTab1;
    @BindView(R.id.tv_title2)
    TextView tvTitle2;
    @BindView(R.id.view_indicator2)
    View viewIndicator2;
    @BindView(R.id.ll_tab2)
    LinearLayout llTab2;

    private ViewPager mViewPager;

    public CommunityTabView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CommunityTabView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(@NonNull Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_community_tab, this, true);
        ButterKnife.bind(this, view);
        selection(0);
    }

    public void selection(int position) {
        switch (position) {
            case 0:
                tvTitle1.setTextColor(getSelectColor(true));
                tvTitle1.setTextSize(getSelectTextSize(true));
                viewIndicator1.setSelected(true);
                tvTitle2.setTextColor(getSelectColor(false));
                tvTitle2.setTextSize(getSelectTextSize(false));
                viewIndicator2.setSelected(false);
                break;
            case 1:
                tvTitle1.setTextColor(getSelectColor(false));
                tvTitle1.setTextSize(getSelectTextSize(false));
                viewIndicator1.setSelected(false);
                tvTitle2.setTextColor(getSelectColor(true));
                tvTitle2.setTextSize(getSelectTextSize(true));
                viewIndicator2.setSelected(true);
                break;
        }
    }

    private int getSelectColor(boolean selected) {
        if (selected) {
            return Color.parseColor("#37BD42");
        } else {
            return Color.parseColor("#333333");
        }
    }

    private int getSelectTextSize(boolean selected) {
        if (selected) {
            return 25;
        } else {
            return 17;
        }
    }

    public void setupViewPager(ViewPager vpContent) {
        mViewPager = vpContent;
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.ll_tab1, R.id.ll_tab2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_tab1:
                if(mViewPager!=null){
                    mViewPager.setCurrentItem(0);
                }
                break;
            case R.id.ll_tab2:
                if(mViewPager!=null){
                    mViewPager.setCurrentItem(1);
                }
                break;
        }
    }
}
