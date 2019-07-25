package com.ainirobot.sdk_demo.skill;

import android.os.RemoteException;
import android.util.Log;

public class SkillManager {
    private static final String TAG = SkillManager.class.getSimpleName();
    private static SkillManager sSkillManager;
    public static SkillManager getInstance() {
        if (null == sSkillManager) {
            sSkillManager = new SkillManager();
        }
        return sSkillManager;
    }

    public void cancelWakeupUi() {
        try {
            sendWakeupCancel();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public void sendWakeupCancel() throws RemoteException {
        Log.d(TAG, "mSkillCallback exist " + (SpeechSkill.getInstance().getSkillApi() != null));
        if (null != SpeechSkill.getInstance().getSkillApi()) {
            Log.i(TAG, "robot speech recognize mode switch false");
            SpeechSkill.getInstance().getSkillApi().setRecognizeMode(false);
            SpeechSkill.getInstance().getSkillApi().cancleAudioOperation();
        }
    }
}
