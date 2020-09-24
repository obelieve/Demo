package com.news.mediaplayer.utils;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

/**
 * 亮度控制
 * Created by Admin
 * on 2020/9/24
 */
public class BrightnessUtil {


    /**
     * 设置当前APP的亮度
     *
     * @param activity
     * @param brightnessPercent   0 to 1 adjusts the brightness from dark to full bright
     */
    public static void setAppBrightness(Activity activity, float brightnessPercent)
    {
        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.screenBrightness = brightnessPercent;
        window.setAttributes(layoutParams);
    }

    /**
     * 获取当前页面亮度
     * @return
     */
    public static float getAppBrightness(Context context)
    {
        float brightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        if(context instanceof Activity)
        {
            Window window = ((Activity)context).getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            brightness = layoutParams.screenBrightness;
        }
        if(brightness == WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE)
        {
            brightness = getSystemBrightness(context) / 255f;
        }
        return brightness;
    }

    /**
     * 获取系统亮度
     */
    public static int getSystemBrightness(Context context)
    {
        return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 255);
    }
}
