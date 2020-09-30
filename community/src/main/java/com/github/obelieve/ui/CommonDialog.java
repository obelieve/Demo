package com.github.obelieve.ui;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import androidx.annotation.NonNull;

import com.zxy.frame.dialog.SimpleAlertDialog;

/**
 * Created by Admin
 * on 2020/8/26
 */
public class CommonDialog extends SimpleAlertDialog {

    public CommonDialog(@NonNull Activity activity) {
        super(activity);
    }


    public CommonDialog setContent(String content) {
        super.setContent(content);
        return this;
    }

    public CommonDialog setSimple(boolean simple) {
        super.setSimple(simple);
        return this;
    }

    public CommonDialog setPositiveButton(String ok, onSubmitListener listener) {
        setOk(ok);
        setOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickSubmit(mDialog);
                }
            }
        });
        return this;
    }

    public CommonDialog setNegativeButton(String cancel, onCancelListener listener) {
        setCancel(cancel);
        setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClickCancel(mDialog);
                }
            }
        });
        return this;
    }

    public interface onCancelListener {
        void onClickCancel(Dialog dialog);
    }

    public interface onSubmitListener {
        void onClickSubmit(Dialog dialog);
    }
}
