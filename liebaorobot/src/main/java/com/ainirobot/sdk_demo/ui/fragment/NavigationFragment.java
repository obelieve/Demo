package com.ainirobot.sdk_demo.ui.fragment;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ainirobot.coreservice.client.Definition;
import com.ainirobot.coreservice.client.RobotApi;
import com.ainirobot.coreservice.client.listener.ActionListener;
import com.ainirobot.coreservice.client.listener.CommandListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.ainirobot.sdk_demo.R;
import com.ainirobot.sdk_demo.model.BaseFragment;
import com.ainirobot.sdk_demo.skill.NavigationSkill;
import com.ainirobot.sdk_demo.utils.Constants;
import com.ainirobot.sdk_demo.utils.TestUtil;
import com.ainirobot.sdk_demo.utils.ToastUtil;

/**
 * @author Orion
 * @time 2018/9/11
 */
public class NavigationFragment extends BaseFragment {

    private static final String TAG = NavigationFragment.class.getSimpleName();
    private View mContainer;

    private Button mEstimateStatus;
    private EditText mEditTargetName;
    private Button mSetLocation;
    private Button mRemoveLocation;
    private Button mGetLocation;
    private TextView mLocationPoint;
    private TextView mIsInLocation;
    //
    private Button mGoPositionButton;
    private Button mStopGoPositionButton;
    //
    private Button mSetEstimateBtn;
    private Button mSetRobotEstimateBtn;
    private Button mStartNavigationBtn;
    private Button mStopNavigationBtn;
    private Button mResumeSpecialPlaceBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainer = inflater.inflate(R.layout.layout_navigation, container, false);
        mEstimateStatus = (Button) mContainer.findViewById(R.id.button_move_estimate_status);
        // 设点
        mEditTargetName = (EditText) mContainer.findViewById(R.id.editText_location_name);
        mSetLocation = (Button) mContainer.findViewById(R.id.button_set_location);
        mRemoveLocation = (Button) mContainer.findViewById(R.id.button_remove_location);
        mGetLocation = (Button) mContainer.findViewById(R.id.button_get_location);
        mLocationPoint = (TextView) mContainer.findViewById(R.id.textView_navigation_location_point);
        mIsInLocation = (TextView) mContainer.findViewById(R.id.button_is_in_location);
        // 导航
        mGoPositionButton = (Button) mContainer.findViewById(R.id.button_go_position);
        mStopGoPositionButton = (Button) mContainer.findViewById(R.id.button_stop_go_position);
        // 设置初始点
        mSetEstimateBtn = (Button) mContainer.findViewById(R.id.button_move_set_pose_estimate);
        mSetRobotEstimateBtn = (Button) mContainer.findViewById(R.id.button_move_set_locate_estimate);
        mStartNavigationBtn = (Button) mContainer.findViewById(R.id.button_start_navigation);
        mStopNavigationBtn = (Button) mContainer.findViewById(R.id.button_stop_navigation);
        mResumeSpecialPlaceBtn = (Button) mContainer.findViewById(R.id.button_resume_special_place_theta);
        //
        setOnClickListener(mEstimateStatus, mSetLocation, mRemoveLocation, mGetLocation, mIsInLocation,
                mGoPositionButton, mStopGoPositionButton, mSetEstimateBtn, mSetRobotEstimateBtn,
                mStartNavigationBtn,mStopNavigationBtn,mResumeSpecialPlaceBtn);
        mContainer.findViewById(R.id.button_get_pose_estimate).setOnClickListener(this);
        return mContainer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_move_estimate_status:
                NavigationSkill.getInstance().isRobotEstimate(
                        new CommandListener() {
                            @Override
                            public void onResult(int result, String message) {
                                ToastUtil.getInstance().onResult(result, message);
                            }
                        });
                TestUtil.updateApi("isRobotEstimate");
                break;
            case R.id.button_get_pose_estimate:
                NavigationSkill.getInstance().getPosition(new CommandListener() {
                    @Override
                    public void onResult(final int result, final String message) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.getInstance().onResult(result, message);
                                    mLocationPoint.setText(message);
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorString) throws RemoteException {
                        super.onError(errorCode, errorString);
                        ToastUtil.getInstance().onError(errorCode, errorString);

                    }
                    @Override
                    public void onStatusUpdate(int status, String data) {
                        ToastUtil.getInstance().onError(status, data);

                    }
                });
                TestUtil.updateApi("getPosition");

                break;
            case R.id.button_set_location:
                if (TextUtils.isEmpty(mEditTargetName.getText())) {
                    ToastUtil.getInstance().toast("请输入位置名称");
                    return;
                }
                NavigationSkill.getInstance().setLocation(mEditTargetName.getText().toString(),
                        new CommandListener() {
                            @Override
                            public void onResult(int result, String message) {
                                ToastUtil.getInstance().onResult(result, message);
                            }
                        });
                TestUtil.updateApi("setLocation");

                break;
            case R.id.button_remove_location:
                if (TextUtils.isEmpty(mEditTargetName.getText())) {
                    ToastUtil.getInstance().toast("请输入位置名称");
                    return;
                }
                NavigationSkill.getInstance().removeLocation(mEditTargetName.getText().toString(),
                        new CommandListener() {
                            @Override
                            public void onResult(int result, String message) {
                                ToastUtil.getInstance().onResult(result, message);
                            }
                        });
                TestUtil.updateApi("removeLocation");

                break;
            case R.id.button_get_location:
                if (TextUtils.isEmpty(mEditTargetName.getText())) {
                    ToastUtil.getInstance().toast("请输入位置名称");
                    return;
                }
                NavigationSkill.getInstance().getLocation(mEditTargetName.getText().toString(),
                        new CommandListener() {
                            @Override
                            public void onResult(int result, final String message) {
                                ToastUtil.getInstance().onResult(result, message);
                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mLocationPoint.setText(message);
                                        }
                                    });
                                }
                            }
                        });
                TestUtil.updateApi("getLocation");

                break;
            case R.id.button_go_position:
                if (TextUtils.isEmpty(mLocationPoint.getText())) {
                    ToastUtil.getInstance().toast("坐标点不能为空，请点击 获取指定位置坐标");
                    return;
                }
                // 导航可以使用goPosition或者startNavigation，请注意要使用对应的stop方法
                NavigationSkill.getInstance().goPosition(getPoint(mLocationPoint.getText().toString()),
                        new CommandListener() {
                            @Override
                            public void onResult(int result, String message) {
                                ToastUtil.getInstance().onResult(result, message);
                            }

                        });
                TestUtil.updateApi("goPosition");

                break;
            case R.id.button_stop_go_position:
                RobotApi.getInstance().stopGoPosition(Constants.REQUEST_ID_DEFAULT);
                TestUtil.updateApi("stopGoPosition");

                break;
            case R.id.button_is_in_location:
                if (TextUtils.isEmpty(mEditTargetName.getText())) {
                    ToastUtil.getInstance().toast("请输入位置名称");
                    return;
                }
                JSONObject json = new JSONObject();
                try {
                    json.put(Definition.JSON_NAVI_TARGET_PLACE_NAME, mEditTargetName.getText().toString());
                    json.put(Definition.JSON_NAVI_COORDINATE_DEVIATION, 0.9d);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                NavigationSkill.getInstance().isRobotInlocations(json.toString(),
                        new CommandListener() {
                            @Override
                            public void onResult(int result, String message) {
                                ToastUtil.getInstance().onResult(result, message);
                            }
                            @Override
                            public void onError(int errorCode, String errorString) throws RemoteException {
                                super.onError(errorCode, errorString);
                                ToastUtil.getInstance().onError(errorCode, errorString);

                            }
                            @Override
                            public void onStatusUpdate(int status, String data) {
                                ToastUtil.getInstance().onError(status, data);

                            }
                        });
                TestUtil.updateApi("isRobotInlocations");

                break;
            case R.id.button_move_set_pose_estimate:
                if (TextUtils.isEmpty(mLocationPoint.getText())) {
                    ToastUtil.getInstance().toast("坐标点不能为空，请点击 获取指定位置坐标");
                    return;
                }
                NavigationSkill.getInstance().setPoseEstimate(getPoint(mLocationPoint.getText().toString()),
                        new CommandListener() {
                            @Override
                            public void onResult(int result, String message) {
                                ToastUtil.getInstance().onResult(result, message);
                            }
                            @Override
                            public void onError(int errorCode, String errorString) throws RemoteException {
                                ToastUtil.getInstance().onResult(errorCode, errorString);
                            }

                            @Override
                            public void onStatusUpdate(int status, String data) {
                                Log.d(TAG, "onStatusUpdate\t" + status + "\tdata " + data);
                            }
                        });
                TestUtil.updateApi("setPoseEstimate");

                break;
            case R.id.button_move_set_locate_estimate:
                NavigationSkill.getInstance().saveRobotEstimate(new CommandListener() {
                    @Override
                    public void onResult(int result, String message) {
                        ToastUtil.getInstance().onResult(result, message);
                    }
                });
                TestUtil.updateApi("saveRobotEstimate");

            case R.id.button_start_navigation:
                if (TextUtils.isEmpty(mEditTargetName.getText())) {
                    ToastUtil.getInstance().toast("请输入位置名称");
                    return;
                }
                // 导航可以使用goPosition或者startNavigation，请注意要使用对应的stop方法
                NavigationSkill.getInstance().startNavigation((mEditTargetName.getText().toString()), new ActionListener() {
                    @Override
                    public void onResult(int status, String responseString) throws RemoteException {
                        ToastUtil.getInstance().onResult(status, responseString);
                    }

                    @Override
                    public void onError(int errorCode, String errorString) throws RemoteException {
                        ToastUtil.getInstance().onResult(errorCode, errorString);
                    }

                    @Override
                    public void onStatusUpdate(int status, String data) throws RemoteException {
                        Log.d(TAG, "onStatusUpdate\t" + status + "\tdata " + data);
                    }
                });
                TestUtil.updateApi("startNavigation");

                break;
            case R.id.button_stop_navigation:
                NavigationSkill.getInstance().stopNavigation();
                TestUtil.updateApi("stopNavigation");

                break;
            case R.id.button_resume_special_place_theta:
                if (TextUtils.isEmpty(mEditTargetName.getText())) {
                    ToastUtil.getInstance().toast("请输入位置名称");
                    return;
                }

                NavigationSkill.getInstance().resumeSpecialPlaceTheta(mEditTargetName.getText().toString(),new CommandListener(){

                    @Override
                    public void onResult(int result, String message) {
                        Log.d(TAG,"turn result:"+result+"   message:"+message);
                        if (result == -1){
                            ToastUtil.getInstance().onResult(result,"命令没有执行");

                        }else{
                            ToastUtil.getInstance().onResult(result,"命令执行");
                        }
                    }
                });
                TestUtil.updateApi("resumeSpecialPlaceTheta");

                break;
            default:
                break;
        }
    }

    private String getPoint(String text) {
        String point = "";
        try {
            JSONObject jsonObject = new JSONObject(text);
            JSONObject pointJson = new JSONObject();
            pointJson.putOpt("x", jsonObject.optString("px"));
            pointJson.putOpt("y", jsonObject.optString("py"));
            pointJson.putOpt("theta", jsonObject.optString("theta"));
            point = pointJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return point;
    }

}
