package com.zxy.mall.view;

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
import com.zxy.mall.entity.SellerEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ShoppingCartView extends FrameLayout {


    @BindView(R.id.view_bg)
    View mViewBg;
    @BindView(R.id.civ_circle_bg)
    CircleImageView mCivCircleBg;
    @BindView(R.id.civ_shopping_cart)
    CircleImageView mCivShoppingCart;
    @BindView(R.id.iv_shopping_cart_icon)
    ImageView mIvShoppingCartIcon;
    @BindView(R.id.tv_num_dot)
    TextView mTvNumDot;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_append_price)
    TextView mTvAppendPrice;
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;

    public ShoppingCartView(@NonNull Context context) {
        this(context, null, 0);
    }

    public ShoppingCartView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShoppingCartView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_shopping_cart, this, true);
        ButterKnife.bind(this, view);
    }

    @OnClick({R.id.civ_shopping_cart, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.civ_shopping_cart:
                break;
            case R.id.tv_confirm:
                break;
        }
    }

    public void loadData(SellerEntity entity) {
        mTvNumDot.setVisibility(GONE);
        mCivShoppingCart.setSelected(false);
        mIvShoppingCartIcon.setSelected(false);
        mTvPrice.setText("￥0");
        mTvAppendPrice.setText("另需配送费￥" + entity.getDeliveryPrice() + "元");
        mTvConfirm.setText("￥" + entity.getMinPrice() + "起送");
    }

}
