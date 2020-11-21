package com.zxy.demo.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.zxy.demo.R;
import com.zxy.demo.databinding.LayoutCommentDialogBinding;
import com.zxy.frame.dialog.BaseDialog;
import com.zxy.frame.utils.info.SystemInfoUtil;

public class SendCommentDialog extends BaseDialog {

    LayoutCommentDialogBinding mBinding;

    public SendCommentDialog(Activity activity) {
        super(activity, R.style.BaseBottomDialog);
        mBinding = LayoutCommentDialogBinding.inflate(LayoutInflater.from(activity));
        setContentView(mBinding.getRoot());
        setWidth(SystemInfoUtil.screenWidth(mActivity));
        setHeight((int) SystemInfoUtil.density(mActivity) * 50);
        setGravity(Gravity.BOTTOM);
        mBinding.editComment.requestFocus();
        mBinding.editComment.post(() -> {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        });
        mBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
