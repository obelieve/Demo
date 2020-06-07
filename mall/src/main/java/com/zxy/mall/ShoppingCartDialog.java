package com.zxy.mall;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.dialog.BaseDialog;
import com.zxy.mall.utils.ShoppingCartManager;
import com.zxy.mall.view.BuyView;
import com.zxy.utility.SystemUtil;

import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShoppingCartDialog extends BaseDialog {


    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.tv_clear)
    TextView mTvClear;

    ShoppingCartAdapter mShoppingCartAdapter;
    Callback mCallback;

    public ShoppingCartDialog(Activity activity) {
        super(activity, R.style.BaseBottomDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_shopping_cart, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        setWidth(SystemUtil.screenWidth());
        setGravity(Gravity.BOTTOM);
        mRvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        mShoppingCartAdapter = new ShoppingCartAdapter(getActivity());
        mRvContent.setAdapter(mShoppingCartAdapter);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void refreshData() {
        mShoppingCartAdapter.getDataHolder().setList(ShoppingCartManager.getInstance().getGoodsList());
    }

    @OnClick(R.id.tv_clear)
    public void onViewClicked() {
        ShoppingCartManager.getInstance().clearAll();
        mShoppingCartAdapter.getDataHolder().setList(new ArrayList<>());
        if(mCallback!=null){
            mCallback.shoppingCart(0,0.0f);
        }
        dismiss();
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

    public interface Callback{
        void shoppingCart(int count,float sumPrice);
    }
}
