package com.zxy.demo.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.zxy.demo.R;
import com.zxy.frame.dialog.BaseDialog;
import com.zxy.utility.SystemUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
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
        ButterKnife.bind(this,view);
        setContentView(view);
        setWidth(SystemUtil.screenWidth());
        setHeight((int) SystemUtil.density() * 50);
        setGravity(Gravity.BOTTOM);
        mEditComment.requestFocus();
        mEditComment.post(() -> {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        });
    }

    @OnClick(R.id.btn_ok)
    public void onViewClicked() {
    }
}
