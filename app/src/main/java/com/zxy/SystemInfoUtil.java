package com.zxy;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by zxy on 2018/8/31 18:10.
 */

public class SystemInfoUtil
{
    public static int screenWidth(Context context)
    {
        DisplayMetrics ds = context.getResources().getDisplayMetrics();
        return ds.widthPixels;
    }

    public static int screenHeight(Context context)
    {
        DisplayMetrics ds = context.getResources().getDisplayMetrics();
        return ds.heightPixels;
    }

}
