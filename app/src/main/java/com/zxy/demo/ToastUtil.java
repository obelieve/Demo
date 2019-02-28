package com.zxy.demo;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by admin on 2019/2/28.
 */

public class ToastUtil {
    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    public static void show(String s) {
        Toast.makeText(sContext, s, Toast.LENGTH_SHORT).show();
    }
}
