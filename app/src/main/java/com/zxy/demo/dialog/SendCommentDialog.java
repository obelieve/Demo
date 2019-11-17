package com.zxy.demo.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.zxy.demo.R;
import com.zxy.frame.dialog.BaseDialog;
import com.zxy.utility.SystemInfoUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class SendCommentDialog extends BaseDialog {

    @BindView(R.id.edit_comment)
    EditText mEditComment;
    @BindView(R.id.btn_ok)
    Button mBtnOk;

    public SendCommentDialog(Activity activity) {
        super(activity, R.style.BaseBottomDialog);
        View view;
        view = LayoutInflater.from(activity).inflate(R.layout.layout_comment_dialog, null);
        setContentView(view);
        setWidth(SystemInfoUtil.screenWidth(activity));
        setHeight((int) SystemInfoUtil.density(activity) * 50);
    }

    @OnClick(R.id.btn_ok)
    public void onViewClicked() {
    }
}
