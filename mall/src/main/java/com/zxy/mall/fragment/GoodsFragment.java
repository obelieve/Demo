package com.zxy.mall.fragment;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.base.BaseFragment;
import com.zxy.frame.utils.image.GlideUtil;
import com.zxy.mall.R;
import com.zxy.mall.entity.FoodEntity;
import com.zxy.mall.entity.GoodEntity;
import com.zxy.mall.entity.mock.MockRepository;
import com.zxy.mall.view.BuyView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsFragment extends BaseFragment {

    @BindView(R.id.rv_left)
    RecyclerView mRvLeft;
    @BindView(R.id.rv_right)
    RecyclerView mRvRight;

    GoodsLeftAdapter mGoodsLeftAdapter;
    GoodsRightAdapter mGoodsRightAdapter;
    @Override
    public int layoutId() {
        return R.layout.fragment_goods;
    }

    @Override
    protected void initView() {
        mRvLeft.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvRight.setLayoutManager(new LinearLayoutManager(getContext()));
        mGoodsLeftAdapter = new GoodsLeftAdapter(getContext());
        mGoodsRightAdapter = new GoodsRightAdapter(getContext());
        mRvLeft.setAdapter(mGoodsLeftAdapter);
        mRvRight.setAdapter(mGoodsRightAdapter);
        mGoodsLeftAdapter.getDataHolder().setList(MockRepository.getGoodList());
        mGoodsRightAdapter.getDataHolder().setList(MockRepository.getFoodList());
        mGoodsLeftAdapter.setItemClickCallback(new BaseRecyclerViewAdapter.OnItemClickCallback<GoodEntity>() {
            @Override
            public void onItemClick(View view, GoodEntity entity, int position) {

            }
        });
    }


    public static class GoodsLeftAdapter extends BaseRecyclerViewAdapter<GoodEntity> {


        public GoodsLeftAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new GoodsLeftViewHolder(parent, R.layout.viewholder_goods_left);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            GoodsLeftViewHolder holder1 = ((GoodsLeftViewHolder) holder);
            holder1.mTvName.setText(getDataHolder().getList().get(position).getName());
        }


        public class GoodsLeftViewHolder extends BaseViewHolder {

            @BindView(R.id.tv_name)
            TextView mTvName;


            public GoodsLeftViewHolder(ViewGroup parent, int layoutId) {
                super(parent, layoutId);
                ButterKnife.bind(this, itemView);
            }
        }
    }


    public static class GoodsRightAdapter extends BaseRecyclerViewAdapter<FoodEntity> {


        public GoodsRightAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new GoodsRightViewHolder(parent, R.layout.viewholder_goods_right);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            GoodsRightViewHolder holder1 = ((GoodsRightViewHolder) holder);
            FoodEntity foodEntity = getDataHolder().getList().get(position);
           holder1.bind(foodEntity);

        }


        public class GoodsRightViewHolder extends BaseViewHolder {

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

            public GoodsRightViewHolder(ViewGroup parent, int layoutId) {
                super(parent, layoutId);
                ButterKnife.bind(this, itemView);
            }

            public void bind(FoodEntity foodEntity) {
                if(foodEntity.isTop()){
                    mTvType.setVisibility(View.VISIBLE);
                    mTvType.setText(foodEntity.getGoods_type_name());
                }else{
                    mTvType.setVisibility(View.GONE);
                }
                GlideUtil.loadImage(getContext(),foodEntity.getIcon(),mIvImage);
                mTvName.setText(foodEntity.getName());
                if(!TextUtils.isEmpty(foodEntity.getDescription())){
                    mTvInfo.setVisibility(View.VISIBLE);
                    mTvInfo.setText(foodEntity.getDescription());
                }else{
                    mTvInfo.setVisibility(View.GONE);
                }
                mTvSellCount.setText("月售"+foodEntity.getSellCount()+"份");
                mTvRating.setText("好评率"+foodEntity.getRating()+"%");
                mTvPrice.setText(foodEntity.getPrice()+"");
                if(!TextUtils.isEmpty(foodEntity.getOldPrice())){
                    mTvOldPrice.setVisibility(View.VISIBLE);
                    mTvOldPrice.setText("￥"+foodEntity.getOldPrice()+"");
                }else{
                    mTvOldPrice.setVisibility(View.GONE);
                }
                mTvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
    }
}
