package com.zxy.demo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.zxy.demo.databinding.ActivityMainBinding;
import com.zxy.demo.fragment.LoadRefreshFragment;
import com.zxy.frame.base.ApiBaseActivity;

public class MainActivity extends ApiBaseActivity<ActivityMainBinding> {


    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        mViewBinding.vpContent.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                Fragment fragment;
                try {
                    fragment = (Fragment) MainEnum.values()[position].getClazz().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    fragment = new LoadRefreshFragment();
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return MainEnum.values().length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return MainEnum.values()[position].getName();
            }
        });
        mViewBinding.tlTab.setupWithViewPager(mViewBinding.vpContent);
        mViewBinding.vpContent.setCurrentItem(MainEnum.getCurrentIndex());
    }

}



