package com.ainirobot.sdk_demo.model;

import android.util.Log;

/**
 * @author Orion
 * @time 2018/9/12
 */
public class BaseSkill {

    private static final String TAG = "BaseSkill";

    private String skillName;

    public BaseSkill(String skillName) {
        this.skillName = skillName;
    }

    protected void mySkill() {
        Log.d(TAG, "mySkill: " + this.skillName);
    }
}
