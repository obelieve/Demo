package com.ainirobot.sdk_demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ainirobot.sdk_demo.skill.SpeechSkill;


/**
 * @author Orion
 * @time 2018/10/30
 */
public class SpeechService extends Service {

    private static final String TAG = "SpeechService";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: connect speech server.");
        SpeechSkill.getInstance().connetApi();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SpeechSkill.getInstance().release();
    }
}
