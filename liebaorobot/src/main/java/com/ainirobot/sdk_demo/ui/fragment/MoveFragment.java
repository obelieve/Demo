package com.ainirobot.sdk_demo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.ainirobot.coreservice.client.RobotApi;
import com.ainirobot.coreservice.client.listener.CommandListener;

import com.ainirobot.sdk_demo.R;
import com.ainirobot.sdk_demo.model.BaseFragment;
import com.ainirobot.sdk_demo.utils.Constants;
import com.ainirobot.sdk_demo.utils.TestUtil;
import com.ainirobot.sdk_demo.utils.ToastUtil;


/**
 * @author Orion
 * @time 2018/9/11
 */
public class MoveFragment extends BaseFragment {

    private static final String TAG = "MoveFragment";

    private View mContainer;

    private EditText mEditMoveSpeed;
    private EditText mEditMoveDistance;
    private Button mBtnGoForward;
    private Button mBtnBackForward;
    //
    private EditText mEditTurnSpeed;
    private EditText mEditTurnAngle;
    private Button mBtnTurnLeft;
    private Button mBtnTurnRight;
    //
    private EditText mEditLineSpeed;
    private EditText mEditAngleSpeed;
    private Button mBtnMotionArc;
    //
    private EditText mEditHeadVertical;
    private EditText mEditHeadHorizontal;
    private Button mBtnHeadAbsolute;
    private Button mBtnHeadRelative;
    private Button mBtnHeadReset;
    //
    private Button mStopAll;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainer = inflater.inflate(R.layout.layout_move, container, false);
        // 前进后退
        mEditMoveSpeed = (EditText) mContainer.findViewById(R.id.editText_move_forward_speed);
        mEditMoveDistance = (EditText) mContainer.findViewById(R.id.editText_move_forward_distance);
        mBtnGoForward = (Button) mContainer.findViewById(R.id.button_move_go_forward);
        mBtnBackForward = (Button) mContainer.findViewById(R.id.button_move_back_forward);
        // 旋转
        mEditTurnSpeed = (EditText) mContainer.findViewById(R.id.editText_move_turn_speed);
        mEditTurnAngle = (EditText) mContainer.findViewById(R.id.editText_move_turn_angle);
        mBtnTurnLeft = (Button) mContainer.findViewById(R.id.editText_move_turn_left);
        mBtnTurnRight = (Button) mContainer.findViewById(R.id.editText_move_turn_right);
        // arc
        mEditLineSpeed = (EditText) mContainer.findViewById(R.id.editText_move_arc_speed);
        mEditAngleSpeed = (EditText) mContainer.findViewById(R.id.editText_move_arc_angle);
        mBtnMotionArc = (Button) mContainer.findViewById(R.id.editText_move_move_arc);
        // head
        mEditHeadVertical = (EditText) mContainer.findViewById(R.id.editText_move_head_vertical);
        mEditHeadHorizontal = (EditText) mContainer.findViewById(R.id.editText_move_head_horizontal);
        mBtnHeadAbsolute = (Button) mContainer.findViewById(R.id.editText_move_move_head_absolute);
        mBtnHeadRelative = (Button) mContainer.findViewById(R.id.editText_move_move_head_relative);
        mBtnHeadReset = (Button) mContainer.findViewById(R.id.editText_move_move_head_reset);
        //
        mStopAll = (Button) mContainer.findViewById(R.id.editText_move_stop_all);
        setOnClickListener(mBtnGoForward, mBtnBackForward, mBtnTurnLeft, mBtnTurnRight,
                mBtnMotionArc, mBtnHeadAbsolute, mBtnHeadRelative, mStopAll,mBtnHeadReset);
        return mContainer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_move_go_forward:
                if (TextUtils.isEmpty(mEditMoveSpeed.getText()) || TextUtils.isEmpty(mEditMoveDistance.getText())) {
                    ToastUtil.getInstance().toast("参数不能为空");
                    return;
                }
                RobotApi.getInstance().goForward(Constants.REQUEST_ID_DEFAULT,
                        getFloatValue(mEditMoveSpeed),
                        getFloatValue(mEditMoveDistance),
                        new CommandListener() {

                            @Override
                            public void onResult(int result, String message) {
                                ToastUtil.getInstance().onResult(result, message);
                            }
                        });
                TestUtil.updateApi("goForward");
                break;
            case R.id.button_move_back_forward:
                if (TextUtils.isEmpty(mEditMoveSpeed.getText()) || TextUtils.isEmpty(mEditMoveDistance.getText())) {
                    ToastUtil.getInstance().toast("参数不能为空");
                    return;
                }
                RobotApi.getInstance().goBackward(Constants.REQUEST_ID_DEFAULT,
                        getFloatValue(mEditMoveSpeed),
                        getFloatValue(mEditMoveDistance),
                        new CommandListener() {

                            @Override
                            public void onResult(int result, String message) {
                                ToastUtil.getInstance().onResult(result, message);
                            }
                        });
                TestUtil.updateApi("goBackward");

