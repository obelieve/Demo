package com.zxy.frame.dialog;

import android.app.Activity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.zxy.frame.R;
import com.zxy.utility.SystemUtil;

public class LoadDialog extends BaseDialog {

    public LoadDialog(@NonNull Activity activity) {
        super(activity,R.style.LoadingDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_loading, null);
        setContentView(view);
        SystemUtil.init(activity.getApplicationContext());
        setWidth(SystemUtil.screenWidth());
        setHeight(SystemUtil.getRealHeight(activity));
        setGravity(Gravity.CENTER);
    }

}
