package com.zxy.ui;


import com.zxy.ui.anim.AnimationFragment;
import com.zxy.ui.anim.RvAnimationFragment;

public enum UIEnum {
    M3(ViewFragment.class.getSimpleName(), ViewFragment.class,true),
    M2(RvAnimationFragment.class.getSimpleName(), RvAnimationFragment.class),
    M1(AnimationFragment.class.getSimpleName(), AnimationFragment.class);
    private String mName;
    private Class mClazz;
    private boolean mCurrent;

    UIEnum(String name, Class aClass) {
        mName = name;
        mClazz = aClass;
    }

    UIEnum(String name, Class clazz, boolean current) {
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
        for (int i = 0; i < UIEnum.values().length; i++) {
            if (UIEnum.values()[i].mCurrent) {
                return i;
            }
        }
        return 0;
    }
}
