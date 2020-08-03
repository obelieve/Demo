package com.zxy.demo.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.zxy.demo.R;
import com.zxy.frame.base.BaseFragment;

import butterknife.BindView;

public class NestedScrollView_ViewPagerFragment extends BaseFragment {

    @BindView(R.id.tl_tab)
    TabLayout mTlTab;
    @BindView(R.id.vp_content)
    CViewPager mVpContent;

    @Override
    public int layoutId() {
        return R.layout.fragment_nestedscrollview_viewpager;
    }

    @Override
    protected void initView() {
        mVpContent.setAdapter(new WrappingFragmentPagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return position==0?new Fragment1():new Fragment2();
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return "页面"+position;
            }
        });
        mTlTab.setupWithViewPager(mVpContent);
    }
}
