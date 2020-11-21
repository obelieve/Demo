package com.zxy.demo.fragment;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.zxy.demo.databinding.FragmentStickyCoordinatorBinding;
import com.zxy.frame.base.ApiBaseFragment;
import com.zxy.frame.view.PageStatusView;

/**
 * Created by Administrator
 * on 2020/4/17
 */
public class StickyCoordinatorFragment extends ApiBaseFragment<FragmentStickyCoordinatorBinding> {

    Handler mHandler = new Handler();

    @Override
    protected void initView() {
        mViewBinding.viewPageStatus.loading();
        mViewBinding.viewPageStatus.setCallback(new PageStatusView.Callback() {
            @Override
            public void onClickRefresh() {
                mViewBinding.viewPageStatus.success();
            }
        });
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewBinding.viewPageStatus.failure();
            }
        }, 500);
        mViewBinding.tlTab.setupWithViewPager(mViewBinding.vpContent);
        mViewBinding.vpContent.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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
