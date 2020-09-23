package com.github.obelieve.community.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.obelieve.community.R;
import com.github.obelieve.community.bean.BBSUserInfoEntity;
import com.github.obelieve.utils.ImagePreviewUtil;
import com.zxy.frame.utils.image.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;

public class PersonalPageHeaderView extends FrameLayout {


    @BindView(R.id.iv_header_bg)
    ImageView ivHeaderBg;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_active_days)
    TextView tvActiveDays;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.civ_head)
    CircleImageView civHead;
    @BindView(R.id.rl_login)
    RelativeLayout rlLogin;
    @BindView(R.id.fl_content)
    FrameLayout flContent;

    private String mUserImageUrl;
    private BBSUserInfoEntity mEntity;


    public PersonalPageHeaderView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PersonalPageHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PersonalPageHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_personal_page_header, this, true);
        ButterKnife.bind(this, view);
    }

    public void loadData(BBSUserInfoEntity entity) {
        mEntity = entity;
        tvNickname.setText(entity.getNickname());
        tvInfo.setText(entity.getUserinfo());
        tvActiveDays.setText(entity.getActivedays() + "");
        MultiTransformation<Bitmap> multiTransformation = new MultiTransformation<>(new BlurTransformation(25, 3), new ColorFilterTransformation(0x66111111));
        Object avatar = entity.getAvatar();
        mUserImageUrl = (String) avatar;
        GlideApp.with(this).load(avatar).placeholder(R.drawable.me_face).error(R.drawable.me_face).into(civHead);
        GlideApp.with(this)
                .load(avatar)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        GlideApp.with(PersonalPageHeaderView.this).load(avatar)
                                .apply(RequestOptions.bitmapTransform(multiTransformation))
                                .into(ivHeaderBg);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        GlideApp.with(PersonalPageHeaderView.this).load(R.drawable.me_face)
                                .apply(RequestOptions.bitmapTransform(multiTransformation))
                                .into(ivHeaderBg);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @OnClick(R.id.civ_head)
    public void onViewClicked() {
        ImagePreviewUtil.show((Activity) getContext(),civHead, mUserImageUrl);
    }
}
