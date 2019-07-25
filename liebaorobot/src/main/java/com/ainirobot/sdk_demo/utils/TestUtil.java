package com.ainirobot.sdk_demo.utils;

import com.ainirobot.sdk_demo.model.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import static com.ainirobot.sdk_demo.utils.Constants.FRAGMENT_TYPE.UPDATE_API;

public class TestUtil {
    /**
     * 单个技能模式
     */
    public static int SINGLE_MODE = 1;
    /**
     * 场景案例模式
     */
    public static int SCENE_MODE = 2;

    /**
     * 测试模式
     * 1 单个技能测试模式
     * 2 场景案例测试模式
     */
    private static  int testMode = SINGLE_MODE;

    /**
     * 是否是场景案例测试模式
     * @return true 是，false 不是
     */
    public static boolean isSceneMode() {
        return testMode == SCENE_MODE;
    }

    public static void setTestMode(int mode) {
        testMode = mode;
    }

    /**
     * EventBus
     */
    public static void updateApi(String apiName){
        MessageEvent messageEvent = new MessageEvent(UPDATE_API);
        messageEvent.setWhat(apiName);
        EventBus.getDefault().post(messageEvent);
    }
}
