package com.zxy.demo;

import com.zxy.demo.fragment.CustomTabFragment;
import com.zxy.demo.fragment.FilterListFragment;
import com.zxy.demo.fragment.ForTAFragment;
import com.zxy.demo.fragment.ListContainerFragment;
import com.zxy.demo.fragment.LoginFragment;
import com.zxy.demo.fragment.LottieAnimationFragment;
import com.zxy.demo.fragment.PayFragment;
import com.zxy.demo.fragment.SplashFragment;
import com.zxy.demo.fragment.StickyCoordinatorFragment;
import com.zxy.demo.fragment.VersionUpdateEntranceFragment;
import com.zxy.demo.fragment.ViewRenderFragment;
import com.zxy.demo.fragment.ZDialogFragment;
import com.zxy.demo.fragment.ZWebViewFragment;
import com.zxy.demo.test.TestFragment;
import com.zxy.demo.test.ui.UITestFragment;

public enum MainEnum {

    M15("UITestFragment", UITestFragment.class,true),
    M14("TestFragment", TestFragment.class),
    M13("控件效果", ViewRenderFragment.class),
    M12("自定义Tab", CustomTabFragment.class),
    M11("列表显示", ListContainerFragment.class),
    M10("列表筛选", FilterListFragment.class),
    M9("加载WebView", ZWebViewFragment.class),
    M8("Lottie动画", LottieAnimationFragment.class),
    M7("支付页面", PayFragment.class),
    M6("版本升级", VersionUpdateEntranceFragment.class),
    M5("登录", LoginFragment.class),
    M4("引导页", SplashFragment.class),
    M3("@其他人", ForTAFragment.class),
    M2("Dialog", ZDialogFragment.class),
    M1("置顶&&加载刷新", StickyCoordinatorFragment.class);
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
