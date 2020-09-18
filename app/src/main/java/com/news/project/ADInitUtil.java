package com.news.project;

import android.content.Context;

import com.qq.e.comm.managers.setting.GlobalSetting;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin
 * on 2020/9/16
 */
public class ADInitUtil {

    private static Map<String, Integer> mMap = new HashMap<>();
    private static String[] sComponentPkgAndCls =new String[2];

    static {
        mMap.put("baidumarket", 1);
        mMap.put("头条", 2);
        mMap.put("广点通", 3);
        mMap.put("搜狗", 4);
        mMap.put("其他网盟", 5);
        mMap.put("oppomarket", 6);
        mMap.put("vivomarket", 7);
        mMap.put("huaweimarket", 8);
        mMap.put("yingyongbao", 9);
        mMap.put("xiaomimarket", 10);
        mMap.put("金立", 11);
        mMap.put("百度手机助手", 12);
        mMap.put("meizu", 13);
        mMap.put("AppStore", 14);
        mMap.put("其他", 15);
    }

    /**
     *
     * @param context
     * @param pkg 包名
     * @param cls
     */
    public static void init(Context context,String pkg,String cls){
        sComponentPkgAndCls[0]=pkg;
        sComponentPkgAndCls[1]=cls;
        AppChannelUtil.init(context);
//        ApplicationInfo appInfo = null;
//        try {
//            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
//            String appMV = appInfo.metaData.getString("applicationMetadataKey");
//            if (!TextUtils.isEmpty(appMV) && mMap.containsKey(appMV)) {
//                GlobalSetting.setChannel(mMap.get(appMV));
//            } else {
//                GlobalSetting.setChannel(999);
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//            GlobalSetting.setChannel(999);
//        }
        GlobalSetting.setChannel(1);
    }

    public static String[] getComponentPkgAndCls() {
        return sComponentPkgAndCls;
    }
}
