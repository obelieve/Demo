package com.zxy.demo.fragment;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;

import com.zxy.demo.databinding.FragmentPayBinding;
import com.zxy.demo.databinding.ViewholderPayPriceSelectBinding;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.base.ApiBaseActivity;
import com.zxy.frame.base.ApiBaseFragment;
import com.zxy.frame.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin
 * on 2020/6/4
 */
public class PayFragment extends ApiBaseFragment<FragmentPayBinding> {


    SelectAdapter mSelectAdapter;

    @Override
    protected void initView() {
        mSelectAdapter = new SelectAdapter(getActivity());
        mViewBinding.rvSelect.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mViewBinding.rvSelect.setAdapter(mSelectAdapter);
        mSelectAdapter.setItemClickCallback(new BaseRecyclerViewAdapter.OnItemClickCallback<SelectEntity>() {
            @Override
            public void onItemClick(View view, SelectEntity entity, int position) {
                String num = entity.getIntegral() + "";
                mViewBinding.etNum.setText(num);
                mViewBinding.etNum.setSelection(num.length());
            }
        });
        mViewBinding.etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    for (SelectEntity entity : mSelectAdapter.getDataHolder().getList()) {
                        if (entity != null && entity.getMoney() != null && entity.getMoney().equals(s.toString())) {
                            mSelectAdapter.setSelect(entity);
                        }
                    }
                } else {
                    mSelectAdapter.removeSelect();
                }
            }
        });
        mSelectAdapter.getDataHolder().setList(MockData.getSelectEntityList());
        mViewBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mViewBinding.etNum.getText().toString())) {
                    ToastUtil.show("请输入充值金额");
                    return;
                }
                if (getActivity() instanceof ApiBaseActivity) {
                    ((ApiBaseActivity) getActivity()).showLoading();
                }
                String payWay;
                if (mViewBinding.rbAliPay.isChecked()) {
                    payWay = "alipay";
                    PayEntity payEntity = MockData.pay(payWay, mViewBinding.etNum.getText().toString());
                    PayUtil.aliPay(payEntity.getSign(), new Callback() {
                        @Override
                        public void onSuccess() {
                            if (getActivity() instanceof ApiBaseActivity) {
                                ((ApiBaseActivity) getActivity()).dismissLoading();
                            }
                            ToastUtil.show("支付成功");
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                } else {
                    payWay = "wechat";
                    PayEntity payEntity = MockData.pay(payWay, mViewBinding.etNum.getText().toString());
                    PayUtil.wxPay(payEntity.getSign(), new Callback() {
                        @Override
                        public void onSuccess() {
                            if (getActivity() instanceof ApiBaseActivity) {
                                ((ApiBaseActivity) getActivity()).dismissLoading();
                            }
                            ToastUtil.show("支付成功");
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                }
            }
        });
    }


    public static class SelectAdapter extends BaseRecyclerViewAdapter<SelectEntity> {

        private int mCurSelectPosition = -1;

        public SelectAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new PayPriceSelectViewHolder(ViewholderPayPriceSelectBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            PayPriceSelectViewHolder selectViewHolder = (PayPriceSelectViewHolder) holder;
            selectViewHolder.bind(getDataHolder().getList().get(position), position);
        }

        public void setSelect(SelectEntity entity) {
            List<SelectEntity> list = getDataHolder().getList();
            if (entity != null && list.contains(entity)) {
                entity.selected = true;
                mCurSelectPosition = list.indexOf(entity);
                notifyDataSetChanged();
            }
        }

        public void removeSelect() {
            List<SelectEntity> list = getDataHolder().getList();
            if (mCurSelectPosition >= 0 && mCurSelectPosition < list.size()) {
                list.get(mCurSelectPosition).selected = false;
            }
            mCurSelectPosition = -1;
            notifyDataSetChanged();
        }

        public class PayPriceSelectViewHolder extends BaseViewHolder<SelectEntity,ViewholderPayPriceSelectBinding> {


            public PayPriceSelectViewHolder(ViewholderPayPriceSelectBinding viewBinding) {
                super(viewBinding);
            }

            public void bind(SelectEntity entity, int pos) {
                if (mCurSelectPosition == -1 && entity.isSelected()) {
                    mCurSelectPosition = pos;
                }
                mViewBinding.llContent.setSelected(entity.selected);
                mViewBinding.tvMoney.setText(entity.getMoney() + "元");
                mViewBinding.tvIntegral.setText(entity.getIntegral() + "金币");
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!entity.selected) {
                            if (mCurSelectPosition >= 0 && mCurSelectPosition < getDataHolder().getList().size()) {
                                getDataHolder().getList().get(mCurSelectPosition).selected = false;
                            }
                            getDataHolder().getList().get(pos).selected = true;
                            mCurSelectPosition = pos;
                            notifyDataSetChanged();
                            if (mItemClickCallback != null) {
                                mItemClickCallback.onItemClick(itemView, entity, pos);
                            }
                        }
                    }
                });
            }
        }
    }

    public static class MockData {

        public static List<SelectEntity> getSelectEntityList() {
            List<SelectEntity> selectEntityList = new ArrayList<>();
            for (int i = 10; i <= 60; i += 10) {
                SelectEntity entity = new SelectEntity();
                entity.setMoney(i + "");
                entity.setIntegral(i);
                selectEntityList.add(entity);
            }
            return selectEntityList;
        }

        public static PayEntity pay(String payWay, String money) {
            String sign = "";
            return new PayEntity(sign);
        }
    }

    public static class SelectEntity {
        /**
         * money : 10.00
         * integral : 10
         */
        private boolean selected;
        private String money;
        private int integral;

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }
    }

    public static class PayEntity {

        public String sign;

        public PayEntity(String sign) {
            this.sign = sign;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }

    public static class PayUtil {

        /**
         * 支付宝支付
         */
        public static void aliPay(String sign, Callback callback) {
            if (callback != null) {
                callback.onSuccess();
            }
        }

        /**
         * 微信支付
         */
        public static void wxPay(String sign, Callback callback) {
            if (callback != null) {
                callback.onSuccess();
            }
        }
    }

    public interface Callback {
        void onSuccess();

        void onFailure();
    }
}
