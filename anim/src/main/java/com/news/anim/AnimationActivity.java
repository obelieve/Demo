package com.news.anim;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.zxy.frame.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by Admin
 * on 2020/6/10
 */
public class AnimationActivity extends BaseActivity {

    @BindView(R.id.tl_tab)
    TabLayout tlTab;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    @Override
    protected int layoutId() {
        return R.layout.activity_animation;
    }

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        vpContent.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                Fragment fragment;
                try {
                    fragment = (Fragment) AnimationEnum.values()[position].getClazz().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    fragment = new AnimationFragment();
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return AnimationEnum.values().length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return AnimationEnum.values()[position].getName();
            }
        });
        tlTab.setupWithViewPager(vpContent);
        vpContent.setCurrentItem(AnimationEnum.getCurrentIndex());
    }

}
