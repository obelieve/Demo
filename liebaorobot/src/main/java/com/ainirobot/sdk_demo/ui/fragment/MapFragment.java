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

import com.ainirobot.coreservice.client.actionbean.Pose;
import com.ainirobot.coreservice.client.listener.ActionListener;
import com.ainirobot.coreservice.client.listener.CommandListener;
import com.ainirobot.sdk_demo.R;
import com.ainirobot.sdk_demo.model.BaseFragment;
import com.ainirobot.sdk_demo.skill.ChargeSkill;
import com.ainirobot.sdk_demo.skill.NavigationSkill;
import com.ainirobot.sdk_demo.utils.TestUtil;
import com.ainirobot.sdk_demo.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Orion
 * @time 2018/9/20
 */
public class MapFragment extends BaseFragment {

    private static final String TAG = "MapFragment";

    private View mContainer;

    //
    private EditText mEditMapName;
    private Button mGetMapName;
    private Button mSwitchMapName;
    private Button mPlaceList;

    private Button mStartCruise;
    private Button mStopCruise;

    private TextView mResultText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainer = inflater.inflate(R.layout.layout_map, container, false);
        // 地图
        mEditMapName = (EditText) mContainer.findViewById(R.id.editText_map_name);
        mGetMapName = (Button) mContainer.findViewById(R.id.button_navigation_get_mapname);
        mSwitchMapName = (Button) mContainer.findViewById(R.id.button_navigation_switch_map);
        mPlaceList = (Button) mContainer.findViewById(R.id.button_navigation_get_place_list);
        mStartCruise = (Button) mContainer.findViewById(R.id.button_map_start_cruise);
        mStopCruise = (Button) mContainer.findViewById(R.id.button_map_stop_cruise);
        mResultText = (TextView) mContainer.findViewById(R.id.text_map_result);
        setOnClickListener(mGetMapName, mSwitchMapName, mPlaceList, mStartCruise, mStopCruise);
        return mContainer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_navigation_get_place_list:
                NavigationSkill.getInstance().getPlaceList(new CommandListener() {
                    @Override
                    public void onResult(int result, String message) {
                        Log.d(TAG, "onResult: " + message);
                        mResultText.setText(message);
                    }

                    @Override
                    public void onStatusUpdate(int status, String data) {
                        ToastUtil.getInstance().onUpdate(status, data);
                    }
                });
                TestUtil.updateApi("getPlaceList");
                break;
            case R.id.button_navigation_get_mapname:
                NavigationSkill.getInstance().getMapName(new CommandListener() {
                    @Override
                    public void onResult(int result, String message) {
                        ToastUtil.getInstance().onResult(result, message);
                        mResultText.setText(message);
                    }
                });
                TestUtil.updateApi("getMapName");
                break;
            case R.id.button_navigation_switch_map:
                NavigationSkill.getInstance().switchMap(mEditMapName.getText().toString(), new CommandListener() {
                    @Override
                    public void onResult(int result, String message) {
                        ToastUtil.getInstance().onResult(result, message);
                        mResultText.setText(message);
                    }
                });
                TestUtil.updateApi("switchMap");

                break;
            case R.id.button_map_start_cruise:
                NavigationSkill.getInstance().startCruise(getPoseList(), 0,
                        null, new ActionListener() {
                            @Override
                            public void onResult(int status, String responseString) throws RemoteException {
                                ToastUtil.getInstance().onResult(status, responseString);
                            }

                            @Override
                            public void onStatusUpdate(int status, String data) throws RemoteException {
                                ToastUtil.getInstance().onUpdate(status, data);
                            }

                            @Override
                            public void onError(int errorCode, String errorString) throws RemoteException {
                                ToastUtil.getInstance().onError(errorCode, errorString);
                            }
                        });
                TestUtil.updateApi("startCruise");

                break;
            case R.id.button_map_stop_cruise:
                NavigationSkill.getInstance().stopCruise();
                TestUtil.updateApi("stopCruise");
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ChargeSkill.getInstance().stopAutoChargeAction();
    }

    private List<Pose> getPoseList() {
        if (TextUtils.isEmpty(mResultText.getText().toString())) {
            return null;
        }
        List<Pose> route = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(mResultText.getText().toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = (JSONObject) array.get(i);
                if (!"充电桩".equals(object.optString("name"))) {
                    Pose pose = new Pose();
                    pose.setX(Float.valueOf(object.optString("x")));
                    pose.setY(Float.valueOf(object.optString("y")));
                    pose.setTheta(Float.valueOf(object.optString("theta")));
                    route.add(pose);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return route;
    }
}
