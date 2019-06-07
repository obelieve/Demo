package com.zxy.demo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.zxy.demo.R;
import com.zxy.demo.adapter.item_decoration.HorizontalItemDivider;
import com.zxy.demo.utils.GlideUtil;
import com.zxy.demo.view.ShoppingCartView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeCategory2Adapter extends RecyclerView.Adapter {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_home2_type1, parent, false);
                viewHolder = new RecyclerView.ViewHolder(view) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
                break;
            case 1:
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_home1_type1, parent, false);
                viewHolder = new Home1Type1ViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position) {

        return position == 0 ? 0 : 1;
    }

    private static class Home1Type1ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_more;
        RecyclerView rv_content;
        Type1Adapter mType1Adapter;

        private Home1Type1ViewHolder(View itemView) {
            super(itemView);
            tv_more = itemView.findViewById(R.id.tv_more);
            rv_content = itemView.findViewById(R.id.rv_content);
            rv_content.setNestedScrollingEnabled(false);
            rv_content.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            rv_content.addItemDecoration(new HorizontalItemDivider(itemView.getContext().getResources().getColor(R.color.gray)));
            mType1Adapter = new Type1Adapter();
            List<String> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                list.add("有野上海青_" + i);
            }
            mType1Adapter.setList(list);
            rv_content.setAdapter(mType1Adapter);
        }

        public static class Type1Adapter extends RecyclerView.Adapter {

            List<String> mList;

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_home1_type1_child, parent, false);
                return new Type1ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            public void setList(List<String> list) {
                mList = list;
            }

            @Override
            public int getItemCount() {
                if (mList != null)
                    return mList.size();
                return 0;
            }
        }

        public static class Type1ViewHolder extends RecyclerView.ViewHolder {

            private TextView mTvLabel;
            private ImageView mIvImg;
            private TextView mTvTitle;
            private TextView mTvCurPrice;
            private TextView mTvOriPrice;
            private ShoppingCartView mViewShopping;

            public Type1ViewHolder(View itemView) {
                super(itemView);
                mTvLabel = itemView.findViewById(R.id.tv_label);
                mIvImg = itemView.findViewById(R.id.iv_img);
                mTvTitle = itemView.findViewById(R.id.tv_title);
                mTvCurPrice = itemView.findViewById(R.id.tv_cur_price);
                mTvOriPrice = itemView.findViewById(R.id.tv_ori_price);
                mViewShopping = itemView.findViewById(R.id.view_shopping);
            }
        }
    }
}
