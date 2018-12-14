package com.zxy.frame.utility;

import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by zxy on 2018/12/14 11:48.
 */

public class ToastUtil
{
    public static void show(String msg)
    {
        Toast.makeText(UContext.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void show(@StringRes int id)
    {
        Toast.makeText(UContext.getContext(), UContext.getContext().getString(id), Toast.LENGTH_SHORT).show();
    }
}
