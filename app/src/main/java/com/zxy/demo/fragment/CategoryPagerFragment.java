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
import com.zxy.demo.adapter.viewholder.CategoryPagerViewHolder0;
import com.zxy.demo.adapter.viewholder.CategoryPagerViewHolder1;
import com.zxy.demo.base.BaseFragment;
import com.zxy.demo.base.BaseRecyclerViewAdapter;
import com.zxy.demo.utils.ToastUtil;
import com.zxy.demo.view.VerticalViewPager;
import com.zxy.utility.LogUtil;

import java.util.ArrayList;
import java.util.List;
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
        mRvContent.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRvContent.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRvContent.setAdapter(new CategoryPagerAdapter());
        return view;
    }

    public static class CategoryPagerAdapter extends BaseRecyclerViewAdapter<String, BaseRecyclerViewAdapter.BaseViewHolder> {

        public CategoryPagerAdapter() {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < (new Random().nextBoolean() ? 15 : 9); i++) {
                list.add("s=" + i);
            }
            getDataHolder().setList(list);
            setItemClickCallback(new OnItemClickCallback<String>() {
                @Override
                public void onItemClick(View view, String s, int position) {
                    ToastUtil.show(s);
                }
            });
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            BaseViewHolder viewHolder = null;
            switch (viewType) {
                case 0:
                    viewHolder = new CategoryPagerViewHolder0(parent);
                    break;
                case 1:
                default:
                    viewHolder = new CategoryPagerViewHolder1(parent);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {

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
        public int getItemViewType(int position) {
            return position == 0 ? 0 : 1;
        }
    }
}
