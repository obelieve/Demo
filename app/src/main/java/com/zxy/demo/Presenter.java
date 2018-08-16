package com.zxy.demo;

import android.view.View;

import com.zxy.utility.LogUtil;

/**
 * Created by zxy on 2018/8/15 14:56.
 */

public class Presenter {
    public void onSaveClick(View view){
        LogUtil.e("user"+" onSaveClick"+view);
    }
}
