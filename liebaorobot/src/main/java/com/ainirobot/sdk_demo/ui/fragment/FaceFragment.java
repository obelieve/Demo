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
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ainirobot.coreservice.client.Definition;
import com.ainirobot.coreservice.client.RobotApi;
import com.ainirobot.coreservice.client.actionbean.LeadingParams;
import com.ainirobot.coreservice.client.listener.ActionListener;
import com.ainirobot.coreservice.client.listener.CommandListener;
import com.ainirobot.coreservice.client.listener.Person;
import com.ainirobot.coreservice.client.listener.PersonInfoListener;

import java.util.ArrayList;
import java.util.List;

import com.ainirobot.sdk_demo.R;
import com.ainirobot.sdk_demo.control.PersonInfo;
import com.ainirobot.sdk_demo.model.BaseFragment;
import com.ainirobot.sdk_demo.skill.FaceSkill;
import com.ainirobot.sdk_demo.utils.Constants;
import com.ainirobot.sdk_demo.utils.TestUtil;
import com.ainirobot.sdk_demo.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Orion
 * @time 2018/9/11
 */
public class FaceFragment extends BaseFragment {

    private static final String TAG = "FaceFragment";

    private View mContainer;
    private ScrollView mScrollView;
    //
    private EditText mEditName;
    private Button mBtnRegister;
    private Button mBtnStopRegister;
    //
    private EditText mEditPersonId;
    private EditText mEditTarget;
    private Button mBtnStartLead;
    private Button mBtnStopLead;
    //
    private EditText mEditPersonId2;
    private Button mBtnStartFollow;
    private Button mBtnStopFollow;
    private Button mSwitchCamera;
    //
    private EditText mPicPersonId;
    private TextView mPicturePath;
    private Button mBtnGetPicture;
    //
    private Button mStartGetPersonInfo;
    private Button mStopGetPersonInfo;
    private TextView mPersonInfo;
    private RadioGroup mRg;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainer = inflater.inflate(R.layout.layout_face, container, false);
        mScrollView = mContainer.findViewById(R.id.face_fragment_root_scroll);
        //
        mEditName = (EditText) mContainer.findViewById(R.id.editText_face_person_name);
        mBtnRegister = (Button) mContainer.findViewById(R.id.button_face_start_register);
        mBtnStopRegister = (Button) mContainer.findViewById(R.id.button_face_stop_register);
        //
        mEditPersonId = (EditText) mContainer.findViewById(R.id.editText_face_lead_personid);
        mEditTarget = (EditText) mContainer.findViewById(R.id.editText_face_lead_target);
        mBtnStartLead = (Button) mContainer.findViewById(R.id.button_face_start_lead);
        mBtnStopLead = (Button) mContainer.findViewById(R.id.button_face_stop_lead);
        //
        mEditPersonId2 = (EditText) mContainer.findViewById(R.id.editText_face_focus_follow_personid);
        mBtnStartFollow = (Button) mContainer.findViewById(R.id.button_face_focus_follow_start);
        mBtnStopFollow = (Button) mContainer.findViewById(R.id.button_face_focus_follow_stop);
        //
        mSwitchCamera = (Button) mContainer.findViewById(R.id.button_switch_camera);
        //
        mPicPersonId = (EditText) mContainer.findViewById(R.id.editText_face_person_id);
        mPicturePath = (TextView) mContainer.findViewById(R.id.textView_person_picture_path);
        mBtnGetPicture = (Button) mContainer.findViewById(R.id.button_get_person_picture_id);
        //
        mStartGetPersonInfo = (Button) mContainer.findViewById(R.id.button_get_all_person_info);
        mStopGetPersonInfo = (Button) mContainer.findViewById(R.id.button_stop_get_all_person_info);

        mPicturePath = (TextView) mContainer.findViewById(R.id.textView_person_picture_path);
        mPersonInfo = (TextView) mContainer.findViewById(R.id.textView_all_person_info);

        setOnClickListener(mBtnRegister, mBtnStopRegister, mBtnStartLead, mBtnStopLead, mBtnStartFollow,
                mBtnStopFollow, mSwitchCamera, mBtnGetPicture, mStartGetPersonInfo, mStopGetPersonInfo);
        mRg = mContainer.findViewById(R.id.rg_group);
        mRg.check(R.id.rb_1);
        return mContainer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_face_start_register:
                if (TextUtils.isEmpty(mEditName.getText().toString())) {
                    ToastUtil.getInstance().toast("参数不能为空");
                    return;
                }
                FaceSkill.getInstance().register(mEditName.getText().toString());
                TestUtil.updateApi("startRegister");

