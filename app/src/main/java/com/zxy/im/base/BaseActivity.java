package com.zxy.im.base;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by zxy on 2018/12/12 14:30.
 */

public class BaseActivity extends AppCompatActivity
{
    ProgressDialog mProgressDialog;

    protected void showProgressDialog(String msg)
    {
        if (mProgressDialog == null)
        {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    protected void dismissProgressDialog()
    {
        if (mProgressDialog != null && mProgressDialog.isShowing())
        {
            mProgressDialog.dismiss();
        }
    }
}