                break;
            case R.id.editText_move_turn_left:
                if (TextUtils.isEmpty(mEditTurnSpeed.getText()) || TextUtils.isEmpty(mEditTurnAngle.getText())) {
                    ToastUtil.getInstance().toast("参数不能为空，速度要大于10");
                    return;
                }
                //速度数值较小时（10以下），转动的角度不准确。目前解决方案是提高转动速度。
                RobotApi.getInstance().turnLeft(Constants.REQUEST_ID_DEFAULT,
                        getFloatValue(mEditTurnSpeed),
                        getFloatValue(mEditTurnAngle),
                        new CommandListener() {

                            @Override
                            public void onResult(int result, String message) {
                                ToastUtil.getInstance().onResult(result, message);
                            }
                        });
                TestUtil.updateApi("turnLeft");
                break;
            case R.id.editText_move_turn_right:
                if (TextUtils.isEmpty(mEditTurnSpeed.getText()) || TextUtils.isEmpty(mEditTurnAngle.getText())) {
                    ToastUtil.getInstance().toast("参数不能为空，速度要大于10");
                    return;
                }
                //速度数值较小时（10以下），转动的角度不准确。目前解决方案是提高转动速度。
                RobotApi.getInstance().turnRight(Constants.REQUEST_ID_DEFAULT,
                        getFloatValue(mEditTurnSpeed),
                        getFloatValue(mEditTurnAngle),
                        new CommandListener() {

                            @Override
                            public void onResult(int result, String message) {
                                ToastUtil.getInstance().onResult(result, message);
                            }
                        });
                TestUtil.updateApi("turnRight");

                break;
            case R.id.editText_move_move_arc:
                RobotApi.getInstance().motionArc(Constants.REQUEST_ID_DEFAULT,
                        Float.valueOf(mEditLineSpeed.getText().toString()),
                        Float.valueOf(mEditAngleSpeed.getText().toString()),
                        new CommandListener() {

                            @Override
                            public void onResult(int result, String message) {
                                ToastUtil.getInstance().onResult(result, message);
                            }
                        });
                TestUtil.updateApi("motionArc");

                break;
            case R.id.editText_move_move_head_absolute:
                if (TextUtils.isEmpty(mEditHeadHorizontal.getText()) || TextUtils.isEmpty(mEditHeadVertical.getText())) {
                    ToastUtil.getInstance().toast("参数不能为空");
                    return;
                }
                RobotApi.getInstance().moveHead(Constants.REQUEST_ID_DEFAULT,
                        "absolute", "absolute",
                        getIntValue(mEditHeadHorizontal),
                        getIntValue(mEditHeadVertical),
                        new CommandListener() {

                            @Override
                            public void onResult(int result, String message) {
                                ToastUtil.getInstance().onResult(result, message);
                            }
                        });
                TestUtil.updateApi("moveHead");
                break;
            case R.id.editText_move_move_head_relative:
                if (TextUtils.isEmpty(mEditHeadHorizontal.getText()) || TextUtils.isEmpty(mEditHeadVertical.getText())) {
                    ToastUtil.getInstance().toast("参数不能为空");
                    return;
                }
                RobotApi.getInstance().moveHead(Constants.REQUEST_ID_DEFAULT,
                        "relative", "relative",
                        getIntValue(mEditHeadHorizontal),
                        getIntValue(mEditHeadVertical),
                        new CommandListener() {

                            @Override
                            public void onResult(int result, String message) {
                                ToastUtil.getInstance().onResult(result, message);
                            }
                        });
                TestUtil.updateApi("moveHead");

                break;
            case R.id.editText_move_stop_all:
                RobotApi.getInstance().stopMove(Constants.REQUEST_ID_DEFAULT, new CommandListener() {
                    @Override
                    public void onResult(int result, String message) {
                        ToastUtil.getInstance().onResult(result, message);
                    }
                });
                TestUtil.updateApi("stopMove");

                break;
            case R.id.editText_move_move_head_reset:
                RobotApi.getInstance().resetHead(Constants.REQUEST_ID_DEFAULT, new CommandListener() {
                    @Override
                    public void onResult(int result, String message) {
                        ToastUtil.getInstance().onResult(result, message);
                    }
                });
                TestUtil.updateApi("resetHead");
                break;
            default:
                break;
        }
    }

    private int getIntValue(EditText editText) {
        if (editText == null || TextUtils.isEmpty(editText.getText().toString())) {
            return 0;
        }

        return Integer.valueOf(editText.getText().toString());
    }

    private float getFloatValue(EditText editText) {
        if (editText == null || TextUtils.isEmpty(editText.getText().toString())) {
            return 0f;
        }

        return Float.valueOf(editText.getText().toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RobotApi.getInstance().stopMove(Constants.REQUEST_ID_DEFAULT, new CommandListener() {
            @Override
            public void onResult(int result, String message) {
                Log.e(TAG, "onResult: end");
            }
        });
    }
}
