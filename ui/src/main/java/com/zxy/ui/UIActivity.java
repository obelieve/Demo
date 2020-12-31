package com.zxy.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.news.anim.databinding.ActivityAnimationBinding;
import com.zxy.frame.base.ApiBaseActivity2;
import com.zxy.ui.anim.AnimationFragment;

/**
 * Created by Admin
 * on 2020/6/10
 */
public class UIActivity extends ApiBaseActivity2<ActivityAnimationBinding> {


    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        mViewBinding.vpContent.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                Fragment fragment;
                try {
                    fragment = (Fragment) UIEnum.values()[position].getClazz().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    fragment = new AnimationFragment();
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return UIEnum.values().length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return UIEnum.values()[position].getName();
            }
        });
        mViewBinding.tlTab.setupWithViewPager(mViewBinding.vpContent);
        mViewBinding.vpContent.setCurrentItem(UIEnum.getCurrentIndex());
    }

    @Override
    protected void initViewModel() {

    }

}
