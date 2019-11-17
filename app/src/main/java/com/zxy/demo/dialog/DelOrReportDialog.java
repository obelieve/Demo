package com.zxy.demo.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.frame.dialog.BaseDialog;
import com.zxy.utility.SystemInfoUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DelOrReportDialog extends BaseDialog {

    @BindView(R.id.tv_report)
    TextView mTvReport;
    @BindView(R.id.tv_cancel)
    TextView mTvCancel;

    public DelOrReportDialog(Activity activity) {
        super(activity, R.style.BaseBottomDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_del_or_report, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        setWidth(SystemInfoUtil.screenWidth(activity));
        setGravity(Gravity.BOTTOM);
    }

    @OnClick({R.id.tv_report, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_report:
                dismiss();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }
}
