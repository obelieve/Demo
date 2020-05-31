package com.zxy.mall.view.header;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.zxy.frame.utils.ViewUtil;
import com.zxy.frame.utils.image.GlideApp;
import com.zxy.mall.R;
import com.zxy.mall.entity.SellerEntity;
import com.zxy.mall.entity.SupportEntity;
import com.zxy.mall.utils.MallUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;

public class StoreHeaderView extends FrameLayout {

    @BindView(R.id.view_statusBar)
    View mViewStatusBar;
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
        ButterKnife.bind(this, view);
        ViewUtil.insetStatusBar(mViewStatusBar);
    }

    @OnClick(R.id.tv_num)
    public void onViewClicked() {

    }

    public void loadData(SellerEntity entity) {
        if (entity == null) return;
        MultiTransformation<Bitmap> multiTransformation = new MultiTransformation<>(new BlurTransformation(25, 3), new ColorFilterTransformation(0x66111111));
        Object avatar = entity.getAvatar();
        GlideApp.with(this).load(avatar).error(R.color.color_333333)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .into(mIvBg);
        GlideApp.with(this).load(avatar).error(R.drawable.ic_error).into(mIvImage);
        mTvName.setText(entity.getName());
        mTvDelivery.setText(entity.getDescription() + entity.getDeliveryTime() + "分钟送达");
        if (entity.getSupports() != null) {
            int size = entity.getSupports().size();
            if (size > 0) {
                SupportEntity supportEntity = entity.getSupports().get(0);
                GlideApp.with(this).load(MallUtil.getSupportTypeIcon(supportEntity.getType())).into(mIvTag);
                mTvTag.setText(supportEntity.getDescription());
            }
            mTvNum.setText(size + "个");
        }
        mTvBulletin.setText(entity.getBulletin());
    }
}
