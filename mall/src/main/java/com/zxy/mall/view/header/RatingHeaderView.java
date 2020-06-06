package com.zxy.mall.view.header;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zxy.mall.R;
import com.zxy.mall.entity.SellerEntity;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RatingHeaderView extends FrameLayout {

    @BindView(R.id.tv_score)
    TextView mTvScore;
    @BindView(R.id.tv_score_title)
    TextView mTvScoreTitle;
    @BindView(R.id.tv_rank_rate)
    TextView mTvRankRate;
    @BindView(R.id.view)
    View mView;
    @BindView(R.id.tv_title1)
    TextView mTvTitle1;
    @BindView(R.id.rb_service_score)
    RatingBar mRbServiceScore;
    @BindView(R.id.tv_service_score)
    TextView mTvServiceScore;
    @BindView(R.id.tv_title2)
    TextView mTvTitle2;
    @BindView(R.id.rb_food_score)
    RatingBar mRbFoodScore;
    @BindView(R.id.tv_food_score)
    TextView mTvFoodScore;
    @BindView(R.id.tv_delivery_time)
    TextView mTvDeliveryTime;

    public RatingHeaderView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public RatingHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RatingHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_rating_header, this, true);
        ButterKnife.bind(this, view);
    }

    public void loadData(SellerEntity entity) {
        if (entity != null) {
            mTvScore.setText(entity.getScore() + "");
            mTvServiceScore.setText(getScaleDouble(entity.getServiceScore()) + "");
            mRbServiceScore.setRating((float) getScaleDouble(entity.getServiceScore()));
            mTvFoodScore.setText(getScaleDouble(entity.getFoodScore()) + "");
            mRbFoodScore.setRating((float) getScaleDouble(entity.getFoodScore()));
            mTvRankRate.setText("高于周边商家" + entity.getRankRate() + "%");
            mTvDeliveryTime.setText("送达时间" + entity.getDeliveryTime() + "分钟");
        }
    }

    private double getScaleDouble(double d) {
        BigDecimal b = new BigDecimal(d);
        return b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