                break;
            case R.id.button_face_stop_register:
                RobotApi.getInstance().stopRegister(Constants.REQUEST_ID_DEFAULT);
                TestUtil.updateApi("stopRegister");
                break;
            case R.id.button_face_start_lead:
                if (TextUtils.isEmpty(mEditPersonId.getText().toString())) {
                    ToastUtil.getInstance().toast("参数不能为空");
                    return;
                }
                LeadingParams params = new LeadingParams();
                params.setPersonId(Integer.valueOf(mEditPersonId.getText().toString()));
                params.setDestinationName(mEditTarget.getText().toString());
                // 丢失判断
                params.setLostTimer(2 * 1000);
                // 避障超时
                params.setAvoidTimeout(30 * 1000);
                params.setAvoidDistance(2.0d);
                // 超距等待
                params.setWaitTimeout(12 * 60 * 60 * 1000);
                params.setMaxDistance(2.8d);
                FaceSkill.getInstance().startLead(Constants.REQUEST_ID_DEFAULT, params, new ActionListener() {
                    @Override
                    public void onResult(int status, String responseString) throws RemoteException {
                        ToastUtil.getInstance().onResult(status, responseString);
                    }
                    @Override
                    public void onError(int errorCode, String errorString) throws RemoteException {
                        super.onError(errorCode, errorString);
                        ToastUtil.getInstance().onError(errorCode, errorString);
                    }
                });
                TestUtil.updateApi("startLead");
                break;
            case R.id.button_face_stop_lead:
                FaceSkill.getInstance().stopLead(Constants.REQUEST_ID_DEFAULT, true);
                TestUtil.updateApi("stopLead");
                break;
            case R.id.button_face_focus_follow_start:
                if (TextUtils.isEmpty(mEditPersonId2.getText().toString())) {
                    ToastUtil.getInstance().toast("参数不能为空");
                    return;
                }
                FaceSkill.getInstance().startFocusFollow(Constants.REQUEST_ID_DEFAULT,
                        Integer.valueOf(mEditPersonId2.getText().toString()), 5000, 1.5f,
                        new ActionListener() {
                            @Override
                            public void onStatusUpdate(int status, String data) {
                                Log.d(TAG, "startTrackPerson onStatusUpdate status: " + status + ", " + data);
                                switch (status) {
                                    case Definition.STATUS_TRACK_TARGET_SUCCEED:
                                        Log.d(TAG, "onStatusUpdate: STATUS_TRACK_TARGET_SUCCEED");
                                        ToastUtil.getInstance().toast("STATUS_TRACK_TARGET_SUCCEED: " + data);
                                        break;
                                    case Definition.STATUS_GUEST_LOST:
                                        Log.d(TAG, "onStatusUpdate: STATUS_GUEST_LOST");
                                        ToastUtil.getInstance().toast("STATUS_GUEST_LOST: " + data);
                                        break;
                                    case Definition.STATUS_GUEST_FARAWAY:
                                        Log.d(TAG, "onStatusUpdate: STATUS_GUEST_FARAWAY");
                                        ToastUtil.getInstance().toast("STATUS_GUEST_FARAWAY: " + data);
                                        break;
                                    default:
                                        Log.d(TAG, "default");
                                        break;
                                }
                            }

                            @Override
                            public void onError(int errorCode, String errorString) {
                                Log.d(TAG, "startTrackPerson onError errorCode: " + errorCode + ", " + errorString);
                                ToastUtil.getInstance().onError(errorCode, errorString);
                            }

                            @Override
                            public void onResult(int status, String responseString) {
                                Log.d(TAG, "startTrackPerson onResult status: " + status);
                                switch (status) {
                                    case Definition.ACTION_RESPONSE_STOP_SUCCESS:
                                        Log.d(TAG, "onResult: ACTION_RESPONSE_STOP_SUCCESS");
                                        ToastUtil.getInstance().toast("ACTION_RESPONSE_STOP_SUCCESS: " + responseString);
                                        break;
                                    default:
                                        Log.d(TAG, "default");
                                        break;
                                }
                            }
                        });
                TestUtil.updateApi("startFocusFollow");
                break;
            case R.id.button_face_focus_follow_stop:
                FaceSkill.getInstance().stopFocusFollow(0);
                TestUtil.updateApi("stopFocusFollow");
                break;
            case R.id.button_switch_camera:
                FaceSkill.getInstance().switchCamera(mRg.getCheckedRadioButtonId()==R.id.rb_1?"forward":"backward", new CommandListener() {
                    @Override
                    public void onResult(int result, String message) {
                        ToastUtil.getInstance().onResult(result, message);
                        try {
                            JSONObject json = new JSONObject(message);
                            if (Definition.RESPONSE_OK.equals(json.getString("status"))) {
                                //切换成功
                                ToastUtil.getInstance().toast("切换成功");
                            }
                        } catch (JSONException | NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });
                TestUtil.updateApi("switchCamera");
                break;
            case R.id.button_get_all_person_info:
                Log.d(TAG, "onClick: getallpersoninfo");
                PersonInfo.getInstance().getAllPersonInfo();
                TestUtil.updateApi("startGetAllPersonInfo");
                PersonInfo.getInstance().registerPersonInfoListener(
                        new PersonInfoListener() {
                            @Override
                            public void onResult(int status, String responseString) {
                                Log.d(TAG, "onResult: " + status + ", " + responseString);
                                ToastUtil.getInstance().onResult(status, responseString);
                            }

                            @Override
                            public void onData(int code, final List<Person> data) {
                                Log.d(TAG, "onData: " + code + ", " + data.toString());
                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                            mPersonInfo.setText("=============start=============>>");
                                            for (Person person : data) {
                                                Log.d(TAG, "onData: " + person.toGson());
                                                String item = mPersonInfo.getText() + "\n==Person==>>\n" +
                                                        person.toGson() +
                                                        "============end==============>>";
                                                mPersonInfo.setText(item);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                break;
            case R.id.button_stop_get_all_person_info:
                stop();
                break;
            case R.id.button_get_person_picture_id:
                if (TextUtils.isEmpty(mPicPersonId.getText().toString())) {
                    ToastUtil.getInstance().toast("参数不能为空");
                    return;
                }
                getPicturePath(mPicPersonId.getText().toString());
                TestUtil.updateApi("getPictureById");
                break;
            default:
                break;
        }
    }

    private void stop() {
        PersonInfo.getInstance().stopGetAllPersonInfo();
        PersonInfo.getInstance().unRegisterPersonInfoListener(new PersonInfoListener() {
            @Override
            public void onResult(int status, String responseString) {
                Log.d(TAG, "stopGetAllPersonInfo:onResult: " + status + ", " + responseString);
                ToastUtil.getInstance().onResult(status, responseString);
            }

            @Override
            public void onData(int code, List<Person> data) {
                Log.d(TAG, "stopGetAllPersonInfo:onData: " + code);
            }
        });
        TestUtil.updateApi("stopGetAllPersonInfo");
    }

    private void getPicturePath(final String personId) {
        if (TextUtils.isEmpty(personId)) {
            ToastUtil.getInstance().toast("参数不能为空");
            return;
        }
        FaceSkill.getInstance().getPicturePath(Integer.valueOf(personId), new CommandListener() {
            @Override
            public void onResult(int result, String message) {
                try {
                    JSONObject json = new JSONObject(message);
                    String status = json.optString("status");
                    // 获取照片成功
                    if (Definition.RESPONSE_OK.equals(status)) {
                        JSONArray pictures = json.optJSONArray("pictures");
                        if (!TextUtils.isEmpty(pictures.optString(0))) {
                            // 照片本地存储全路径
                            String picturePath = pictures.optString(0);
                            getPersonInfoFromServer(picturePath, personId);
                        } else {
                            mPicturePath.setText("未找到");
                        }
                    } else {
                        mPicturePath.setText("未找到");
                    }
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                    mPicturePath.setText("未找到");
                }
            }
        });
    }

    private void getPersonInfoFromServer(String picturePath, String faceId) {
        List<String> pictures = new ArrayList<>();
        pictures.add(picturePath);
        final StringBuilder personInfo = new StringBuilder(picturePath);
        FaceSkill.getInstance().getPersonInfoFromServer(faceId, pictures, new CommandListener() {
            @Override
            public void onResult(int result, String message) {
                try {
//                    JSONObject json = new JSONObject(message);
//                    JSONObject info = json.getJSONObject("people");
                    personInfo.append("\n")
                            .append(message);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                mPicturePath.setText(personInfo.toString());
            }
        });
        TestUtil.updateApi("getPersonInfoFromNet");

    }
}
