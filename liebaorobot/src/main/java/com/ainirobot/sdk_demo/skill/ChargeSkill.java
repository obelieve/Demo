package com.ainirobot.sdk_demo.skill;

import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;

import com.ainirobot.coreservice.client.Definition;
import com.ainirobot.coreservice.client.RobotApi;
import com.ainirobot.coreservice.client.listener.ActionListener;

import com.ainirobot.sdk_demo.RobotApplication;
import com.ainirobot.sdk_demo.model.BaseSkill;
import com.ainirobot.sdk_demo.utils.Constants;
import com.ainirobot.sdk_demo.utils.ToastUtil;

/**
 * @author Orion
 * @time 2018/9/12
 */
public class ChargeSkill extends BaseSkill {

    private static final String TAG = "ChargeSkill";

    private ChargeSkill() {
        super(TAG);
    }

    public static ChargeSkill getInstance() {
        return SingleHolder.INSTANCE;
    }

    private static class SingleHolder {
        public static final ChargeSkill INSTANCE = new ChargeSkill();
    }

    public void setStartChargePoseAction() {
        RobotApi.getInstance().setStartChargePoseAction(Constants.REQUEST_ID_DEFAULT, 0, new ActionListener() {
            @Override
            public void onResult(final int status, final String responseString) throws RemoteException {
                super.onResult(status, responseString);
                ToastUtil.getInstance().onResult(status,responseString);
                Log.d(TAG, "setStartChargePoseAction status = " + status + " , response = " + responseString);
                if (Definition.RESULT_OK == status) {
                    SpeechSkill.getInstance().playTxt("设置充电桩成功");
                    Settings.Global.putInt(RobotApplication.getInstance().getContentResolver(), "充电桩", 1);// for settings use
                }
            }

            @Override
            public void onError(int errorCode, String errorString) throws RemoteException {
                super.onError(errorCode, errorString);
                ToastUtil.getInstance().onError(errorCode,errorString);
                Log.d(TAG, "setStartChargePoseAction onError code = " + errorCode + ", msg = " + errorString);
                SpeechSkill.getInstance().playTxt("设置充电桩失败");
            }
        });
    }

    private static final long CHARGE_TIMEOUT = 3 * Definition.MINUTE;

    public void startNaviToAutoChargeAction() {

        RobotApi.getInstance().startNaviToAutoChargeAction(Constants.REQUEST_ID_DEFAULT, CHARGE_TIMEOUT, new ActionListener() {
            @Override
            public void onResult(int status, String responseString) throws RemoteException {
                ToastUtil.getInstance().onResult(status,responseString);
                switch (status) {
                    case Definition.RESULT_OK:
                        //充电成功
                        SpeechSkill.getInstance().playTxt("自动充电成功");
                        break;
                    case Definition.RESULT_FAILURE:
                        //充电失败
                        SpeechSkill.getInstance().playTxt("自动充电失败");
                        break;
                    default:
                        ToastUtil.getInstance().onResult(status,responseString);
                        break;
                }
            }
            @Override
            public void onError(int errorCode, String errorString) throws RemoteException {
                SpeechSkill.getInstance().playTxt("自动充电失败");
                ToastUtil.getInstance().onError(errorCode,errorString);

            }
            @Override
            public void onStatusUpdate(int status, String data) throws RemoteException {
                switch (status) {
                    case Definition.STATUS_NAVI_GLOBAL_PATH_FAILED:
                        Log.d(TAG, "全局路径规划失败");
                        break;
                    case Definition.STATUS_NAVI_OUT_MAP:
                        Log.d(TAG, "目标点不能到达，引领目的地在地图外，有可能为地图与位置点不匹配，请\n" +
                                "重新设置位置点");
                        break;
                    case Definition.STATUS_NAVI_AVOID:
                        Log.d(TAG, "前往回充点路线已被障碍物堵死");
                        break;
                    case Definition.STATUS_NAVI_AVOID_END:
                        Log.d(TAG, "障碍物已移除");
                        break;
                    default:
                        break;
                }
            }
        });

    }

    public void stopAutoChargeAction() {
        RobotApi.getInstance().stopAutoChargeAction(Constants.REQUEST_ID_DEFAULT, true);

    }

    /**
     * 需OTA4.6以上rom
     */
    public void stopChargingByApp(){
        RobotApi.getInstance().stopChargingByApp();
    }
}
