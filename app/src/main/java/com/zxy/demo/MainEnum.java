package com.zxy.demo;

import com.zxy.demo._issue.ForTAFragment;
import com.zxy.demo._issue.LoginFragment;
import com.zxy.demo._issue.SplashFragment;
import com.zxy.demo._issue.CustomTabFragment;
import com.zxy.demo._issue.VersionUpdateEntranceFragment;
import com.zxy.demo._issue.ZDialogFragment;
import com.zxy.demo.fragment.MainFragment;

public enum MainEnum {

    M1("加载刷新", MainFragment.class),
    M2("Dialog", ZDialogFragment.class),
    M3("加载刷新", ForTAFragment.class),
    M4("@其他人", ForTAFragment.class),
    M5("引导页", SplashFragment.class),
    M6("登录", LoginFragment.class),
    M7("版本升级", VersionUpdateEntranceFragment.class),
    M8("自定义Tab", CustomTabFragment.class)
    ;
    private String mName;
    private Class mClazz;

    MainEnum(String name, Class aClass) {
        mName = name;
        mClazz = aClass;
    }

    public String getName() {
        return mName;
    }

    public Class getClazz() {
        return mClazz;
    }

    @Override
    public String toString() {
        return "MainEnum{" +
                "mName='" + mName + '\'' +
                ", mClazz=" + mClazz +
                '}';
    }
}
