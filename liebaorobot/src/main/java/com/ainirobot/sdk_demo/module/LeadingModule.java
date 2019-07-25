package com.ainirobot.sdk_demo.module;

import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.ainirobot.coreservice.client.Definition;
import com.ainirobot.coreservice.client.RobotApi;
import com.ainirobot.coreservice.client.SettingsUtil;
import com.ainirobot.coreservice.client.listener.ActionListener;
import com.ainirobot.coreservice.client.listener.CommandListener;
import com.ainirobot.coreservice.client.listener.TextListener;
import com.ainirobot.sdk_demo.R;
import com.ainirobot.sdk_demo.RobotApplication;
import com.ainirobot.sdk_demo.model.bean.MessageEvent;
import com.ainirobot.sdk_demo.skill.ControlManager;
import com.ainirobot.sdk_demo.skill.SpeechSkill;
import com.ainirobot.sdk_demo.utils.Constants;

import org.greenrobot.eventbus.EventBus;

import static com.ainirobot.sdk_demo.utils.Constants.COORDINATE_DEVIATION;

/**
 * 导航场景
 */
public class LeadingModule extends BaseModule{
    private static final String TAG = LeadingModule.class.getSimpleName();
    private static LeadingModule sLeadingModule;
    private LeadListener mLeadListener;
    private TextListener mTextListener = new TextListener(){
        @Override
        public void onStop() {
            stop();
        }

        @Override
        public void onError() {
            stop();
        }

        @Override
        public void onComplete() {
            stop();
        }
    };
    public static LeadingModule getInstance() {
        if (null == sLeadingModule) {
            sLeadingModule = new LeadingModule();
        }
        return sLeadingModule;
    }

    /**
     * 底盘导航
     * @param params 目的地
     */
    @Override
    public void start(final String params) {
        EventBus.getDefault().post(new MessageEvent(Constants.FRAGMENT_TYPE.NAVIGATION));
        SpeechSkill.getInstance().getSkillApi().setRecognizeMode(false);
        checkMapExist(params);
    }

    /**
     * 判断地图是否存在
     * @param params 目的地名称
     */
    private void checkMapExist(final String params){
        RobotApi.getInstance().getMapName(0, new CommandListener() {
            @Override
            public void onResult(int result, String message) {
                Log.i(TAG, "getMapName onResult : " + result + " || " + message);
                if (isCommResultOk(result, message)) {
                    startNavigation(params);
                } else {
                    Log.i(TAG, "getMapName, no map error");
                    doSpeech(RobotApplication.getInstance().getString(R.string.without_map),mTextListener);
                }
            }
        });
    }
    private boolean isCommResultOk(int result, String message) {
        return (result == Definition.RESULT_OK && !TextUtils.isEmpty(message)
                && !"timeout".equals(message) && !"failed".equals(message));
    }
    /**
     * end
     */
    @Override
    public void stop() {
        mLeadListener.leadEnd();
        RobotApi.getInstance().stopNavigation(0);
        ControlManager.getInstance().resetMode();
        ControlManager.getInstance().setMode(Constants.Mode.WELCOME_MODE,null);
    }

