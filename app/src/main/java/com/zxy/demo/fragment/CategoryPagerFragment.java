package com.zxy.demo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.demo.base.BaseFragment;
import com.zxy.demo.view.VerticalViewPager;
import com.zxy.utility.LogUtil;

import java.util.Random;

public class CategoryPagerFragment extends BaseFragment {

    private static final String VERTICAL_VIEWPAGER = "VerticalViewPager";

    private RecyclerView mRvContent;

    public static CategoryPagerFragment getInstance(VerticalViewPager viewPager) {
        CategoryPagerFragment fragment = new CategoryPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(VERTICAL_VIEWPAGER, viewPager);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_pager, container, false);
        mRvContent = view.findViewById(R.id.rv_content);
        if (getArguments() != null)
            mRvContent.setOnTouchListener(new VerticalViewPager.VerticalVPOnTouchListener((VerticalViewPager) getArguments().getSerializable(VERTICAL_VIEWPAGER)));
        mRvContent.setLayoutManager(new GridLayoutManager(getContext(),  3));
        mRvContent.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRvContent.setAdapter(new CategoryPagerAdapter());
        return view;
    }

    public static class CategoryPagerAdapter extends RecyclerView.Adapter {

        private int mCount;

        public CategoryPagerAdapter() {
            mCount = new Random().nextBoolean() ? 15 : 9;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = null;
            RecyclerView.ViewHolder viewHolder = null;
            switch (viewType) {
                case 0:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category_pager0, parent, false);
                    viewHolder = new ViewHolder0(view);
                    break;
                case 1:
                default:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category_pager1, parent, false);
                    viewHolder = new ViewHolder1(view);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
            final int spanCount = manager.getSpanCount();
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return position == 0 ? spanCount : 1;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mCount;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? 0 : 1;
        }

        public static class ViewHolder0 extends RecyclerView.ViewHolder {

            private ImageView mIvContent;

            private ViewHolder0(View itemView) {
                super(itemView);
                mIvContent = itemView.findViewById(R.id.iv_content);
            }
        }

        public static class ViewHolder1 extends RecyclerView.ViewHolder {

            private ImageView mIvContent;
            private TextView mTvContent;

            private ViewHolder1(View itemView) {
                super(itemView);
                mIvContent = itemView.findViewById(R.id.iv_content);
                mTvContent = itemView.findViewById(R.id.tv_content);
            }
        }
    }
}
