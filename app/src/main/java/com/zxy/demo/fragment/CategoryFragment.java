package com.zxy.demo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.demo.adapter.HomeCategory2Adapter;
import com.zxy.demo.adapter.item_decoration.VerticalItemDivider;
import com.zxy.demo.base.BaseFragment;
import com.zxy.demo.utils.StatusBarUtil;
import com.zxy.demo.view.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxy on 2018/10/30 10:37.
 */

public class CategoryFragment extends BaseFragment {


    private RecyclerView mRvTitle;
    private VerticalViewPager mVvpContent;
    private FrameLayout mFlTitle;

    private CategoryAdapter mAdapter;

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
        mRvTitle.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvTitle.setAdapter(mAdapter);
        mVvpContent.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
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

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            int i=15;
            while (i>0){
                mList.add(new DiscoveryFragment());
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
    }

    public static class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

        private List<Boolean> mBooleanList = new ArrayList<>();
        private int mLastSelectedPosition=0;
        {
            for(int i=0;i<15;i++){
                mBooleanList.add(false);
            }
            mBooleanList.set(0,true);
        }

        public void setSelectedItem(int item){
            mBooleanList.set(mLastSelectedPosition,false);
            mBooleanList.set(item,true);
            mLastSelectedPosition = item;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent, false);
            return new CategoryViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
            holder.mClContent.setSelected(mBooleanList.get(position));
            holder.mViewSelected.setSelected(mBooleanList.get(position));
        }

        @Override
        public int getItemCount() {
            return 15;
        }

        public static class CategoryViewHolder extends RecyclerView.ViewHolder {

            private ConstraintLayout mClContent;
            private View mViewSelected;
            private TextView mTvTitle;

            private CategoryViewHolder(View itemView) {
                super(itemView);
                mClContent = itemView.findViewById(R.id.cl_content);
                mViewSelected = itemView.findViewById(R.id.view_selected);
                mTvTitle = itemView.findViewById(R.id.tv_title);
            }
        }
    }


}
