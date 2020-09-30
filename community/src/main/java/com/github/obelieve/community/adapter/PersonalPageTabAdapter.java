package com.github.obelieve.community.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.github.obelieve.community.ui.PersonalPageTabFragment;

public class PersonalPageTabAdapter extends FragmentPagerAdapter {

    private int mUserId;
    private boolean mCurUser;

    public PersonalPageTabAdapter(FragmentManager fm, int userId, boolean curUser) {
        super(fm);
        mUserId = userId;
        mCurUser = curUser;
    }

    @Override
    public Fragment getItem(int position) {
        return PersonalPageTabFragment.getInstance(position, mUserId);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String pre = "TA的";
        if (mCurUser) {
            pre = "";
        }
        if (position == 0) return pre + "动态";
        else
            return pre + "点赞";
    }
}
