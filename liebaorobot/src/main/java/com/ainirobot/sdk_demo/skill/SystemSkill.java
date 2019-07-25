package com.ainirobot.sdk_demo.skill;

import android.os.RemoteException;
import android.util.Log;

import com.ainirobot.coreservice.client.Definition;
import com.ainirobot.coreservice.client.RobotApi;
import com.ainirobot.coreservice.client.listener.ActionListener;
import com.ainirobot.coreservice.client.listener.CommandListener;
import com.ainirobot.sdk_demo.model.BaseSkill;
import com.ainirobot.sdk_demo.utils.Constants;
import com.ainirobot.sdk_demo.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class SystemSkill extends BaseSkill {

    private static final String TAG = SystemSkill.class.getSimpleName();

    private static final String PATH = "/sdcard";
    private static final String NAME = "test.mp3";

    private SystemSkill() {
        super(TAG);
    }

    public static SystemSkill getInstance() {
        return SingleHolder.INSTANCE;
    }

    private static class SingleHolder {
        public static final SystemSkill INSTANCE = new SystemSkill();
    }

//    public void setLight(String params){
//        RobotApi.getInstance().setLight(Constants.REQUEST_ID_DEFAULT,params,null);
//    }

    public void getRobotSn(CommandListener listener){
        RobotApi.getInstance().getRobotSn(listener);
    }

    public void textToMp3(String text,CommandListener listener){
        RobotApi.getInstance().textToMp3(Constants.REQUEST_ID_DEFAULT,text,PATH,NAME,listener);
    }

    public void setLight(int startColor,int endColor,int finalColor,int startColorTime,
                         int endColorTime,int loopTimes,int lightDuration){
        Log.d(TAG,"setLight:");
        JSONObject params = new JSONObject();
//        String type = Definition.LAMP_TYPE_BREATH;
        try {
            params.put(Definition.JSON_LAMP_TYPE, Definition.LAMP_TYPE_BREATH);
            params.put(Definition.JSON_LAMP_TARGET, 0);
            params.put(Definition.JSON_LAMP_RGB_START,  startColor);
            params.put(Definition.JSON_LAMP_RGB_END,  endColor);
            params.put(Definition.JSON_LAMP_START_TIME, startColorTime);
            params.put(Definition.JSON_LAMP_END_TIME, endColorTime);
            params.put(Definition.JSON_LAMP_REPEAT, loopTimes);
            params.put(Definition.JSON_LAMP_ON_TIME, lightDuration);
            params.put(Definition.JSON_LAMP_RGB_FREEZE, finalColor);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RobotApi.getInstance().setLight(0, params.toString(), new ActionListener(){
            @Override
            public void onResult(int status, String responseString) throws RemoteException {

                Log.d(TAG,"onResult:"+status+"   string:"+responseString);
            }

            @Override
            public void onError(int errorCode, String errorString) throws RemoteException {
                Log.d(TAG,"onError:"+errorCode+"   string:"+errorString);
            }
        });
    }

    public void installApk(){
        RobotApi.getInstance().installApk(Constants.REQUEST_ID_DEFAULT,"/sdcard/test.apk","100");
    }
}
