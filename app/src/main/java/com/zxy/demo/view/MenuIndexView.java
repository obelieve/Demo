package com.zxy.demo.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.zxy.demo.R;
import com.zxy.demo.adapter.item_decoration.HorizontalItemDivider;
import com.zxy.demo.adapter.item_decoration.VerticalItemDivider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuIndexView extends FrameLayout {


    private RecyclerView mRvContent;
    private ImageView mIvFold;

    MenuIndexAdapter mAdapter;

    public MenuIndexView(@NonNull Context context) {
        this(context, null, 0);
    }

    public MenuIndexView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuIndexView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_menu_index, this, true);
        mRvContent = findViewById(R.id.rv_content);
        mIvFold = findViewById(R.id.iv_fold);
        mRvContent.setLayoutManager(new GridLayoutManager(context, 5));
        mRvContent.addItemDecoration(new VerticalItemDivider(true, 2, Color.TRANSPARENT));
        mAdapter = new MenuIndexAdapter();
        mAdapter.setList(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
        mRvContent.setAdapter(mAdapter);
        mIvFold.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapter.isFold()) {
                    mAdapter.setFoldNotify(false);
                    mIvFold.setSelected(true);
                } else {
                    mAdapter.setFoldNotify(true);
                    mIvFold.setSelected(false);
                }
            }
        });
    }

    public static class MenuIndexAdapter extends RecyclerView.Adapter<MenuIndexAdapter.MenuIndexHolder> {

        private List<Integer> mStringList = new ArrayList<>();
        private boolean mFold = true;
        private int mItemCount;

        @NonNull
        @Override
        public MenuIndexHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_menu_index, parent, false);
            return new MenuIndexHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MenuIndexHolder holder, int position) {

        }

        public void setList(List<Integer> stringList) {
            if (stringList != null) {
                mStringList = stringList;
                setFold(true);
            }
        }

        public void setFold(boolean fold) {
            mFold = fold;
            if (mStringList != null) {
                if (fold) {
                    mItemCount = 10;
                } else {
                    mItemCount = mStringList.size();
                }
            }
        }

        public void setFoldNotify(boolean fold) {
            setFold(fold);
            notifyDataSetChanged();
        }

        public boolean isFold() {
            return mFold;
        }

        @Override
        public int getItemCount() {
            return mItemCount;
        }


        public static class MenuIndexHolder extends RecyclerView.ViewHolder {

            private ImageView mIv;
            private TextView mTv;

            public MenuIndexHolder(View itemView) {
                super(itemView);
                mIv = itemView.findViewById(R.id.iv);
                mTv = itemView.findViewById(R.id.tv);
            }
        }
    }
}
