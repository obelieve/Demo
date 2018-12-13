package com.zxy.frame.utility;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by zxy on 2018/8/31 18:10.
 */

public class ResUtil
{
    private static Context sContext;

    public static void init(Context context)
    {
        sContext = context;
    }

    public static int screenWidth()
    {
        DisplayMetrics ds = sContext.getResources().getDisplayMetrics();
        return ds.widthPixels;
    }

    public static int screenHeight()
    {
        DisplayMetrics ds = sContext.getResources().getDisplayMetrics();
        return ds.heightPixels;
    }

    public static float density()
    {
        DisplayMetrics ds = sContext.getResources().getDisplayMetrics();
        return ds.density;
    }

    public static int dp2px(int dp)
    {
        DisplayMetrics ds = sContext.getResources().getDisplayMetrics();
        return (int) (ds.density * dp);
    }
}
