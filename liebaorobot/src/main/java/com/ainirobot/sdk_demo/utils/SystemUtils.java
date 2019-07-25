package com.ainirobot.sdk_demo.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

/**
 * @author Orion
 * @time 2018/9/11
 */
public class SystemUtils {

    public static boolean isServiceRunning(Class cls, Context context) {
        if (cls == null || TextUtils.isEmpty(cls.getName())) {
            return false;
        }

        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        try {
            //此过时方法对自身依然有效，Android8.0以后，对第三方APP无效
            List<ActivityManager.RunningServiceInfo> list = manager
                    .getRunningServices(50);
            if (list != null && list.size() > 0) {
                for (ActivityManager.RunningServiceInfo info : list) {
                    if (cls.getName().equals(info.service.getClassName())) {
                        return true;
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return false;
    }

}
