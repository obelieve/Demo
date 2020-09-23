package com.github.obelieve.utils.others;

import android.os.Environment;

/**
 * Created by TQ on 2017/11/6.
 */

public class SystemConstant {

    //app类型
    public static String MT = "android";
    //百度Crab key
    public static String CRAB_KEY = "4f78b291cf41b4d1";
    //小米推送 APP_ID
    public static final String APP_ID_MI = "";
    // 小米推送 APP_KEY
    public static final String APP_KEY_MI = "";

    // oppo 推送
    public static final String APP_ID_OPPO = "";
    public static final String APP_KEY_OPPO = "";


    public static final String WX_APPID = "wxc269e9064942a59d";
    public static final String WX_AppSecret = "6123f2809dccba2928f10ad4c468358f";

    //QQ
    public static final String QQ_APPID = "1109774533";
    public static final String QQ_APPKEY = "RmD0Tm3GtypsiZ6J";

    //友盟 APP_KEY
    public static final String APP_KEY_U = "";

    //基本路径
    public static String BASE_PATH = Environment.getExternalStorageDirectory() + "/Plan/";
    //图片路径
    public static String TEMP_IMAGE_PATH = BASE_PATH + "temp/";
    //安装包路径
    public static String TEMP_APK_PATH = BASE_PATH + "apk/";


    /**************************************** 时间常量 ******************************************/
    /** 一秒 */
    public static long SECOND = 1000L;
    /** 一分 */
    public static long MINUTE = 60 * SECOND;
    /** 一小时 */
    public static long HOUR = 60 * MINUTE;
    /** 一天 */
    public static long DAY = 24 * HOUR;
    /** 一周 */
    public static long WEEK = DAY * 7;
    /** 半天 */
    public static long HALF_DAY = 12 * HOUR;
    /** 一月 */
    public static long MONTH = 30 * DAY;
    /** 一年 */
    public static long YEAR = 12 * MONTH;

}
