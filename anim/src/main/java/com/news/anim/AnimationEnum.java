package com.news.anim;


public enum AnimationEnum {

    M2(RvAnimationFragment.class.getSimpleName(), RvAnimationFragment.class,true),
    M1(AnimationFragment.class.getSimpleName(), AnimationFragment.class);
    private String mName;
    private Class mClazz;
    private boolean mCurrent;

    AnimationEnum(String name, Class aClass) {
        mName = name;
        mClazz = aClass;
    }

    AnimationEnum(String name, Class clazz, boolean current) {
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
        for (int i = 0; i < AnimationEnum.values().length; i++) {
            if (AnimationEnum.values()[i].mCurrent) {
                return i;
            }
        }
        return 0;
    }
}
