package com.ainirobot.sdk_demo.skill;

import android.os.RemoteException;
import android.util.Log;

import com.ainirobot.coreservice.client.ApiListener;
import com.ainirobot.coreservice.client.listener.TextListener;
import com.ainirobot.coreservice.client.speech.SkillApi;
import com.ainirobot.coreservice.client.speech.SkillCallback;

import java.util.ArrayList;
import java.util.List;

import com.ainirobot.sdk_demo.RobotApplication;
import com.ainirobot.sdk_demo.model.BaseSkill;


/**
 * @author Orion
 * @time 2018/9/11
 */
public class SpeechSkill extends BaseSkill {

    private static final String TAG = "SpeechSkill";

    private SkillApi skillApi;
    private List<OnSpeechCallBack> callbackList;
    private boolean isConnected = false;

    private SkillCallback skillCallback = new SkillCallback() {

        @Override
        public void onSpeechParResult(final String s) throws RemoteException {
            //用户说话临时识别结果
            Log.d(TAG, "onSpeechParResult: " + s);
            for (OnSpeechCallBack callback : callbackList) {
                callback.onSpeechParResult(s);
            }
        }

        @Override
        public void onStart() throws RemoteException {
            //用户开始说话
            Log.d(TAG, "onStart: ");
            for (OnSpeechCallBack callback : callbackList) {
                callback.onStart();
            }
        }

        @Override
        public void onStop() throws RemoteException {
            //用户说话结束
            Log.d(TAG, "onStop: ");
            for (OnSpeechCallBack callback : callbackList) {
                callback.onStop();
            }
        }

        @Override
        public void onVolumeChange(int i) throws RemoteException {
            //用户说话声音大小变化
            for (OnSpeechCallBack callback : callbackList) {
                callback.onVolumeChange(i);
            }
        }

        @Override
        public void onQueryEnded(int i) throws RemoteException {
            Log.d(TAG, "onQueryEnded: ");
            for (OnSpeechCallBack callback : callbackList) {
                callback.onQueryEnded(i);
            }
        }
    };


    private SpeechSkill() {
        super(TAG);
        if (skillApi == null) {
            skillApi = new SkillApi();
            callbackList = new ArrayList<>();
        }
    }

    public void connetApi() {
        final ApiListener apiListener = new ApiListener() {
            @Override
            public void handleApiDisabled() {
                Log.d(TAG, "handleApiDisabled: SpeechApi");
                isConnected = false;
            }

            @Override
            public void handleApiConnected() {
                //注册语音事件回调
                Log.d(TAG, "handleApiConnected: SpeechApi");
                isConnected = true;
                skillApi.registerCallBack(skillCallback);
                skillApi.setASREnabled(true);
                skillApi.setRecognizable(true);
            }

            @Override
            public void handleApiDisconnected() {
                Log.d(TAG, "handleApiDisconnected: SpeechApi");
                isConnected = false;
                skillApi.connectApi(RobotApplication.getInstance());
            }
        };
        skillApi.addApiEventListener(apiListener);
        skillApi.connectApi(RobotApplication.getInstance());
    }

    public static SpeechSkill getInstance() {
        return SingleHolder.INSTANCE;
    }

    private static class SingleHolder {
        public static final SpeechSkill INSTANCE = new SpeechSkill();
    }

    public void playTxt(String text) {
        skillApi.playText(text, null);
    }

    public void playTxt(String text, TextListener textListener) {
        skillApi.playText(text, textListener);
    }

    public void release() {
        if (isConnected) {
            skillApi.unregisterCallBack(skillCallback);
        }
    }

    public SkillApi getSkillApi() {
        return skillApi;
    }

    public synchronized void addCallBack(OnSpeechCallBack skillCallback) {
        if (this.callbackList.contains(skillCallback)) {
            return;
        }
        this.callbackList.add(skillCallback);
    }

    public synchronized void removeCallBack(OnSpeechCallBack skillCallback) {
        this.callbackList.remove(skillCallback);
    }

    public boolean isConnected() {
        return isConnected;
    }

    public interface OnSpeechCallBack {

        void onSpeechParResult(String var1);

        void onStart();

        void onStop();

        void onVolumeChange(int var1);

        void onQueryEnded(int var1);
    }

}
