package com.zxy.demo;

import com.zxy.demo.fragment.CustomTabFragment;
import com.zxy.demo.fragment.FilterListFragment;
import com.zxy.demo.fragment.ForTAFragment;
import com.zxy.demo.fragment.LoginFragment;
import com.zxy.demo.fragment.LottieAnimationFragment;
import com.zxy.demo.fragment.SplashFragment;
import com.zxy.demo.fragment.StickyCoordinatorFragment;
import com.zxy.demo.fragment.VersionUpdateEntranceFragment;
import com.zxy.demo.fragment.ViewRenderFragment;
import com.zxy.demo.fragment.ZDialogFragment;
import com.zxy.demo.fragment.ZWebViewFragment;

public enum MainEnum {

    M1("置顶&&加载刷新", StickyCoordinatorFragment.class),
    M2("Dialog", ZDialogFragment.class),
    M3("@其他人", ForTAFragment.class),
    M4("引导页", SplashFragment.class),
    M5("登录", LoginFragment.class),
    M6("版本升级", VersionUpdateEntranceFragment.class),
    M7("自定义Tab", CustomTabFragment.class),
    M8("Lottie动画", LottieAnimationFragment.class),
    M9("加载WebView", ZWebViewFragment.class),
    M10("列表筛选", FilterListFragment.class),
    M11("控件效果", ViewRenderFragment.class,true);
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
