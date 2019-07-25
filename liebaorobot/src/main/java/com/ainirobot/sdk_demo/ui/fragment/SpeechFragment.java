package com.ainirobot.sdk_demo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ainirobot.coreservice.client.RobotApi;
import com.ainirobot.coreservice.client.listener.TextListener;

import com.ainirobot.sdk_demo.R;
import com.ainirobot.sdk_demo.model.BaseFragment;
import com.ainirobot.sdk_demo.skill.SpeechSkill;
import com.ainirobot.sdk_demo.utils.Constants;
import com.ainirobot.sdk_demo.utils.TestUtil;
import com.ainirobot.sdk_demo.utils.ToastUtil;


/**
 * @author Orion
 * @time 2018/9/11
 */
public class SpeechFragment extends BaseFragment {

    private static final String TAG = "SpeechFragment";

    private View mRootView;
    private Switch mModeSwitch;

    private EditText mSpeechText;
    private Button mPlayButton;
    private Button mStopButton;
    //
    private Button mSetRecognizable;
    private Button mSetRecognizUnable;
    private Button mOpenAsrBtn;
    private Button mCloseAsrBtn;
    //
    private EditText mSpeechCenter;
    private EditText mSpeechRange;
    private Button mSetAngleBtn;
    //
    private TextView mResultText;
    private Button mWakeUp;
    private Button mStopWakeUp;
    private SpeechSkill.OnSpeechCallBack skillCallback = new SpeechSkill.OnSpeechCallBack() {

        @Override
        public void onSpeechParResult(final String s) {
            //用户说话临时识别结果
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StringBuilder text = new StringBuilder();
                        text.append(mResultText.getText().toString());
                        text.append("\n").append(s);
                        mResultText.setText(text.toString());
                    }
                });
            }
        }

        @Override
        public void onStart() {
            //用户开始说话
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mResultText.setText("语音识别结果：");
                    }
                });
            }
        }

        @Override
        public void onStop() {
            //用户说话结束
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mResultText.setText(mResultText.getText().toString() + "\n语音识别结束");
                    }
                });
            }
        }

        @Override
        public void onVolumeChange(int i) {
            //用户说话声音大小变化
        }

        @Override
        public void onQueryEnded(int i) {
            Log.d(TAG, "onQueryEnded: ");
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SpeechSkill.getInstance().getSkillApi().setRecognizeMode(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.layout_speech, container, false);
        // TODO: 2019/6/26 添加toast
        mResultText = (TextView) mRootView.findViewById(R.id.textView_speech_result);
        mSpeechText = (EditText) mRootView.findViewById(R.id.editText_speech_test_conent);
        mModeSwitch = (Switch) mRootView.findViewById(R.id.switch_speech_mode);
        mPlayButton = (Button) mRootView.findViewById(R.id.button_speech_start_play);
        mStopButton = (Button) mRootView.findViewById(R.id.button_speech_stop_play);
        //
        mSetRecognizable = (Button) mRootView.findViewById(R.id.button_speech_open_recognizable);
        mSetRecognizUnable = (Button) mRootView.findViewById(R.id.button_speech_close_recognizable);
        mOpenAsrBtn = (Button) mRootView.findViewById(R.id.button_speech_open_asr);
        mCloseAsrBtn = (Button) mRootView.findViewById(R.id.button_speech_close_asr);
        //
        mSpeechCenter = (EditText) mRootView.findViewById(R.id.speech_set_angle_center);
        mSpeechRange = (EditText) mRootView.findViewById(R.id.speech_set_angle_range);
        mSetAngleBtn = (Button) mRootView.findViewById(R.id.button_speech_set_angle);
        mWakeUp = (Button) mRootView.findViewById(R.id.button_wake_up);
        mStopWakeUp = (Button) mRootView.findViewById(R.id.button_stop_wakeup);

        setOnClickListener(mPlayButton, mStopButton, mSetRecognizable, mSetRecognizUnable,
                mOpenAsrBtn, mCloseAsrBtn, mSetAngleBtn,mWakeUp,mStopWakeUp);
        init();
        return mRootView;
    }

    private void init() {
        mModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpeechSkill.getInstance().getSkillApi().setRecognizeMode(true);
                } else {
                    SpeechSkill.getInstance().getSkillApi().setRecognizeMode(false);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: 连接语音服务");
        SpeechSkill.getInstance().addCallBack(skillCallback);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_speech_start_play:
                if (TextUtils.isEmpty(mSpeechText.getText().toString())) {
                    ToastUtil.getInstance().toast("播报内容不能为空");
                    break;
                }
                SpeechSkill.getInstance().playTxt(mSpeechText.getText().toString(), new TextListener() {
                    @Override
                    public void onStart() {
                        //播放开始
                    }

                    @Override
                    public void onStop() {
                        //播放停止
                    }

                    @Override
                    public void onError() {
                        //播放错误
                        Toast.makeText(getActivity(), "播放出现错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        //播放完成
                    }
                });
                TestUtil.updateApi("playText");

                break;
            case R.id.button_speech_stop_play:
                SpeechSkill.getInstance().getSkillApi().stopTTS();
                ToastUtil.getInstance().toast("已停止");
                TestUtil.updateApi("stopTTS");
                break;
            case R.id.button_speech_open_recognizable:
                SpeechSkill.getInstance().getSkillApi().setRecognizable(true);
                ToastUtil.getInstance().toast("开启语音识别");
                TestUtil.updateApi("setRecognizable");
                break;
            case R.id.button_speech_close_recognizable:
                SpeechSkill.getInstance().getSkillApi().setRecognizable(false);
                ToastUtil.getInstance().toast("关闭语音识别");
                TestUtil.updateApi("setRecognizable");

                break;
            case R.id.button_speech_open_asr:
                SpeechSkill.getInstance().getSkillApi().setASREnabled(true);
                ToastUtil.getInstance().toast("开启语音");
                TestUtil.updateApi("setASREnabled");

                break;
            case R.id.button_speech_close_asr:
                SpeechSkill.getInstance().getSkillApi().setASREnabled(false);
                ToastUtil.getInstance().toast("关闭语音");
                TestUtil.updateApi("setASREnabled");

                break;
            case R.id.button_speech_set_angle:
                if (TextUtils.isEmpty(mSpeechCenter.getText().toString())
                        || TextUtils.isEmpty(mSpeechRange.getText().toString())) {
                    ToastUtil.getInstance().toast("参数不能为空");
                    break;
                }
                SpeechSkill.getInstance().getSkillApi().setAngleCenterRange(
                        Float.valueOf(mSpeechCenter.getText().toString()),
                        Float.valueOf(mSpeechRange.getText().toString()));
                ToastUtil.getInstance().toast("设置语音识别区域");
                TestUtil.updateApi("setAngleCenterRange");
                break;
            case R.id.button_wake_up:
                //query '小豹小豹'，详见MessageManager
                TestUtil.updateApi("wakeUp");
                break;
            case R.id.button_stop_wakeup:
                RobotApi.getInstance().stopWakeUp(Constants.REQUEST_ID_DEFAULT);
                TestUtil.updateApi("stopWakeUp");
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SpeechSkill.getInstance().removeCallBack(skillCallback);
        SpeechSkill.getInstance().getSkillApi().setRecognizeMode(false);
    }

}
