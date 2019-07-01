package com.zxy.demo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.demo.base.BaseFragment;
import com.zxy.demo.utils.StatusBarUtil;
import com.zxy.demo.view.CommonRowView;

/**
 * Created by zxy on 2018/10/30 10:38.
 */

public class MeFragment extends BaseFragment {

    private ConstraintLayout mClContent;
    private ImageView mIvHead;
    private ImageView mIvQrCode;
    private TextView mTvName;
    private TextView mTvPhoneNum;
    private ImageView mIvSetting;
    private ImageView mIvMsg;
    private LinearLayout mLlOrder;
    private CommonRowView mCrvAddress;
    private CommonRowView mCrvCoupon;
    private CommonRowView mCrvReceipt;
    private CommonRowView mCrvService;
    private CommonRowView mCrvRecommend;
    private CommonRowView mCrvSafety;
    private CommonRowView mCrvDeliverTime;
    private CommonRowView mCrvHelp;
    private CommonRowView mCrvRecruit;
    private CommonRowView mCrvAbout;

    @Override
    public boolean settingStatusBarLight() {
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        mClContent = view.findViewById(R.id.cl_content);
        mIvHead = view.findViewById(R.id.iv_head);
        mIvQrCode = view.findViewById(R.id.iv_qr_code);
        mTvName = view.findViewById(R.id.tv_name);
        mTvPhoneNum = view.findViewById(R.id.tv_phone_num);
        mIvSetting = view.findViewById(R.id.iv_setting);
        mIvMsg = view.findViewById(R.id.iv_msg);
        mLlOrder = view.findViewById(R.id.ll_order);
        mCrvAddress = view.findViewById(R.id.crv_address);
        mCrvCoupon = view.findViewById(R.id.crv_coupon);
        mCrvReceipt = view.findViewById(R.id.crv_receipt);
        mCrvService = view.findViewById(R.id.crv_service);
        mCrvRecommend = view.findViewById(R.id.crv_recommend);
        mCrvSafety = view.findViewById(R.id.crv_safety);
        mCrvDeliverTime = view.findViewById(R.id.crv_deliver_time);
        mCrvHelp = view.findViewById(R.id.crv_help);
        mCrvRecruit = view.findViewById(R.id.crv_recruit);
        mCrvAbout = view.findViewById(R.id.crv_about);
        StatusBarUtil.fitsSystemWindows(mClContent);
        return view;
    }
}