    /**
     * 开始导航
     */
    private void startNavigation(final String mNavPlace) {
        float linearSpeed = SettingsUtil.getFloat(RobotApplication.getInstance(), SettingsUtil.ROBOT_SETTING_NAV_LINEAR_SPEED, SettingsUtil.ROBOT_SETTING_DEFAULT_LINEAR_SPEED);
        float angularSpeed = SettingsUtil.getFloat(RobotApplication.getInstance(), SettingsUtil.ROBOT_SETTING_NAV_ANGULAR_SPEED, SettingsUtil.ROBOT_SETTING_DEFAULT_ANGULAR_SPEED);
        RobotApi.getInstance().startNavigation(0, mNavPlace, COORDINATE_DEVIATION,
                20 * 1000, linearSpeed, angularSpeed, new ActionListener() {
                    @Override
                    public void onResult(int status, String responseString) throws RemoteException {
                        Log.i(TAG, "startNavigation onResult " + status + " || " + responseString);
                        switch (status) {
                            case Definition.RESULT_OK: {
                                doSpeech(mNavPlace+"已到达",mTextListener);
                                break;
                            }

                            case Definition.ACTION_RESPONSE_STOP_SUCCESS: {
                                Log.d(TAG, "Navigation stop success");
                                break;
                            }

                            case Definition.RESULT_FAILURE: {
                                Log.w(TAG, "can't go to destination, reqId: " + 0 + " responseString: " + responseString);
                                doSpeech((mNavPlace + "无法到达"),mTextListener);
                                break;
                            }

                            default:
                                Log.e(TAG, "startNavigation::onResult() reqId: " + 0 + " unknown status: " + status
                                        + "  responseString: " + responseString);
                                stop();
                                break;
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorString) throws RemoteException {
                        Log.i(TAG, "startNavigation onError " + errorCode + " || " + errorString);
                        String errorMsg = "";
                        switch (errorCode) {
                            case Definition.ERROR_NOT_ESTIMATE:
                                errorMsg = "请先定位";
                                break;
                            case Definition.ACTION_RESPONSE_REQUEST_RES_ERROR:
                            case Definition.ACTION_RESPONSE_ALREADY_RUN:
                                errorMsg = "我的腿动不了了, 请检查当前模式";
                                break;
                            case Definition.ERROR_DESTINATION_CAN_NOT_ARRAIVE:
                                errorMsg = mNavPlace + "无法到达";
                                break;
                            case Definition.ERROR_DESTINATION_NOT_EXIST:
                                errorMsg = "没有设定" + mNavPlace;
                                break;
                            case Definition.ERROR_RESOURCE_LOCK:
                                errorMsg = "还未连接到底盘，请稍后再试";
                                break;
                            case Definition.ERROR_IN_DESTINATION:
                                errorMsg = "这里就是" + mNavPlace;
                                break;
                            default:
                                Log.i(TAG, "startNavigation onError default ");
                                break;
                        }

                        if (!TextUtils.isEmpty(errorMsg)) {
                            doSpeech(errorMsg,mTextListener);
                        } else {
                            Log.e(TAG, "startNavigation onError stopModule");
                            stop();
                        }
                    }

                    @Override
                    public void onStatusUpdate(int status, String data) throws RemoteException {
                        Log.i(TAG, "startNavigation onStatusUpdate " + status + " || " + data);
                        switch (status){
                            case Definition.STATUS_NAVI_OUT_MAP:
                                doSpeech(RobotApplication.getInstance().getString(R.string.speech_status_navi_out_map_please_edit),null);
                                break;
                            case Definition.STATUS_NAVI_GLOBAL_PATH_FAILED:
                                doSpeech(RobotApplication.getInstance().getString(R.string.speech_status_navi_global_path_failed),null);
                                break;
                            case Definition.STATUS_NAVI_OBSTACLES_AVOID:
                                doSpeech(RobotApplication.getInstance().getString(R.string.speech_status_navi_global_path_obstacles),null);
                                break;
                            case Definition.STATUS_GOAL_OCCLUDED:
                            case Definition.STATUS_NAVI_AVOID:
                                doSpeech(RobotApplication.getInstance().getString(R.string.speech_status_navi_avoid),null);
                                break;
                            case Definition.STATUS_NAVI_AVOID_END:
                                break;
                            case Definition.STATUS_START_NAVIGATION:
                                doSpeech("好的,去" + mNavPlace,null);
                                if (mLeadListener != null) {
                                    mLeadListener.leading(mNavPlace);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
    }

    /**
     * 到达目的地-语音播报
     * @param speech 语音播报内容
     */
    private void doSpeech(String speech,TextListener listener) {
        SpeechSkill.getInstance().getSkillApi().playText(speech,listener);
    }

    public void registerLeadListener(LeadListener listener) {
        this.mLeadListener = listener;
    }

    public void unRegistLeadListener() {
        this.mLeadListener = null;
    }

    public interface LeadListener {
        /**
         * 开始引领
         * @param mNavPlace 目的地
         */
        void leading(String mNavPlace);

        /**
         * 引领任务结束
         */
        void leadEnd();
    }
}
