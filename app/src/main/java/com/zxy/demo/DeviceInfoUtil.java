package com.zxy.demo;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by zxy on 2018/10/15 09:22.
 */

public class DeviceInfoUtil
{
    private static Context mContext;
    private static DisplayMetrics mDisplayMetrics;

    public static void init(Context context)
    {
        mContext = context;
    }

    private static DisplayMetrics getDisplayMetric()
    {
        if (mContext == null)
        {
            throw new NullPointerException("need initial init(Context)!");
        }
        if (mDisplayMetrics == null)
        {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(dm);
            mDisplayMetrics = dm;
        }
        return mDisplayMetrics;
    }

    public static int screenWidth()
    {
        return getDisplayMetric().widthPixels;
    }

    public static int screenHeight()
    {
        return getDisplayMetric().heightPixels;
    }

    public static float density()
    {
        return getDisplayMetric().density;
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * density());
    }
}
