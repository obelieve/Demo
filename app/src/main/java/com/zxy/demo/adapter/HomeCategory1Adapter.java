package com.zxy.demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.zxy.demo.R;
import com.zxy.demo.utils.GlideUtil;
import com.zxy.utility.LogUtil;

import java.util.Arrays;

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
                                Toast.makeText(holder.itemView.getContext().getApplicationContext(),"aaa",Toast.LENGTH_SHORT).show();
                            }
                        }).startTurning();
                //设置指示器的方向
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
//                .setOnPageChangeListener(this)//监听翻页事件
                ;
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
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        int type = super.getItemViewType(position);
        type = 0;
        return type;
    }

    public void onPause() {
        if (mHome1Type0ViewHolder != null) {
            mHome1Type0ViewHolder.banner_content.stopTurning();
        }
    }


    public static class Home1Type0ViewHolder extends RecyclerView.ViewHolder {

        private static String[] images = {
                "http://img2.3lian.com/2014/f2/37/d/40.jpg",
                "http://img2.3lian.com/2014/f2/37/d/39.jpg",
                "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
                "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
                "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
        };
        ConvenientBanner<String> banner_content;

        private Home1Type0ViewHolder(View itemView) {
            super(itemView);
            banner_content = itemView.findViewById(R.id.banner_content);
            banner_content.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
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

    private static class Home1Type1ViewHolder extends RecyclerView.ViewHolder {

        private Home1Type1ViewHolder(View itemView) {
            super(itemView);
        }
    }


    private static class Home1Type2ViewHolder extends RecyclerView.ViewHolder {

        private Home1Type2ViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class Home1Type3ViewHolder extends RecyclerView.ViewHolder {

        private Home1Type3ViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class Home1Type4ViewHolder extends RecyclerView.ViewHolder {

        private Home1Type4ViewHolder(View view) {
            super(view);
        }
    }

    private static class Home1Type5ViewHolder extends RecyclerView.ViewHolder {

        private Home1Type5ViewHolder(View view) {
            super(view);
        }
    }
}
