package com.zxy.demo;

import com.zxy.demo._issue.CustomTabFragment;
import com.zxy.demo._issue.ForTAFragment;
import com.zxy.demo._issue.LoginFragment;
import com.zxy.demo._issue.LottieAnimationFragment;
import com.zxy.demo._issue.SplashFragment;
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
    M8("自定义Tab", CustomTabFragment.class),
    M9("Lottie动画", LottieAnimationFragment.class,true);
    private String mName;
    private Class mClazz;
    private boolean mCurrent;

    MainEnum(String name, Class aClass) {
        mName = name;
        mClazz = aClass;
    }

    MainEnum(String name, Class clazz, boolean current) {
        mName = name;
        mClazz = clazz;
        mCurrent = current;
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

    public static int getCurrentIndex() {
        for (int i = 0; i < MainEnum.values().length; i++) {
            if (MainEnum.values()[i].mCurrent) {
                return i;
            }
        }
        return 0;
    }
}
