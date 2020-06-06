package com.zxy.mall.fragment;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.frame.base.BaseFragment;
import com.zxy.frame.utils.image.GlideUtil;
import com.zxy.frame.view.LeftRightRecyclerView;
import com.zxy.mall.R;
import com.zxy.mall.activity.GoodsDetailActivity;
import com.zxy.mall.entity.FoodEntity;
import com.zxy.mall.entity.GoodsEntity;
import com.zxy.mall.entity.mock.MockRepository;
import com.zxy.mall.view.BuyView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsFragment extends BaseFragment {

    @BindView(R.id.view_lrrv)
    LeftRightRecyclerView<GoodsEntity,FoodEntity> mViewLrrv;

    @Override
    public int layoutId() {
        return R.layout.fragment_goods;
    }

    @Override
    protected void initView() {
        mViewLrrv.init(new LeftRightRecyclerView.LeftViewHolderFactory<GoodsEntity>() {
            @Override
            public LeftRightRecyclerView.LeftViewHolder<GoodsEntity> genLeftViewHolder(ViewGroup parent) {
                return new GoodsLeftViewHolder(parent);
            }
        }, new LeftRightRecyclerView.RightViewHolderFactory<FoodEntity>() {
            @Override
            public LeftRightRecyclerView.RightViewHolder<FoodEntity> genRightViewHolder(ViewGroup parent) {
                return new FoodRightViewHolder(parent);
            }
        });
        mViewLrrv.setLeftRightData(MockRepository.getGoodList(),MockRepository.getFoodList());
        mViewLrrv.setCallback(new LeftRightRecyclerView.Callback<FoodEntity>() {
            @Override
            public void onRightItemClick(View view, FoodEntity data, int position) {
                GoodsDetailActivity.start(getActivity(),data.getGoods_type_name(),data.getName());
            }
        });
    }


    public static class GoodsLeftViewHolder extends LeftRightRecyclerView.LeftViewHolder<GoodsEntity>{

        @BindView(R.id.tv_name)
        TextView mTvName;

        public GoodsLeftViewHolder(ViewGroup parent) {
            super(parent, R.layout.viewholder_goods_left);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(GoodsEntity data) {
            mTvName.setText(data.getName());
            mTvName.setSelected(data.isSelected());
        }
    }

    public static class FoodRightViewHolder extends LeftRightRecyclerView.RightViewHolder<FoodEntity>{

        @BindView(R.id.tv_type)
        TextView mTvType;
        @BindView(R.id.iv_image)
        ImageView mIvImage;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_info)
        TextView mTvInfo;
        @BindView(R.id.tv_sell_count)
        TextView mTvSellCount;
        @BindView(R.id.tv_rating)
        TextView mTvRating;
        @BindView(R.id.tv_price)
        TextView mTvPrice;
        @BindView(R.id.tv_old_price)
        TextView mTvOldPrice;
        @BindView(R.id.view_buy)
        BuyView mViewBuy;

        private Context mContext;
        public FoodRightViewHolder(ViewGroup parent) {
            super(parent, R.layout.viewholder_goods_right);
            ButterKnife.bind(this, itemView);
            mContext = parent.getContext();
        }

        @Override
        public void bind(FoodEntity data) {
            if (data.isTop()) {
                mTvType.setVisibility(View.VISIBLE);
                mTvType.setText(data.getGoods_type_name());
            } else {
                mTvType.setVisibility(View.GONE);
            }
            GlideUtil.loadImage(mContext, data.getIcon(), mIvImage);
            mTvName.setText(data.getName());
            if (!TextUtils.isEmpty(data.getDescription())) {
                mTvInfo.setVisibility(View.VISIBLE);
                mTvInfo.setText(data.getDescription());
            } else {
                mTvInfo.setVisibility(View.GONE);
            }
            mTvSellCount.setText("月售" + data.getSellCount() + "份");
            mTvRating.setText("好评率" + data.getRating() + "%");
            mTvPrice.setText(data.getPrice() + "");
            if (!TextUtils.isEmpty(data.getOldPrice())) {
                mTvOldPrice.setVisibility(View.VISIBLE);
                mTvOldPrice.setText("￥" + data.getOldPrice() + "");
            } else {
                mTvOldPrice.setVisibility(View.GONE);
            }
            mTvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}
