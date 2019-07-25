package com.ainirobot.sdk_demo.control;

import android.util.Log;

import com.ainirobot.coreservice.client.listener.Person;
import com.ainirobot.coreservice.client.listener.PersonInfoListener;

import java.util.ArrayList;
import java.util.List;

import com.ainirobot.sdk_demo.skill.FaceSkill;

/**
 * @author Orion
 * @time 2018/12/14
 */
public class PersonInfo {

    private static final String TAG = "PersonInfo";

    public static boolean isStartFinding = false;

    private static List<PersonInfoListener> listeners = new ArrayList<>();

    private PersonInfo() {
        listeners.clear();
    }

    public static PersonInfo getInstance() {
        return SingleHolder.INSTANCE;
    }

    private static class SingleHolder {
        public static final PersonInfo INSTANCE = new PersonInfo();
    }

    private static PersonInfoListener PERSONINFOLISTENER = new PersonInfoListener() {
        @Override
        public void onData(int code, List<Person> data) {
            Log.d(TAG, "===>> onData: " + code + " - " + data.size());
            if(data != null){
                for(Person person : data){
                    Log.d(TAG, "onData Person: " + person.toGson());
                }
            }
            for (PersonInfoListener listener : listeners) {
                listener.onData(code, data);
            }
        }

        @Override
        public void onResult(int status, String responseString) {
            Log.d(TAG, "===>> onResult: " + status + " - " + responseString);
            for (PersonInfoListener listener : listeners) {
                listener.onResult(status, responseString);
            }
        }
    };

    public void getAllPersonInfo() {
        Log.d(TAG, "getAllPersonInfo: ...");
        if (isStartFinding) {
            return;
        }
        isStartFinding = true;
        FaceSkill.getInstance().startGetAllPersonInfo(PERSONINFOLISTENER);
    }

    public void stopGetAllPersonInfo() {
        Log.d(TAG, "stopGetAllPersonInfo: ...");
        if (!isStartFinding) {
            return;
        }
        isStartFinding = false;
        FaceSkill.getInstance().stopGetAllPersonInfo(PERSONINFOLISTENER);
    }

    public synchronized void registerPersonInfoListener(PersonInfoListener personInfoListener) {
        if (listeners.contains(personInfoListener)) {
            return;
        }
        listeners.add(personInfoListener);
    }

    public synchronized void unRegisterPersonInfoListener(PersonInfoListener personInfoListener) {
        if (listeners.contains(personInfoListener)) {
            listeners.remove(personInfoListener);
        }
    }

}
