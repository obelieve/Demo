package com.ainirobot.sdk_demo.skill;

import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.ainirobot.coreservice.client.Definition;
import com.ainirobot.coreservice.client.RobotApi;
import com.ainirobot.coreservice.client.actionbean.LeadingParams;
import com.ainirobot.coreservice.client.listener.ActionListener;
import com.ainirobot.coreservice.client.listener.CommandListener;
import com.ainirobot.coreservice.client.listener.PersonInfoListener;
import com.ainirobot.sdk_demo.model.BaseSkill;
import com.ainirobot.sdk_demo.utils.Constants;
import com.ainirobot.sdk_demo.utils.TestUtil;
import com.ainirobot.sdk_demo.utils.ToastUtil;

import org.json.JSONObject;

import java.util.List;


/**
 * @author Orion
 * @time 2018/9/12
 */
public class FaceSkill extends BaseSkill {

    private static final String TAG = "FaceSkill";

    private FaceSkill() {
        super(TAG);
    }

    public static FaceSkill getInstance() {
        return SingleHolder.INSTANCE;
    }

    private static class SingleHolder {
        public static final FaceSkill INSTANCE = new FaceSkill();
    }

    public void register(final String name) {
        RobotApi.getInstance().startRegister(0, name, 6 * 1000, 3, 1000, new ActionListener() {
            @Override
            public void onStatusUpdate(int status, String data) throws RemoteException {
                ToastUtil.getInstance().onUpdate(status, data);
            }

            @Override
            public void onError(int errorCode, String errorString) throws RemoteException {
                ToastUtil.getInstance().onError(errorCode, errorString);
            }

            @Override
            public void onResult(int status, String response) throws RemoteException {
                ToastUtil.getInstance().onResult(status, response);
                switch (status) {
                    case Definition.RESULT_OK: {
                        handlerResult(response, name, true);
                        break;
                    }
                    case Definition.RESULT_FAILURE: {
                        handlerResult(response, name, false);
                        break;
                    }
                    default: {
                        ToastUtil.getInstance().onError(-1, "register failed.");
                        break;
                    }
                }
            }
        });
    }

    private void handlerResult(String response, String name, boolean isSuccess) {
        if (isSuccess) {
            try {
                if (!TextUtils.isEmpty(response)) {
                    JSONObject json = new JSONObject(response);
                    String remoteType = json.optString(Definition.REGISTER_REMOTE_TYPE);
                    String remoteName = json.optString(Definition.REGISTER_REMOTE_NAME);
                    Log.d(TAG, "handlerResult remoteType = " + remoteType + ", remoteName = " + remoteName);
                    if (Definition.REGISTER_REMOTE_SERVER_EXIST.equals(remoteType) && !TextUtils.isEmpty(remoteName)) {
                        SpeechSkill.getInstance().playTxt("我之前就认识你啦");
                    } else if (Definition.REGISTER_REMOTE_SERVER_NEW.equals(remoteType) && !TextUtils.isEmpty(remoteName)) {
                        SpeechSkill.getInstance().playTxt("很高兴认识你");
                    }
                }
            } catch (Throwable e) {
                Log.e(TAG, "handleResultOK e : " + e.getLocalizedMessage());
            }
        } else {
            if (TextUtils.isEmpty(name)) {
                SpeechSkill.getInstance().playTxt("我还不认识你");
            } else {
                SpeechSkill.getInstance().playTxt("请重试");
            }
        }
    }

    /**
     * 唤醒
     *
     * @param angle 面对人物正面的角度值
     */
    public void wakeUp(final float angle) {
        RobotApi.getInstance().wakeUp(Constants.REQUEST_ID_DEFAULT, angle, new ActionListener() {
            @Override
            public void onResult(int status, String responseString) throws RemoteException {
                ToastUtil.getInstance().onResult(status, String.valueOf(responseString));
            }

            @Override
            public void onError(int errorCode, String errorString) throws RemoteException {
                ToastUtil.getInstance().onError(errorCode, errorString);
            }
        });
        TestUtil.updateApi("wakeUp");
    }

    public void startLead(int reqId, LeadingParams params, ActionListener listener) {
        RobotApi.getInstance().startLead(reqId, params, listener);
    }

    public void stopLead(int reqId, boolean isResetHW) {
        RobotApi.getInstance().stopLead(reqId, isResetHW);
    }

    public void startFocusFollow(int reqId, int personId, long lostTimer,
                                 float maxDistance, ActionListener listener) {
        RobotApi.getInstance().startFocusFollow(reqId, personId, lostTimer, maxDistance, listener);
    }

    public void stopFocusFollow(int reqId) {
        RobotApi.getInstance().stopFocusFollow(reqId);
    }

    public void switchCamera(String mode, CommandListener listener) {
        RobotApi.getInstance().switchCamera(Constants.REQUEST_ID_DEFAULT, mode, listener);
    }

    public void startGetAllPersonInfo(PersonInfoListener listener) {
        RobotApi.getInstance().startGetAllPersonInfo(Constants.REQUEST_ID_DEFAULT, listener);
    }

    public void stopGetAllPersonInfo(PersonInfoListener listener) {
        RobotApi.getInstance().stopGetAllPersonInfo(Constants.REQUEST_ID_DEFAULT, listener);
    }

    public void getPicturePath(int faceId, CommandListener listener) {
        RobotApi.getInstance().getPictureById(Constants.REQUEST_ID_DEFAULT, faceId, 1, listener);
    }

    public void getPersonInfoFromServer(String faceId, List<String> pictures, CommandListener listener) {
        RobotApi.getInstance().getPersonInfoFromNet(Constants.REQUEST_ID_DEFAULT, faceId, pictures, listener);
    }
}
