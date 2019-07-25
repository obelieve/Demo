package com.ainirobot.sdk_demo.control;

import android.os.RemoteException;
import android.util.Log;

import com.ainirobot.coreservice.client.Definition;
import com.ainirobot.coreservice.client.RobotApi;
import com.ainirobot.coreservice.client.StatusListener;
import com.ainirobot.coreservice.client.listener.CommandListener;

import com.ainirobot.sdk_demo.utils.Constants;

/**
 * 位置状态监听
 *
 * @author Orion
 */
public class Status {

    private static final String TAG = "StatusInfo";

    private Status() {
    }

    public static Status getInstance() {
        return SingleHolder.INSTANCE;
    }

    private static class SingleHolder {
        public static final Status INSTANCE = new Status();
    }

    /**
     * 位置实时监听
     *
     * @return
     */
    private StatusListener poseStatusListener = new StatusListener() {

        @Override
        public void onStatusUpdate(String type, String data) throws RemoteException {
            Log.d(TAG, String.format("Pose:onStatusUpdate: type = %s, data = %s", type, data));
            // type = navi_pose, data = {"px":0.1724812388420105,"py":1.0510213375091553,"theta":0.01995115727186203}
        }
    };

    /**
     * 急停状态实时监听
     *
     * @return
     */
    private StatusListener emergencyStatusListener = new StatusListener() {

        @Override
        public void onStatusUpdate(String type, String data) throws RemoteException {
            Log.d(TAG, String.format("Emergency:onStatusUpdate: type = %s, data = %s", type, data));
            // type = status_emergency, data = (0-正常，1-急停)
        }
    };

    private StatusListener estimateStatusListener = new StatusListener() {

        @Override
        public void onStatusUpdate(String type, String data) throws RemoteException {
            Log.d(TAG, String.format("Estimate:onStatusUpdate: type = %s, data = %s", type, data));
        }
    };

    public void registerPoseStatus() {
        registerStatus(Definition.STATUS_POSE, poseStatusListener);
    }

    public void registerEmergencyStatus() {
        registerStatus(Definition.STATUS_EMERGENCY, emergencyStatusListener);
    }

    public void registerEstimateStatus() {
        registerStatus(Definition.STATUS_POSE_ESTIMATE, estimateStatusListener);
    }

    private void registerStatus(String stateType, StatusListener statusListener) {
        RobotApi.getInstance().registerStatusListener(stateType, statusListener);
    }

    public void getEmergencyStatus() {
        RobotApi.getInstance().getEmergencyStatus(Constants.REQUEST_ID_DEFAULT, new CommandListener() {
            @Override
            public void onResult(int result, String message) {
                Log.d(TAG, String.format("getEmergencyStatus:onResult: result = %s, message = %s", result, message));
            }

            @Override
            public void onStatusUpdate(int status, String data) {
                Log.d(TAG, String.format("onStatusUpdate:onResult: status = %s, data = %s", status, data));
            }
        });
    }

    public void relese() {
        RobotApi.getInstance().unregisterStatusListener(poseStatusListener);
        RobotApi.getInstance().unregisterStatusListener(emergencyStatusListener);
    }
}
