package com.zxy.frame.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.zxy.frame.R;
import com.zxy.utility.SystemUtil;

import butterknife.ButterKnife;

public class SimpleAlertDialog extends BaseDialog implements View.OnClickListener {

    private TextView mTvContent;
    private Button mBtnCancel;
    private Button mBtnOk;

    View.OnClickListener mOkListener;
    View.OnClickListener mCancelListener;

    public SimpleAlertDialog(@NonNull Activity activity) {
        super(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_simple_alert, null);
        setContentView(view);
        float density = activity.getResources().getDisplayMetrics().density;
        setWidth(activity.getResources().getDisplayMetrics().widthPixels - (int) (30 * density));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setGravity(Gravity.CENTER);
        mTvContent = view.findViewById(R.id.tv_content);
        mBtnCancel = view.findViewById(R.id.btn_cancel);
        mBtnOk = view.findViewById(R.id.btn_ok);
        mBtnCancel.setOnClickListener(this);
        mBtnOk.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_cancel) {
            if (mCancelListener != null)
                mCancelListener.onClick(v);
            dismiss();
        } else if (id == R.id.btn_ok) {
            if (mOkListener != null)
                mOkListener.onClick(v);
            dismiss();
        }
    }
}
