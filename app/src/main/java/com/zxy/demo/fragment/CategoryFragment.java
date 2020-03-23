package com.zxy.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.zxy.demo.R;
import com.zxy.demo.adapter.viewholder.CategoryViewHolder;
import com.zxy.demo.base.BaseFragment;
import com.zxy.demo.base.BaseRecyclerViewAdapter;
import com.zxy.demo.utils.StatusBarUtil;
import com.zxy.demo.view.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

;

/**
 * Created by zxy on 2018/10/30 10:37.
 */

public class CategoryFragment extends BaseFragment {


    private RecyclerView mRvTitle;
    private VerticalViewPager mVvpContent;
    private FrameLayout mFlTitle;

    private CategoryAdapter mAdapter;
    private ViewPagerAdapter mViewPagerAdapter;

    @Override
    public boolean settingStatusBarLight() {
        return false;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        mFlTitle = view.findViewById(R.id.fl_title);
        mRvTitle = view.findViewById(R.id.rv_title);
        mVvpContent = view.findViewById(R.id.vvp_content);
        StatusBarUtil.fitsSystemWindows(mFlTitle);
        mAdapter = new CategoryAdapter();
        mAdapter.setItemClickCallback(new BaseRecyclerViewAdapter.OnItemClickCallback<Boolean>() {
            @Override
            public void onItemClick(View view, Boolean aBoolean, int position) {
                mVvpContent.setCurrentItem(position);
            }
        });
        mRvTitle.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvTitle.setAdapter(mAdapter);
        if(mViewPagerAdapter!=null){
            mViewPagerAdapter.clearFragmentCache(getChildFragmentManager(),mVvpContent);
        }
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), mVvpContent);
        mVvpContent.setAdapter(mViewPagerAdapter);
        mVvpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mAdapter.setSelectedItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> mList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager fm, VerticalViewPager viewPager) {
            super(fm);
            int i = 15;
            while (i > 0) {
                mList.add(CategoryPagerFragment.getInstance(viewPager));
                i--;
            }
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        private void clearFragmentCache(FragmentManager mFragmentManager,ViewGroup container) {
            try {
                int size = getCount();
                FragmentTransaction mCurTransaction = mFragmentManager.beginTransaction();
                for (int i = 0; i < size; i++) {
                    String name = "android:switcher:" + container.getId() + ":" + i;
                    Fragment fragment = mFragmentManager.findFragmentByTag(name);
                    if (fragment != null) {
                        mCurTransaction.remove(fragment);
                    }
                } mCurTransaction.commitNowAllowingStateLoss();} catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static class CategoryAdapter extends BaseRecyclerViewAdapter<Boolean, CategoryViewHolder> {

        private List<Boolean> mBooleanList = new ArrayList<>();
        private int mLastSelectedPosition = 0;

        {
            for (int i = 0; i < 15; i++) {
                mBooleanList.add(false);
            }
            mBooleanList.set(0, true);
            getDataHolder().setList(mBooleanList);
        }

        public void setSelectedItem(int item) {
            getDataHolder().getList().set(mLastSelectedPosition, false);
            getDataHolder().getList().set(item, true);
            mLastSelectedPosition = item;
            getDataHolder().notifyDataSetChanged();
        }

        @Override
        public CategoryViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new CategoryViewHolder(parent);
        }

        @Override
        public void loadViewHolder(CategoryViewHolder holder, int position) {
            holder.clContent.setSelected(mBooleanList.get(position));
            holder.viewSelected.setSelected(mBooleanList.get(position));
        }

    }


}
