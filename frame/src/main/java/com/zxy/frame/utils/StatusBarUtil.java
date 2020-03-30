package com.zxy.frame.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

public class StatusBarUtil {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setStatusBarTranslucentStatus(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(color);
    }

    /**
     * 设置状态栏字色和图标
     *
     * @param activity
     * @param light    状态栏是否是浅色  是：状态栏字色和图标为黑色，否：状态栏字色和图标为白色
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void setWindowLightStatusBar(Activity activity, boolean light) {
        int systemUi = activity.getWindow().getDecorView().getSystemUiVisibility();
        if (light) {
            activity.getWindow().getDecorView().setSystemUiVisibility(systemUi & (~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(systemUi | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }
        return result;
    }

    public static void fitsSystemWindows(Context context, View view) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context), view.getPaddingRight(), view.getPaddingBottom());
    }
}
