package com.ainirobot.sdk_demo.control;

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ainirobot.coreservice.client.listener.CommandListener;
import com.ainirobot.sdk_demo.RobotApplication;

import com.ainirobot.sdk_demo.skill.ControlManager;
import com.ainirobot.sdk_demo.skill.FaceSkill;
import com.ainirobot.sdk_demo.skill.NavigationSkill;
import com.ainirobot.sdk_demo.skill.SpeechSkill;
import com.ainirobot.sdk_demo.ui.LauncherActivity;
import com.ainirobot.sdk_demo.utils.Constants;
import com.ainirobot.sdk_demo.utils.MessageParser;
import com.ainirobot.sdk_demo.utils.TestUtil;
import com.ainirobot.sdk_demo.utils.ToastUtil;

import static com.ainirobot.sdk_demo.utils.Constants.Mode.LEADING_MODE;
import static com.ainirobot.sdk_demo.utils.Constants.Mode.WELCOME_MODE;
import static com.ainirobot.sdk_demo.utils.Constants.REQUEST_TYPE_ASK_SKILL;
import static com.ainirobot.sdk_demo.utils.Constants.REQUEST_TYPE_CALENDAR;
import static com.ainirobot.sdk_demo.utils.Constants.REQUEST_TYPE_CHAT;
import static com.ainirobot.sdk_demo.utils.Constants.REQUEST_TYPE_STOP;
import static com.ainirobot.sdk_demo.utils.Constants.REQUEST_TYPE_TELL_ME_WHY;
import static com.ainirobot.sdk_demo.utils.Constants.REQUEST_TYPE_WEATHER;

/**
 * @author Orion
 * @time 2018/9/14
 */
public class MessageManager {

    private static final String TAG = "MessageManager";

    private boolean isSuspend = true;

    private MessageManager() {
    }

    public static MessageManager getInstance() {
        return SingleHolder.INSTANCE;
    }

    private static class SingleHolder {
        static final MessageManager INSTANCE = new MessageManager();
    }
    /**
     * 解析特定意图
     */
    public void handleMessage(Message message) {
        if (message == null) {
            Log.e(TAG, "handleMessage: message is null.");
            return;
        }

        switch (message.what) {
            case Constants.MSG_REQUEST:
                handleRequest(message);
                break;
            case Constants.MSG_HWREPORT:
                handleHWReport(message);
                break;
            case Constants.MSG_SUSPEND:
                handleSuspend();
                break;
            case Constants.MSG_RECOVERY:
                handleRecovery();
                break;
            default:
                break;
        }
    }

    /**
     * 此处为部分意图处理
     *
     * @param msg
     */
    private void handleRequest(Message msg) {
        Bundle bundle = msg.getData();
        String type = bundle.getString(Constants.BUNDLE_REQUEST_TYPE);
        String param = bundle.getString(Constants.BUNDLE_REQUEST_PARAM);
        switch (type) {
            case Constants.REQUEST_TYPE_SPEECH:
                // 语音唤醒, param 即语音唤醒角度
                FaceSkill.getInstance().wakeUp(Float.valueOf(param));
                break;
            case Constants.REQUEST_TYPE_CRUISE:
                // 巡逻
                /*NavigationSkill.getInstance().startCruise(new CommandListener(){
                    @Override
                    public void onError(int errorCode, String errorString) throws RemoteException {
                        ToastUtil.getInstance().onError(errorCode, errorString);
                    }

                    @Override
                    public void onStatusUpdate(int status, String data) {
                        ToastUtil.getInstance().onUpdate(status, data);
                    }

                    @Override
                    public void onResult(int result, String message) {
                        ToastUtil.getInstance().onResult(result, message);
                    }
                });*/
                break;
            case Constants.REQUEST_TYPE_GUIDE:
                // 导航
                String destination = MessageParser.getDestination(param);
                if (TestUtil.isSceneMode()) {
                    ControlManager.getInstance().setMode(LEADING_MODE,destination);
                } else {
                    NavigationSkill.getInstance().startNavigation(destination);
                }
                break;
            case REQUEST_TYPE_WEATHER:
            case REQUEST_TYPE_CHAT:
            case REQUEST_TYPE_CALENDAR:
            case REQUEST_TYPE_TELL_ME_WHY:
            case REQUEST_TYPE_ASK_SKILL:
                if (TestUtil.isSceneMode()) {
                    ControlManager.getInstance().setMode(WELCOME_MODE,param);
                } else {
                    String answerText = MessageParser.getAnswerText(param);
                    if (!TextUtils.isEmpty(answerText)) {
                        SpeechSkill.getInstance().playTxt(answerText);
                    }
                }
                break;
            case REQUEST_TYPE_STOP:
                    ControlManager.getInstance().stopCurrentModule();
                break;
            default:
                String answerText = MessageParser.getAnswerText(param);
                if (!TextUtils.isEmpty(answerText)) {
                    SpeechSkill.getInstance().playTxt(answerText);
                }
                break;
        }
    }

    private void handleHWReport(Message message) {
        Log.d(TAG, "handleHWReport: " + message);
    }

    private void handleSuspend() {
        ControlManager.getInstance().stopCurrentModule();
        ControlManager.getInstance().resetMode();
        Log.d(TAG, "handleSuspend: 应用被挂起");
        isSuspend = true;
        Toast.makeText(RobotApplication.getInstance(), "应用被挂起", Toast.LENGTH_SHORT).show();
    }

    /**
     * 应用恢复提示放在{@link LauncherActivity#onStart()}
     */
    private void handleRecovery() {
        Log.d(TAG, "handleRecovery: ");
        isSuspend = false;
    }

    public boolean isSuspend() {
        return isSuspend;
    }

}
