package com.zxy.mall.view.header;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zxy.frame.utils.image.GlideUtil;
import com.zxy.mall.R;
import com.zxy.mall.entity.FoodEntity;
import com.zxy.mall.view.BuyView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsDetailHeaderView extends FrameLayout {

    @BindView(R.id.iv_icon)
    ImageView mIvIcon;
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
    @BindView(R.id.tv_goods_info)
    TextView mTvGoodsInfo;


    public GoodsDetailHeaderView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public GoodsDetailHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GoodsDetailHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_goods_detail_header, this, true);
        ButterKnife.bind(this, view);
    }

    public void loadData(FoodEntity entity) {
        GlideUtil.loadImage(getContext(), entity.getIcon(), mIvIcon);
        mTvName.setText(entity.getName());
        mTvSellCount.setText("月售" + entity.getSellCount() + "份");
        mTvRating.setText("好评率" + entity.getRating() + "%");
        mTvPrice.setText(entity.getPrice() + "");
        if (!TextUtils.isEmpty(entity.getOldPrice())) {
            mTvOldPrice.setVisibility(View.VISIBLE);
            mTvOldPrice.setText("￥" + entity.getOldPrice() + "");
        } else {
            mTvOldPrice.setVisibility(View.GONE);
        }
        mTvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mTvGoodsInfo.setText(entity.getInfo());
    }


}
