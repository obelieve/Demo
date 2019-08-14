/*
 *
 *   Copyright (C) 2017 OrionStar Technology Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *
 */
package com.ainirobot.sdk_demo.utils;

import android.text.TextUtils;
import android.util.Log;

import com.ainirobot.coreservice.client.listener.Person;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageParser {
    private static final String TAG = MessageParser.class.getSimpleName();
    public static final String RESULT_OK = "ok";
    public static final String RESULT_FAILED = "failed";
    private static final float SWITCH_TRACKING_DISTANCE = 0.2f;
    private static final int NEAR_TRACKING = 15;

    /**
     * 获取最佳的一个人脸信息
     *
     * @param trackingPerson
     * @param personList
     * @param maxDistance
     * @return
     */
    public static Person getOnePersonWithFace(Person trackingPerson, List<Person>
            personList, double maxDistance) {
        return getOnePerson(trackingPerson, getAllPersonWithFace(personList), maxDistance);
    }

    public static Person getOnePerson(Person trackingPerson, List<Person>
            personList, double maxDistance) {
        if (null == personList || personList.size() <= 0) {
            return null;
        }

        Person person = null;
        Person tracking = null;
        Person nearTracking = null;
        for (int i = 0; i < personList.size(); i++) {
            Person newPerson = personList.get(i);
            int id = newPerson.getId();
            double distance = newPerson.getDistance();
            int angle = newPerson.getAngle();
            Log.d(TAG, "getAllPerson distance: " + distance
                    + ", id: " + id + ", angle: " + angle);
        }
        for (int i = 0; i < personList.size(); i++) {
            Person newPerson = personList.get(i);
            int id = newPerson.getId();
            double distance = newPerson.getDistance();
            int angle = newPerson.getAngle();
            if (maxDistance > 0 && distance > maxDistance) {
                continue;
            }

            if (trackingPerson != null
                    && trackingPerson.getId() == id) {
                tracking = newPerson;//与trackingPerson人脸id相等人脸
            } else {
                if (trackingPerson != null) {//找出人脸列表中，与trackingPerson角度相差最小的人脸nearTracking
                    float trackingAngle = trackingPerson.getAngle();
                    float nearAngle = (nearTracking == null ?
                            0 : nearTracking.getAngle());
                    float diffAngle = Math.abs(angle - trackingAngle);
                    float nearDiffAngle = Math.abs(nearAngle - trackingAngle);

                    if (diffAngle < NEAR_TRACKING &&
                            (nearTracking == null || diffAngle < nearDiffAngle)) {
                        if (nearTracking == null) {
                            nearTracking = newPerson;
                        }
                    }
                }

                if (person != null) {
                    //跟之前person比较，如果当前person的distance和angle都比之前大直接过滤
                    double currentDistance = person.getDistance();
                    if (currentDistance != 0
                            && (currentDistance < distance || distance == 0)) {
                        continue;
                    } else if (distance == 0) {
                        float currentAngle = person.getAngle();
                        if (Math.abs(currentAngle) < Math.abs(angle)) {
                            continue;
                        }
                    }
                }
                person = newPerson;
            }
        }

        if (person == null) {
            return tracking;
        }

        if (tracking == null) {
            if (nearTracking != null) {
                Log.e(TAG, "switch person detail: tracking angle: "
                        + trackingPerson.getAngle() +
                        ", near angle: " + nearTracking.getAngle());
            } else {
                Log.e(TAG, "switch person detail: person angle: "
                        + person.getAngle() +
                        ", distance: " + person.getDistance());
            }
            return nearTracking != null ? nearTracking : person;
        }

        double trackingDistance = tracking.getDistance();
        double minDistance = person.getDistance();
        if (trackingDistance == 0) {
            return tracking;
        } else {
            if (minDistance == 0) {
                return tracking;
            } else if (trackingDistance - minDistance > SWITCH_TRACKING_DISTANCE) {
                Log.e(TAG, "switch person " +
                        "tracking distance: " + trackingDistance +
                        ", min distance: " + minDistance);
                return person;
            } else {
                return tracking;
            }
        }
    }

    public static String parseResult(String message) {
        try {
            JSONObject json = new JSONObject(message);
            return json.getString("status");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RESULT_FAILED;
    }


    public static String parseRegisterName(String params) {
        String name = "";
        if (TextUtils.isEmpty(params) || "[]".equals(params) || "{}".equals(params)) {
            return name;
        }

        try {
            JSONObject jsonObject = new JSONObject(params);
            String slots = jsonObject.optString("slots");
            if (TextUtils.isEmpty(slots) || "[]".equals(slots) || "{}".equals(slots)) {
                name = "";
            } else {
                JSONArray start = new JSONObject(slots).optJSONArray("start");
                if (start != null) {
                    JSONObject jsonObj = start.getJSONObject(0);
                    name = jsonObj.optString("value");
                }
            }
            return name;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static Person parsePerson(String str) {
        Person person;
        Gson gson = new Gson();
        person = gson.fromJson(str, Person.class);
        return person;
    }

    /**
     * Use body info just when has not face info.
     * Body info just used to pre-wake-up.
     *
     * @param personList
     * @param maxDistance
     * @return
     */
    public static Person getOnePersonWithBody(List<Person> personList, double maxDistance) {
        if (null == personList || personList.size() <= 0) {
            return null;
        }

        Person person = null;
        for (int i = 0; i < personList.size(); i++) {
            Person newPerson = personList.get(i);
            double distance = newPerson.getDistance();
            boolean isBody = !newPerson.isWithFace() && newPerson.isWithBody();
            if (isBody && distance <= maxDistance) {
                Log.d(TAG, "hasPersonBody distance: " + distance + ", isBody: " + isBody);
                return newPerson;
            }
        }

        return person;
    }

    /**
     * Get all person with face
     */
    public static List<Person> getAllPersonWithFace(List<Person> personList) {
        if (null == personList || personList.size() <= 0) {
            return null;
        }

        List<Person> personF = new ArrayList<>();
        for (int i = 0; i < personList.size(); i++) {
            if (personList.get(i).isWithFace()) {
                personF.add(personList.get(i));
            }
        }

        return personF;
    }

    /**
     * 获取应答内容
     *
     * @param json NLP返回结果
     * @return
     */
    public static String getAnswerText(String json) {
        String answerText = "";
        try {
            JSONObject object = new JSONObject(json);
            answerText = object.optString("answerText");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return answerText;
    }

    /**
     * 获取应答内容
     *
     * @param json NLP返回结果
     * @return 用户问题
     */
    public static String getUserText(String json) {
        String answerText = "";
        try {
            JSONObject object = new JSONObject(json);
            answerText = object.optString("userText");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return answerText;
    }

    /**
     * 获取目的地
     *
     * @param json NLP返回结果
     * @return 目的地
     */
    public static String getDestination(String json) {
        String destination = "";
        try {
            JSONObject object = new JSONObject(json);
            JSONObject jsonObject = new JSONObject(object.optString("slots"));
            JSONArray jsonArray = jsonObject.optJSONArray("destination");
            JSONObject info = (JSONObject) jsonArray.get(0);
            if (info != null) {
                destination = info.optString("value");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return destination;
    }
}
