package com.zxy.demo;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zxy on 2019/08/21.
 */
public class ToastUtil {

    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    public static void show(String msg) {
        Toast.makeText(sContext, msg, Toast.LENGTH_SHORT).show();
    }

}
