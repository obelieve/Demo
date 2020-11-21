package com.zxy.demo.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.zxy.demo.R;
import com.zxy.demo.databinding.LayoutDelOrReportBinding;
import com.zxy.frame.dialog.BaseDialog;
import com.zxy.frame.utils.info.SystemInfoUtil;


public class DelOrReportDialog extends BaseDialog {

    private LayoutDelOrReportBinding mBinding;

    public DelOrReportDialog(Activity activity) {
        super(activity, R.style.BaseBottomDialog);
        mBinding = LayoutDelOrReportBinding.inflate(LayoutInflater.from(activity));
        View view = mBinding.getRoot();
        setContentView(view);
        setWidth(SystemInfoUtil.screenWidth(mActivity));
        setGravity(Gravity.BOTTOM);
        mBinding.tvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
