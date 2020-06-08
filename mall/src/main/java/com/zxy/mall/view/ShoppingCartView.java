package com.zxy.mall.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.mall.R;
import com.zxy.mall.entity.SellerEntity;
import com.zxy.mall.utils.ShoppingCartManager;

import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ShoppingCartView extends FrameLayout {


    @BindView(R.id.fl_empty)
    FrameLayout mflEmpty;
    @BindView(R.id.ll_shopping_cart)
    LinearLayout mLlShoppingCart;
    @BindView(R.id.cl_shopping_cart_bottom)
    ConstraintLayout clShoppingCartBottom;
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.tv_clear)
    TextView mTvClear;
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

    ShoppingCartAdapter mShoppingCartAdapter;
    SellerEntity mSellerEntity;

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
        mRvContent.setLayoutManager(new LinearLayoutManager(context));
        mShoppingCartAdapter = new ShoppingCartAdapter(context);
        mRvContent.setAdapter(mShoppingCartAdapter);
    }

    @OnClick({R.id.fl_empty,R.id.civ_shopping_cart, R.id.tv_confirm,R.id.tv_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_empty:
                hideShoppingCart();
                break;
            case R.id.civ_shopping_cart:
                if(isShowShoppingCart()){
                 hideShoppingCart();
                }else{
                    showShoppingCart();
                }
                break;
            case R.id.tv_confirm:
                break;
            case R.id.tv_clear:
                ShoppingCartManager.getInstance().clearAll();
                mShoppingCartAdapter.getDataHolder().setList(new ArrayList<>());
                setCountAndPrice(0, 0.0f);
                hideShoppingCart();
                break;
        }
    }

    public void setCountAndPrice(int count, float sumPrice) {
        if (count > 0) {
            mTvNumDot.setVisibility(VISIBLE);
            mTvNumDot.setText(count + "");
        } else {
            mTvNumDot.setVisibility(GONE);
        }
        mTvPrice.setText("￥" + sumPrice);
        if (sumPrice >= ShoppingCartManager.getInstance().deliveryPrice()) {
            mTvConfirm.setEnabled(true);
            mTvConfirm.setText("￥" + sumPrice);
        } else if (sumPrice == 0) {
            if (mSellerEntity != null) {
                mTvConfirm.setEnabled(false);
                mTvConfirm.setText("￥" + mSellerEntity.getMinPrice() + "起送");
            }
        } else {
            mTvConfirm.setEnabled(false);
            mTvConfirm.setText("还差￥" + (ShoppingCartManager.getInstance().deliveryPrice() - sumPrice) + "起送");
        }
    }

    public void loadData(SellerEntity entity) {
        mSellerEntity = entity;
        mTvNumDot.setVisibility(GONE);
        mCivShoppingCart.setSelected(false);
        mIvShoppingCartIcon.setSelected(false);
        mTvPrice.setText("￥0");
        mTvAppendPrice.setText("另需配送费￥" + entity.getDeliveryPrice() + "元");
        mTvConfirm.setText("￥" + entity.getMinPrice() + "起送");
    }

    public boolean isShowShoppingCart(){
        return (mflEmpty.getVisibility()==View.VISIBLE&&mLlShoppingCart.getVisibility()==View.VISIBLE);
    }

    public void showShoppingCart(){
        mflEmpty.setVisibility(VISIBLE);
        mLlShoppingCart.setVisibility(VISIBLE);
        clShoppingCartBottom.setBackgroundColor(Color.WHITE);
        mShoppingCartAdapter.getDataHolder().setList(ShoppingCartManager.getInstance().getGoodsList());
    }

    public void hideShoppingCart(){
        mflEmpty.setVisibility(INVISIBLE);
        mLlShoppingCart.setVisibility(INVISIBLE);
        clShoppingCartBottom.setBackgroundColor(Color.TRANSPARENT);
    }

    public static class ShoppingCartAdapter extends BaseRecyclerViewAdapter<ShoppingCartManager.GoodsBean> {


        public ShoppingCartAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new ShoppingCartViewHolder(parent, R.layout.viewholder_shopping_cart);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            if (holder instanceof ShoppingCartViewHolder) {
                holder.bind(getDataHolder().getList().get(position));
            }
        }


        public class ShoppingCartViewHolder extends BaseViewHolder<ShoppingCartManager.GoodsBean> {

            @BindView(R.id.tv_name)
            TextView mTvName;
            @BindView(R.id.tv_price)
            TextView mTvPrice;
            @BindView(R.id.view_buy)
            BuyView mBuyView;


            ShoppingCartManager.GoodsBean mGoodsBean;

            public ShoppingCartViewHolder(ViewGroup parent, int layoutId) {
                super(parent, layoutId);
                mBuyView.setCallback(new BuyView.Callback() {
                    @Override
                    public void onAdd(int num) {
                        if (mGoodsBean != null) {
                            int count = mGoodsBean.getCount() + 1;
                            mGoodsBean.setCount(count);
                            ShoppingCartManager.getInstance().addGoods(mGoodsBean.getId(), mGoodsBean.getName(), mGoodsBean.getPrice(), count,mGoodsBean.getMaxCount());
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onRemove(int num) {
                        if (mGoodsBean != null) {
                            mGoodsBean.setCount(num);
                            ShoppingCartManager.getInstance().removeGoods(mGoodsBean.getId(), mGoodsBean.getName(), mGoodsBean.getPrice(),num);
                            notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void bind(ShoppingCartManager.GoodsBean bean) {
                super.bind(bean);
                mGoodsBean = bean;
                mTvName.setText(bean.getName());
                mTvPrice.setText(getPriceDouble(bean.getPrice()) + "");
                mBuyView.setMaxNum(bean.getMaxCount());
                mBuyView.setNum(bean.getCount());
            }

        }

        private double getPriceDouble(float price) {
            BigDecimal b = new BigDecimal(price);
            return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
    }

}
