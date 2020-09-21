package com.news.project;

import android.app.Activity;

import com.qq.e.ads.nativ.NativeADUnifiedListener;
import com.qq.e.ads.nativ.NativeUnifiedAD;

import java.util.Random;

/**
 * Created by Admin
 * on 2020/9/21
 */
public class ADManager {

    /**
     * 请求获取原生自渲染 数据
     * @param activity
     * @param listener
     */
    public static void reuqestNativeUnifiedAD(Activity activity, NativeADUnifiedListener listener){
        NativeUnifiedAD nativeAD = new NativeUnifiedAD(activity, Constants.APPID,
                PositionId.INFORMATION_POS_ID[new Random().nextInt(PositionId.INFORMATION_POS_ID.length - 1)],listener);
        nativeAD.loadData(PositionId.INFORMATION_POS_ID.length);
    }
}
