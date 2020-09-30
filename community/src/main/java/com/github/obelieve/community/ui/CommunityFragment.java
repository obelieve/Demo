package com.github.obelieve.community.ui;


import android.app.ActivityOptions;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.github.obelieve.utils.ActivityUtil;
import com.github.obelieve.community.R;
import com.github.obelieve.event.community.CommunityScrollToTopNotifyEvent;
import com.github.obelieve.repository.cache.constant.SystemValue;
import com.google.android.material.appbar.AppBarLayout;
import com.zxy.frame.base.ApiBaseStatusBarFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class CommunityFragment extends ApiBaseStatusBarFragment {

    @BindView(R.id.view_status_bar)
    View statusBarView;
    @BindView(R.id.view_top)
    View viewTop;
    @BindView(R.id.stl_tab)
    SlidingTabLayout stl_tab;
    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.abl_content)
    AppBarLayout ablContent;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    String[] mTitles = {"热门", "新鲜"};
    final int TOP_CLICK_DELAY_TIME = 500;
    long mTopClickTime;


    @Override
    public int layoutId() {
        return R.layout.fragment_community;
    }

    @Override
    protected void initView() {
        super.initView();
        ablContent.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (appBarLayout.getTotalScrollRange() > 0 && Math.abs(verticalOffset) > appBarLayout.getTotalScrollRange() / 4) {
                    float percent = Math.abs(verticalOffset) / (appBarLayout.getTotalScrollRange() * 1.0f);
                    ivSearch.setAlpha(percent);
                    ivSearch.setVisibility(View.VISIBLE);
                } else {
                    ivSearch.setVisibility(View.GONE);
                }
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    ivSearch.setEnabled(true);
                    ivSearch.setAlpha(1.0f);
                } else {
                    ivSearch.setEnabled(false);
                }
            }
        });
        vpContent.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                if(position==0){
                    return new UpdatesFragment(1);
                }else{
                    return new UpdatesFragment();
                }
            }

            @Override
            public int getCount() {
                return mTitles.length;
            }
        });
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mTitles.length; i++) {
                    if (i == position) {
                        stl_tab.getTitleView(i).setTextSize(23);
                    } else {
                        stl_tab.getTitleView(i).setTextSize(17);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        stl_tab.setViewPager(vpContent, mTitles);
        stl_tab.getTitleView(0).setTextSize(23);
    }

    @OnClick({R.id.tv_publish, R.id.tv_search, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_publish:
                if (TextUtils.isEmpty(SystemValue.token)) {
                    ActivityUtil.gotoLoginActivity(mActivity);
                } else {
                    startActivity(new Intent(getActivity(), IssueUpdateActivity.class));
                }
                break;
            case R.id.tv_search:
                startActivity(new Intent(getActivity(), SearchPostActivity.class), ActivityOptions.makeSceneTransitionAnimation(mActivity,tvSearch,"search").toBundle());
                break;
            case R.id.iv_search:
                startActivity(new Intent(getActivity(), SearchPostActivity.class), ActivityOptions.makeSceneTransitionAnimation(mActivity,ivSearch,"search").toBundle());
                break;
        }
    }

    @Override
    public View statusBarView() {
        return statusBarView;
    }


    @OnClick(R.id.view_top)
    public void onViewClicked() {
        if (System.currentTimeMillis() - mTopClickTime < TOP_CLICK_DELAY_TIME) {
            EventBus.getDefault().post(new CommunityScrollToTopNotifyEvent(vpContent.getCurrentItem()==0?1:0));
        }
        mTopClickTime = System.currentTimeMillis();
    }
}
