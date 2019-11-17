package com.zxy.frame.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.zxy.demo.R;
import com.zxy.utility.SystemInfoUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SimpleAlertDialog extends BaseDialog {

    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    @BindView(R.id.btn_ok)
    Button mBtnOk;

    View.OnClickListener mOkListener;
    View.OnClickListener mCancelListener;

    public SimpleAlertDialog(@NonNull Activity activity) {
        super(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_simple_alert, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        float density = SystemInfoUtil.density(activity);
        setWidth(activity.getResources().getDisplayMetrics().widthPixels - (int) (30 * density));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setGravity(Gravity.CENTER);
    }

    public SimpleAlertDialog setContent(String content) {
        mTvContent.setText(content);
        return this;
    }

    public SimpleAlertDialog setOk(String ok) {
        mBtnOk.setText(ok);
        return this;
    }

    public SimpleAlertDialog setCancel(String cancel) {
        mBtnCancel.setText(cancel);
        return this;
    }

    public SimpleAlertDialog setOkListener(View.OnClickListener listener) {
        mOkListener = listener;
        return this;
    }

    public SimpleAlertDialog setCancelListener(View.OnClickListener listener) {
        mCancelListener = listener;
        return this;
    }

    @OnClick({R.id.btn_cancel, R.id.btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                if (mCancelListener != null)
                    mCancelListener.onClick(view);
                dismiss();
                break;
            case R.id.btn_ok:
                if (mOkListener != null)
                    mOkListener.onClick(view);
                dismiss();
                break;
        }
    }
}
