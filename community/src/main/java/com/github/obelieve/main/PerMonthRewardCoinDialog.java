package com.github.obelieve.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.obelieve.community.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin
 * on 2020/5/12
 */
public class PerMonthRewardCoinDialog extends Dialog {


    @BindView(R.id.tv_coin_num)
    TextView tvCoinNum;
    @BindView(R.id.tv_open)
    TextView tvOpen;
    @BindView(R.id.iv_close)
    ImageView ivClose;

    private Context mContext;
    private Callback mCallback;
    private int mNum;

    public PerMonthRewardCoinDialog(@NonNull Context context) {
        super(context, R.style.BaseDialog);
        mContext = context;
        if (getWindow() != null) {
            getWindow().setDimAmount(0.8f);
        }
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        //填充对话框的布局
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_per_month_reward_coin, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        tvCoinNum.setText(mNum+"金币");
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = getContext().getResources().getDisplayMetrics().widthPixels;
        view.setLayoutParams(layoutParams);
    }
    
    public PerMonthRewardCoinDialog setNum(int num) {
        mNum = num;
        return this;
    }

    @OnClick({R.id.tv_open, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_open:
                if (mCallback != null) {
                    mCallback.onOpen();
                }
                break;
            case R.id.iv_close:
                dismiss();
                if (mCallback != null) {
                    mCallback.onClose();
                }
                break;
        }
    }


    public interface Callback {
        void onOpen();
        void onClose();
    }
}
