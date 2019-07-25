package com.ainirobot.sdk_demo;

import android.app.Application;

import com.ainirobot.sdk_demo.skill.ControlManager;

/**
 * @author Orion
 * @time 2018/12/12
 */
public class RobotApplication extends Application {

    private static RobotApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        /**
         * 初始化控制器
         */
        ControlManager.getInstance().init();
    }

    public static RobotApplication getInstance() {
        return INSTANCE;
    }

}
