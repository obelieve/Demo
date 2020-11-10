package com.zxy.demo.fragment;

import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.zxy.demo.R;
import com.zxy.frame.base.ApiBaseFragment;
import com.zxy.frame.view.pagestatus.PageStatus;
import com.zxy.frame.view.pagestatus.PageStatusView;

import butterknife.BindView;

/**
 * Created by Administrator
 * on 2020/4/17
 */
public class StickyCoordinatorFragment extends ApiBaseFragment {

    @BindView(R.id.cl_content)
    CoordinatorLayout clContent;
    @BindView(R.id.common_index_activity_view_status_bar)
    View commonIndexActivityViewStatusBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ctl_bar)
    CollapsingToolbarLayout ctlBar;
    @BindView(R.id.abl_content)
    AppBarLayout ablContent;
    @BindView(R.id.tl_tab)
    TabLayout tlTab;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.view_page_status)
    PageStatusView mViewPageStatus;

    Handler mHandler = new Handler();

    @Override
    public int layoutId() {
        return R.layout.fragment_sticky_coordinator;
    }

    @Override
    protected void initView() {
        mViewPageStatus.setStatusView(PageStatus.getPageStatusView(getContext()));
        mViewPageStatus.setStatus(PageStatus.Status.LOADING);
//        mViewPageStatus.setCallback(new PageStatusView.Callback() {
//
//            @Override
//            public void onInflated(View view) {
//                if(view instanceof FailurePageStatusView){
//                    ((FailurePageStatusView) view).setCallback(new FailurePageStatusView.Callback() {
//                        @Override
//                        public void onRefresh() {
//                            mViewPageStatus.setStatus(PageStatus.Status.SUCCESS);
//                        }
//                    });
//                }
//            }
//        });
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mViewPageStatus.setStatus(PageStatus.Status.FAILURE);
//            }
//        }, 500);
        tlTab.setupWithViewPager(vpContent);
        vpContent.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return new LoadRefreshFragment();
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return "POSITION " + position;
            }
        });
    }
}
