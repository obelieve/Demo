package com.zxy.demo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.zxy.demo.R;
import com.zxy.demo.adapter.item_decoration.HorizontalItemDivider;
import com.zxy.demo.adapter.item_decoration.VerticalItemDivider;
import com.zxy.demo.utils.GlideUtil;
import com.zxy.demo.view.ShoppingCartView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeCategory1Adapter extends RecyclerView.Adapter {

    Home1Type0ViewHolder mHome1Type0ViewHolder;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_home1_type0, parent, false);
                mHome1Type0ViewHolder = new Home1Type0ViewHolder(view);
                viewHolder = mHome1Type0ViewHolder;
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_home1_type1, parent, false);
                viewHolder = new Home1Type1ViewHolder(view);
                break;
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_home1_type2, parent, false);
                viewHolder = new Home1Type2ViewHolder(view);
                break;
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_home1_type3, parent, false);
                viewHolder = new Home1Type3ViewHolder(view);
                break;
            case 4:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_home1_type4, parent, false);
                viewHolder = new Home1Type4ViewHolder(view);
                break;
            case 5:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_home1_type5, parent, false);
                viewHolder = new Home1Type5ViewHolder(view);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_home1_type5, parent, false);
                viewHolder = new Home1Type5ViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                Home1Type0ViewHolder type0 = (Home1Type0ViewHolder) holder;
                type0.banner_content.setPages(
                        new CBViewHolderCreator() {
                            @Override
                            public Home1Type0ViewHolder.LocalImageHolderView createHolder(View itemView) {
                                return new Home1Type0ViewHolder.LocalImageHolderView(itemView);
                            }

                            @Override
                            public int getLayoutId() {
                                return R.layout.item_local_image;
                            }
                        }, Arrays.asList(Home1Type0ViewHolder.images))
                        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                        .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_selected})
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Toast.makeText(holder.itemView.getContext().getApplicationContext(), "aaa", Toast.LENGTH_SHORT).show();
                            }
                        }).startTurning();
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if (position != 0)
            type = 3;
        if (position == 1)
            type = 2;
        if (position == 2)
            type = 1;
        return type;
    }

    public void onPause() {
        if (mHome1Type0ViewHolder != null) {
            mHome1Type0ViewHolder.banner_content.stopTurning();
        }
    }


    public static class Home1Type0ViewHolder extends RecyclerView.ViewHolder {

        private static String[] images = {
                "https://imgs.pupuapi.com/BANNER/2019/06/06/060610124935850306.png",
                "https://imgs.pupuapi.com/BANNER/2019/06/06/060615462457420031.png",
                "https://imgs.pupuapi.com/BANNER/2019/06/06/060615515102165585.png",
                "https://imgs.pupuapi.com/BANNER/2019/06/06/060618061564120610.png",
                "https://imgs.pupuapi.com/BANNER/2019/06/06/060615313653148911.png"
        };
        ConvenientBanner<String> banner_content;

        private Home1Type0ViewHolder(View itemView) {
            super(itemView);
            banner_content = itemView.findViewById(R.id.banner_content);
            banner_content.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
            RecyclerView rv = banner_content.findViewById(R.id.cbLoopViewPager);
            rv.setNestedScrollingEnabled(false);
        }

        public static class LocalImageHolderView extends Holder<String> {
            private ImageView ivPost;

            private LocalImageHolderView(View itemView) {
                super(itemView);
            }

            @Override
            protected void initView(View itemView) {
                ivPost = itemView.findViewById(R.id.ivPost);
            }

            @Override
            public void updateUI(String data) {
                GlideUtil.load(data).into(ivPost);
            }
        }
    }

    public static class Home1Type1ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_more;
        RecyclerView rv_content;
        Type1Adapter mType1Adapter;

        private Home1Type1ViewHolder(View itemView) {
            super(itemView);
            tv_more = itemView.findViewById(R.id.tv_more);
            rv_content = itemView.findViewById(R.id.rv_content);
            rv_content.setNestedScrollingEnabled(false);
            rv_content.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            rv_content.addItemDecoration(new HorizontalItemDivider(itemView.getContext().getResources().getColor(R.color.line_gray2)));
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


    public static class Home1Type2ViewHolder extends RecyclerView.ViewHolder {

        private Home1Type2ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class Home1Type3ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_content;
        RecyclerView rv_content;
        FrameLayout fl_content;
        Home1Type3ViewHolder.Type3Adapter mType3Adapter;

        public Home1Type3ViewHolder(View itemView) {
            super(itemView);
            iv_content = itemView.findViewById(R.id.iv_content);
            rv_content = itemView.findViewById(R.id.rv_content);
            fl_content = itemView.findViewById(R.id.fl_content);
            rv_content.setNestedScrollingEnabled(false);
            rv_content.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            VerticalItemDivider divider = new VerticalItemDivider(itemView.getContext().getResources().getColor(R.color.line_gray2));
            divider.marginLR(10,10);
            rv_content.addItemDecoration(divider);
            mType3Adapter = new Home1Type3ViewHolder.Type3Adapter();
            List<String> list = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                list.add("有野上海青_" + i);
            }
            mType3Adapter.setList(list);
            rv_content.setAdapter(mType3Adapter);
        }

        public static class Type3Adapter extends RecyclerView.Adapter {

            List<String> mList;

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_home1_type3_child, parent, false);
                return new Home1Type3ViewHolder.Type3ViewHolder(view);
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

        public static class Type3ViewHolder extends RecyclerView.ViewHolder {

            private TextView mTvLabel;
            private ImageView mIvImg;
            private TextView mTvTitle;
            private TextView mTvContent;
            private TextView mTvWeight;
            private TextView mTvCurPrice;
            private TextView mTvOriPrice;
            private ShoppingCartView mViewShopping;

            public Type3ViewHolder(View itemView) {
                super(itemView);
                mTvLabel = itemView.findViewById(R.id.tv_label);
                mIvImg = itemView.findViewById(R.id.iv_img);
                mTvTitle = itemView.findViewById(R.id.tv_title);
                mTvContent = itemView.findViewById(R.id.tv_content);
                mTvWeight = itemView.findViewById(R.id.tv_weight);
                mTvCurPrice = itemView.findViewById(R.id.tv_cur_price);
                mTvOriPrice = itemView.findViewById(R.id.tv_ori_price);
                mViewShopping = itemView.findViewById(R.id.view_shopping);
            }
        }
    }

    public static class Home1Type4ViewHolder extends RecyclerView.ViewHolder {

        private Home1Type4ViewHolder(View view) {
            super(view);
        }
    }

    public static class Home1Type5ViewHolder extends RecyclerView.ViewHolder {

        private Home1Type5ViewHolder(View view) {
            super(view);
        }
    }
}
