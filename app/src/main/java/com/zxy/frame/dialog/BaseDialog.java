package com.zxy.frame.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.zxy.demo.R;


public class BaseDialog {

    protected View mContentView;
    protected Dialog mDialog;
    protected Activity mActivity;

    public BaseDialog(Activity activity) {
        mActivity = activity;
        mDialog = new Dialog(activity, R.style.BaseDialog);
    }

    public Activity getActivity() {
        return mActivity;
    }

    public BaseDialog setWidth(int width) {
        if (mContentView != null) {
            ViewGroup.LayoutParams params = mContentView.getLayoutParams();
            params.width = width;
        }
        return this;
    }

    public BaseDialog setHeight(int height) {
        if (mContentView != null) {
            ViewGroup.LayoutParams params = mContentView.getLayoutParams();
            params.height = height;
        }
        return this;
    }

    public BaseDialog setContentView(View view) {
        mContentView = view;
        mDialog.setContentView(view);
        return this;
    }

    public BaseDialog setGravity(int gravity) {
        mDialog.getWindow().setGravity(gravity);
        return this;
    }

    public BaseDialog setCancelable(boolean cancelable) {
        mDialog.setCancelable(cancelable);
        return this;
    }

    public BaseDialog setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public BaseDialog setOnDismissListener(DialogInterface.OnDismissListener listener) {
        mDialog.setOnDismissListener(listener);
        return this;
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }
}
