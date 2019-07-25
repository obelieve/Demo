package com.ainirobot.sdk_demo.utils;

import android.util.Log;

import com.ainirobot.coreservice.client.listener.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VisualDetectUtils {

    private static final String TAG = VisualDetectUtils.class.getSimpleName();
    private static final String MESSAGE_TIMEOUT = "timeout";
    private static final int CODE_OK = 0;

    public static boolean isRemoteDetectSuccess(String jsonMsg) {
        if (jsonMsg == null || MESSAGE_TIMEOUT.equals(jsonMsg)) {
            return false;
        }

        try {
            JSONObject json = new JSONObject(jsonMsg);
            int code = json.optInt("code", -1);
            if (CODE_OK != code) {
                return false;
            }
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    public static Person updatePersonData(Person person, String jsonMsg) {
        if (person == null) {
            return null;
        }
        try {
            JSONObject json = new JSONObject(jsonMsg);
            JSONObject obj = new JSONObject(json.optString("data"));
            JSONObject personObj = new JSONObject(obj.optString("people"));
            JSONArray actions = obj.optJSONArray("actions");
            String name = personObj.optString("name");
            int age = personObj.optInt("age", 0);
            String role = personObj.optString("role");
            String gender = personObj.optString("gender");
            person.setName(name);
            person.setAge(age);
            person.setRole(role);
            person.setGender(gender);

            if (actions != null && actions.length() > 0) {
                ArrayList<Person.WelcomeAction> list = new ArrayList<>();
                for (int i = 0; i < actions.length(); i++) {
                    try {
                        JSONObject object = actions.getJSONObject(i);
                        Person.WelcomeAction action = person.obtainNewWelcomeAction();
                        if ("play_tts".equals(object.optString("action"))) {
                            action.setAction("play_tts");
                            JSONObject object1 = new JSONObject(object.optString("tts"));
                            action.setValue(object1.optString("value"));
                        } else if("query_by_text".equals(object.optString("action"))){
                            action.setAction("query_by_text");
                            action.setValue(object.optString("param"));
                            action.setConfirmTTS(object.optString("confirm_tts"));
                            action.setHelloTTS(object.optString("hello_tts"));
                            action.setIsConfirm(object.optInt("enable_confirm"));
                        }
                        list.add(action);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                person.setWelcomeActions(list);
            }
            Log.d(TAG, "updatePersonData name:" + name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return person;
    }

    public static void deletePictures(final List<String> fileNames) {
        if (fileNames == null || fileNames.size() <= 0) {
            Log.i(TAG, "file name length error, return!!");
            return;
        }
        for (String filename : fileNames) {
            File file = new File(filename);
            if(file.exists() && file.isFile()){
                Log.i(TAG, "delete file:"+filename);
                file.delete();
            }else {
                Log.i(TAG, "no exits, file name:"+fileNames);
            }
        }
    }
}
