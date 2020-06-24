package com.zxy.demo;

import android.Manifest;
import android.app.Activity;
import android.os.Build;

/**
 * Created by Admin
 * on 2020/6/15
 */
public class Util {

    public static void requestPermission(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
    }
    /**
     * 存放播放的区域：adb push C:\Users\Administrator\Desktop\1.mp4 /sdcard/Download/1.mp4
     * @return
     */
    public static String getVideoPath() {
//        return Environment.getExternalStorageDirectory() + File.separator + "Download" + File.separator + "1.avi";
        return "https://image.imjihua.com/video/video_1.mp4";
        //return "http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8";
        //return "https://yy.hongbaoguoji.com/live/p5u13687stream.m3u8?txSecret=f74b119291511e24fedc6d995ddeab8d&txTime=5ef20be0";
        //return "https://yy.hongbaoguoji.com/live/p5u342stream.m3u8?txSecret=a22ea8f0f2be667f275d79b9c05f39d5&txTime=5ef30c8a";
    }
}
