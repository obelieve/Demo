package com.ainirobot.sdk_demo.skill;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.ainirobot.coreservice.client.Definition;
import com.ainirobot.coreservice.client.RobotApi;
import com.ainirobot.coreservice.client.actionbean.Pose;
import com.ainirobot.coreservice.client.listener.ActionListener;
import com.ainirobot.coreservice.client.listener.CommandListener;

import com.ainirobot.sdk_demo.model.BaseSkill;
import com.ainirobot.sdk_demo.utils.Constants;
import com.ainirobot.sdk_demo.utils.TestUtil;
import com.ainirobot.sdk_demo.utils.ToastUtil;

import java.util.List;

/**
 * @author Orion
 * @time 2018/9/12
 */
public class NavigationSkill extends BaseSkill {

    private static final String TAG = "NavigationSkill";

    private NavigationSkill() {
        super(TAG);
    }

    public static NavigationSkill getInstance() {
        return SingleHolder.INSTANCE;
    }

    private static class SingleHolder {
        public static final NavigationSkill INSTANCE = new NavigationSkill();
    }

    public void startNavigation(String destination) {
        RobotApi.getInstance().startNavigation(Constants.REQUEST_ID_DEFAULT, destination,
                Constants.COORDINATE_DEVIATION, Constants.START_NAVIGATION_TIME_OUT,
                new ActionListener() {
                    @Override
                    public void onError(int errorCode, String errorString) throws RemoteException {
                        Log.e(TAG, "onError: " + errorCode + ", " + errorString);
                    }

                    @Override
                    public void onResult(int status, String responseString) throws RemoteException {
                        Log.e(TAG, "onResult: " + status + ", " + responseString);
                        switch (status) {
                            case Definition.RESULT_OK:
                                RobotApi.getInstance().resetHead(Constants.REQUEST_ID_DEFAULT, null);
                                break;
                            case Definition.ACTION_RESPONSE_STOP_SUCCESS:
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onStatusUpdate(int status, String data) throws RemoteException {
                        Log.e(TAG, "onResult: " + status + ", " + data);
                    }
                });
    }
    public void startNavigation(String destination,ActionListener listener) {
        RobotApi.getInstance().startNavigation(Constants.REQUEST_ID_DEFAULT, destination,
                Constants.COORDINATE_DEVIATION, Constants.START_NAVIGATION_TIME_OUT,listener
                );
    }
    public void stopNavigation() {
        RobotApi.getInstance().stopNavigation(Constants.REQUEST_ID_DEFAULT);
    }

    public void setLocation(String param, CommandListener listener) {
        RobotApi.getInstance().setLocation(Constants.REQUEST_ID_DEFAULT, param, listener);
    }

    public void removeLocation(String param, CommandListener listener) {
        RobotApi.getInstance().removeLocation(Constants.REQUEST_ID_DEFAULT, param, listener);
    }

    public void getLocation(String param, CommandListener listener) {
        RobotApi.getInstance().getLocation(Constants.REQUEST_ID_DEFAULT, param, listener);
    }

    public void getPosition(CommandListener listener) {
        RobotApi.getInstance().getPosition(Constants.REQUEST_ID_DEFAULT, listener);
    }

    public void goPosition(String position, CommandListener listener) {
        RobotApi.getInstance().goPosition(Constants.REQUEST_ID_DEFAULT, position, listener);
    }

    public void setPoseEstimate(String param, CommandListener listener) {
        RobotApi.getInstance().setPoseEstimate(Constants.REQUEST_ID_DEFAULT, param, listener);
    }

    public void saveRobotEstimate(CommandListener listener) {
        RobotApi.getInstance().saveRobotEstimate(Constants.REQUEST_ID_DEFAULT, listener);
    }

    public void getPlaceName(String param, CommandListener listener) {
        RobotApi.getInstance().getPlace(Constants.REQUEST_ID_DEFAULT, param, listener);
        TestUtil.updateApi("getPlace");
    }

    public void getPlaceList(CommandListener listener) {
        RobotApi.getInstance().getPlaceList(Constants.REQUEST_ID_DEFAULT, listener);
    }

    public void isRobotInlocations(String param, CommandListener listener) {
        RobotApi.getInstance().isRobotInlocations(Constants.REQUEST_ID_DEFAULT, param, listener);
    }

    public void isRobotEstimate(CommandListener listener) {
        RobotApi.getInstance().isRobotEstimate(Constants.REQUEST_ID_DEFAULT, listener);
    }

    public void getMapName(CommandListener listener) {
        RobotApi.getInstance().getMapName(Constants.REQUEST_ID_DEFAULT, listener);
    }

    public void switchMap(String params, CommandListener listener) {
        RobotApi.getInstance().switchMap(Constants.REQUEST_ID_DEFAULT, params, listener);
    }

    public void startCruise(List<Pose> route, int startPoint, List<Integer> dockingPoints, ActionListener listener) {
        RobotApi.getInstance().startCruise(Constants.REQUEST_ID_DEFAULT,
                route, startPoint, dockingPoints, listener);
    }

    public void stopCruise() {
        RobotApi.getInstance().stopCruise(Constants.REQUEST_ID_DEFAULT);
    }

    public void resumeSpecialPlaceTheta(String placeName, CommandListener listener){
        RobotApi.getInstance().resumeSpecialPlaceTheta(Constants.REQUEST_ID_DEFAULT,placeName,listener);
    }

}
