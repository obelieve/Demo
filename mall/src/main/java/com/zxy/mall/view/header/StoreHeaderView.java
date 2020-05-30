package com.zxy.mall.view.header;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zxy.mall.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoreHeaderView extends FrameLayout {

    @BindView(R.id.iv_bg)
    ImageView mIvBg;
    @BindView(R.id.iv_image)
    ImageView mIvImage;
    @BindView(R.id.iv_brand)
    ImageView mIvBrand;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_delivery)
    TextView mTvDelivery;
    @BindView(R.id.iv_tag)
    ImageView mIvTag;
    @BindView(R.id.tv_tag)
    TextView mTvTag;
    @BindView(R.id.tv_num)
    TextView mTvNum;
    @BindView(R.id.tv_bulletin)
    TextView mTvBulletin;

    public StoreHeaderView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public StoreHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StoreHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_store_header, this, true);
        ButterKnife.bind(this,view);
    }

    @OnClick(R.id.tv_num)
    public void onViewClicked() {
    }
}
