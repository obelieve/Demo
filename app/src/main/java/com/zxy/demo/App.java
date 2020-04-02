package com.zxy.demo;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.qq.e.comm.managers.setting.GlobalSetting;

import java.util.HashMap;
import java.util.Map;

public class App extends Application {

    private static Map<String, Integer> mMap = new HashMap<>();

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

    @Override
    public void onCreate() {
        super.onCreate();
        AppChannelUtil.init(this);
        ApplicationInfo appInfo = null;
        try {
            appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            String appMV = appInfo.metaData.getString("applicationMetadataKey");
            if (!TextUtils.isEmpty(appMV) && mMap.containsKey(appMV)) {
                GlobalSetting.setChannel(mMap.get(appMV));
            } else {
                GlobalSetting.setChannel(999);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            GlobalSetting.setChannel(999);
        }
    }


}
