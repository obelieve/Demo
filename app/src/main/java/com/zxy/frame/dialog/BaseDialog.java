package com.zxy.frame.dialog;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.zxy.demo.R;


public class BaseDialog extends Dialog {

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.BaseDialog);
    }

    public BaseDialog gravity(int gravity) {
        getWindow().setGravity(gravity);
        return this;
    }

    public BaseDialog canceledOnTouchOutside(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
        return this;
    }
}
