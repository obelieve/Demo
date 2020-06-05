package com.zxy.mall.utils;

import androidx.annotation.DrawableRes;

import com.zxy.mall.R;

public class MallUtil {


    public static @DrawableRes
    int getSupportTypeIcon(int type) {
        int icon;
        switch (type) {
            case 0:
            default:
                icon = R.drawable.decrease;
                break;
            case 1:
                icon = R.drawable.discount;
                break;
            case 2:
                icon = R.drawable.special;
                break;
            case 3:
                icon = R.drawable.invoice;
                break;
            case 4:
                icon = R.drawable.guarantee;
                break;
        }
        return icon;
    }
}
