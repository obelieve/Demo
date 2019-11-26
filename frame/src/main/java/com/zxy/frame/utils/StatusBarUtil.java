package com.zxy.frame.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

public class StatusBarUtil {

    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setStatusBarTranslucentStatus(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(color);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void setWindowLightStatusBar(Activity activity, boolean bool) {
        int systemUi = activity.getWindow().getDecorView().getSystemUiVisibility();
        if (bool) {
            activity.getWindow().getDecorView().setSystemUiVisibility(systemUi | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(systemUi & (~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
        }
    }

    /**
     * @return
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resId = sContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = sContext.getResources().getDimensionPixelOffset(resId);
        }
        return result;
    }

    public static void fitsSystemWindows(View view) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(), view.getPaddingRight(), view.getPaddingBottom());
    }
}
