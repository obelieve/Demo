package com.ainirobot.sdk_demo.skill;

import android.util.Log;

import com.ainirobot.sdk_demo.module.BaseModule;
import com.ainirobot.sdk_demo.module.LeadingModule;
import com.ainirobot.sdk_demo.module.WelcomeModule;

import java.util.ArrayList;
import java.util.List;

import static com.ainirobot.sdk_demo.utils.Constants.Mode.IDLE_MODE;
import static com.ainirobot.sdk_demo.utils.Constants.Mode.LEADING_MODE;
import static com.ainirobot.sdk_demo.utils.Constants.Mode.WELCOME_MODE;

public class ControlManager {
    private static final String TAG = ControlManager.class.getSimpleName();



    private static ControlManager sControlManager;
    private static List<BaseModule> moduleList = new ArrayList<>();
    /**
     *当前的任务模式，未来可以使用数据库，不同的query来匹配相应的mode来做
     * 0代表空闲模式
     * 1代表欢迎模式
     * 2代表引领模式
     */
    private int mode = IDLE_MODE;

    public static ControlManager getInstance() {
        if (null == sControlManager) {
            sControlManager = new ControlManager();
        }
        return sControlManager;
    }

    /**
     * 初始化所有任务模式
     */
    public void init(){
        moduleList.add(IDLE_MODE, new BaseModule());
        moduleList.add(WELCOME_MODE, WelcomeModule.getInstance());
        moduleList.add(LEADING_MODE, LeadingModule.getInstance());
    }
    /**
     * 获取当前的任务模式
     * @return 当前的任务模式
     */
    public int getCurrentMode() {
        return mode;
    }

    /**
     * 设置当前的任务模式
     * @param mode 设置当前的任务模式
     */
    public void setMode(int mode,String params) {
        if (mode < getCurrentMode()) {
            Log.e(TAG,"当前任务优先级比较高，不能切换模式" );
        } else if (mode == getCurrentMode()) {
            moduleList.get(mode).update(params);
        } else {
            moduleList.get(getCurrentMode()).stop();
            this.mode = mode;
            moduleList.get(mode).start(params);
        }
    }

    /**
     * 重置当前任务-空闲
     */
    public void resetMode(){
        mode = IDLE_MODE;
    }

    /**
     * 结束当前的任务
     */
    public void stopCurrentModule(){
        moduleList.get(getCurrentMode()).stop();
    }
}
