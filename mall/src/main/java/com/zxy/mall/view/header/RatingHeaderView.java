package com.zxy.mall.view.header;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.zxy.mall.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RatingHeaderView extends FrameLayout {

    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_sum_rating)
    TextView mTvSumRating;
    @BindView(R.id.tv_info)
    TextView mTvInfo;
    @BindView(R.id.view)
    View mView;
    @BindView(R.id.tv_title1)
    TextView mTvTitle1;
    @BindView(R.id.rb_rating1)
    RatingBar mRbRating1;
    @BindView(R.id.tv_rating1)
    TextView mTvRating1;
    @BindView(R.id.tv_title2)
    TextView mTvTitle2;
    @BindView(R.id.rb_rating2)
    RatingBar mRbRating2;
    @BindView(R.id.tv_rating2)
    TextView mTvRating2;
    @BindView(R.id.tv_delivery_time)
    TextView mTvDeliveryTime;
    @BindView(R.id.rv_rating_category)
    RecyclerView mRvRatingCategory;
    @BindView(R.id.cb_select_content)
    CheckBox mCbSelectContent;

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
}
