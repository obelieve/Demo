package com.zxy.mall.utils;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;

import com.zxy.mall.R;

public class MallUtil {


    public static @DrawableRes
    int getSupportTypeIcon(int type) {
        if (type == 0) {
            return R.drawable.decrease_white;
        }
        return 0;
    }
}
