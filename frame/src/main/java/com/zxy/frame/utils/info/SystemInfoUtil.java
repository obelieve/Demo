package com.zxy.frame.utils.info;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.View.NO_ID;

/**
 * Created by zxy on 2018/8/31 18:10.
 */

public class SystemInfoUtil {

    public static int screenWidth(Context context) {
        DisplayMetrics ds = context.getResources().getDisplayMetrics();
        return ds.widthPixels;
    }

    /**
     * 1.屏幕内导航栏不存在时，height = 屏幕真实高度-状态栏高度
     * 2.屏幕内导航栏存在时，height = 屏幕真实高度-状态栏高度-导航栏高度
     *
     * @return
     */
    public static int screenHeight(Context context) {
        DisplayMetrics ds = context.getResources().getDisplayMetrics();
        return ds.heightPixels;
    }

    public static float density(Context context) {
        DisplayMetrics ds = context.getResources().getDisplayMetrics();
        return ds.density;
    }

    public static int dp2px(Context context, int size) {
        DisplayMetrics ds = context.getResources().getDisplayMetrics();
        return (int) ds.density * size;
    }

    public static void settingFullScreen(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            activity.getWindow().setAttributes(lp);
        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        }
        return result;
    }

    public static int getNavigationHeight(Context context) {
        int resourceId = 0;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    /**
     * 获取屏幕真实高度，屏幕可显示的全部区域
     *
     * @param activity
     * @return
     */
    public static int getRealHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        }
        return dm.heightPixels;
    }

    /**
     * 需要在View完全被绘制出来之后调用
     *
     * @param activity
     * @return
     */
    public static boolean isNavigationBarExist(Activity activity) {
        final String NAVIGATION = "navigationBarBackground";
        ViewGroup vp = (ViewGroup) activity.getWindow().getDecorView();
        if (vp != null) {
            for (int i = 0; i < vp.getChildCount(); i++) {
                vp.getChildAt(i).getContext().getPackageName();
                if (vp.getChildAt(i).getId() != NO_ID && NAVIGATION.equals(activity.getResources().getResourceEntryName(vp.getChildAt(i).getId()))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 键盘是否显示
     *
     * @param activity
     * @return
     */
    public static boolean isShowInput(Activity activity) {
        //获取当屏幕内容的高度
        int screenHeight = activity.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        //DecorView即为activity的顶级view
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        //选取screenHeight*2/3进行判断
        return screenHeight * 2 / 3 > rect.bottom;
    }

    /**
     * 键盘切换 显示/隐藏
     */
    public static void toggleKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 显示键盘
     *
     * @param et 输入焦点
     */
    public static void showInput(Context context, final EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 隐藏键盘
     *
     * @param activity
     */
    public static void hideInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        View v = activity.getWindow().peekDecorView();
        if (null != v) {
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }

}
