package com.zxy.demo.utils;

import android.widget.Toast;

import com.zxy.demo.base.App;

public class ToastUtil {

    public static void show(String msg) {
        Toast.makeText(App.getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
