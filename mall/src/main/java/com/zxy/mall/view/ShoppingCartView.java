package com.zxy.mall.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zxy.mall.R;
import com.zxy.utility.LogUtil;

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
        ButterKnife.bind(this,view);
        LogUtil.e("mTvConfirm 绑定："+mTvConfirm);
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
}
