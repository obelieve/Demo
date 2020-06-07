package com.zxy.mall.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.base.BaseActivity;
import com.zxy.frame.utils.image.GlideApp;
import com.zxy.mall.App;
import com.zxy.mall.R;
import com.zxy.mall.entity.SellerEntity;
import com.zxy.mall.entity.SupportEntity;
import com.zxy.mall.entity.mock.MockRepository;
import com.zxy.mall.utils.MallUtil;
import com.zxy.utility.SystemUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;

public class BulletinActivity extends BaseActivity {


    @BindView(R.id.iv_bg)
    ImageView mIvBg;
    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.rb_score)
    RatingBar mRbScore;
    @BindView(R.id.rv_support)
    RecyclerView mRvSupport;
    @BindView(R.id.tv_bulletin)
    TextView mTvBulletin;
    @BindView(R.id.ic_close)
    ImageView mIcClose;

    SupportAdapter mSupportAdapter;

    public static void start(Activity activity) {
        App.pushScreenShot(activity);
        Intent intent = new Intent(activity, BulletinActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mNeedInsetStatusBar = false;
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean isLightStatusBar() {
        return false;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_bulletin;
    }

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        SellerEntity sellerEntity = MockRepository.getSeller();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mLlContent.getLayoutParams();
        params.setMargins(0, SystemUtil.getStatusBarHeight(),0,0);
        mLlContent.setLayoutParams(params);
        MultiTransformation<Bitmap> multiTransformation = new MultiTransformation<>(new BlurTransformation(3, 1),new ColorFilterTransformation(0xbb333333));
        GlideApp.with(this).load(App.peekScreenShot())
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .into(mIvBg);
        mTvName.setText(sellerEntity.getName());
        mRbScore.setRating((float) sellerEntity.getScore());
        mSupportAdapter = new SupportAdapter(this);
        mRvSupport.setLayoutManager(new LinearLayoutManager(this));
        mRvSupport.setAdapter(mSupportAdapter);
        mSupportAdapter.getDataHolder().setList(sellerEntity.getSupports());
        mTvBulletin.setText(sellerEntity.getBulletin());
    }

    @OnClick(R.id.ic_close)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.popScreenShot();
    }

    public static class SupportAdapter extends BaseRecyclerViewAdapter<SupportEntity> {


        public SupportAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new SupportViewHolder(parent, R.layout.viewholder_support_2);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            SupportViewHolder supportViewHolder = (SupportViewHolder) holder;
            supportViewHolder.bind(getDataHolder().getList().get(position));
        }

        public class SupportViewHolder extends BaseViewHolder {

            @BindView(R.id.iv_type)
            ImageView mIvType;
            @BindView(R.id.tv_desc)
            TextView mTvDesc;

            public SupportViewHolder(ViewGroup parent, int layoutId) {
                super(parent, layoutId);
                ButterKnife.bind(this, itemView);
            }

            public void bind(SupportEntity entity) {
                mIvType.setImageResource(MallUtil.getSupportTypeIcon(entity.getType()));
                mTvDesc.setText(entity.getDescription());
            }
        }
    }

}
