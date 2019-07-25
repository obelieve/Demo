package com.ainirobot.sdk_demo.ui.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.ainirobot.coreservice.client.listener.CommandListener;
import com.ainirobot.sdk_demo.R;
import com.ainirobot.sdk_demo.model.BaseFragment;
import com.ainirobot.sdk_demo.skill.SystemSkill;
import com.ainirobot.sdk_demo.utils.TestUtil;
import com.ainirobot.sdk_demo.utils.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SystemFragment extends BaseFragment {

    private static final String TAG = SystemFragment.class.getSimpleName();

    private Button mRobotSNBtn;
    private TextView mRobotSNTextView;
    private Button mTextToMp3Btn;
    private Button mPlayMp3Btn;
    private Button mSetLightBtn;
    private EditText mInputText;
    private EditText mStartColor;
    private EditText mEndColor;
    private EditText mStartColorTime;
    private EditText mEndColorTime;
    private EditText mLightDuration;
    private EditText mLoopTimes;
    private EditText mFinalColor;
    private Button mBackToModuleAppBtn;
    private Button mInstallApk;

    private  MediaPlayer mediaPlayer = new MediaPlayer();
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG,"onReceive");
            if (intent != null
                    && "ainirobot.intent.action.INSTALL_RESULT".equals(intent.getAction())) {
                String taskId = intent.getStringExtra("task_id");
                boolean result = intent.getBooleanExtra("result", false);
                String errorMsg = intent.getStringExtra("error_msg");

                if (result){
                    ToastUtil.getInstance().toast("安装成功！");
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_system, container, false);

        mRobotSNTextView = (TextView) view.findViewById(R.id.textView_sn_info);
        mInputText = (EditText) view.findViewById(R.id.editText_text_to_mp3);
        mStartColor = (EditText) view.findViewById(R.id.edit_start_color);
        mEndColor = (EditText) view.findViewById(R.id.edit_end_color);
        mStartColorTime = (EditText) view.findViewById(R.id.edit_start_color_time);
        mEndColorTime = (EditText) view.findViewById(R.id.edit_end_color_time);
        mLightDuration = (EditText) view.findViewById(R.id.edit_light_duration);
        mLoopTimes = (EditText) view.findViewById(R.id.edit_loop_times);
        mFinalColor = (EditText) view.findViewById(R.id.edit_final_color);


        mRobotSNBtn = (Button) view.findViewById(R.id.button_get_sn);
        mTextToMp3Btn = (Button) view.findViewById(R.id.button_text_to_mp3);
        mPlayMp3Btn = (Button) view.findViewById(R.id.button_play_mp3);
        mSetLightBtn = (Button) view.findViewById(R.id.button_set_light);
        mBackToModuleAppBtn = (Button) view.findViewById(R.id.button_back_to_module_app);
        mInstallApk = (Button) view.findViewById(R.id.button_install_apk);


        setOnClickListener(mRobotSNBtn,mTextToMp3Btn,mPlayMp3Btn,mSetLightBtn,mBackToModuleAppBtn,mInstallApk);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ainirobot.intent.action.INSTALL_RESULT");
        getActivity().registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button_get_sn:
                showSNInfo();
                TestUtil.updateApi("getRobotSn");
                break;
            case R.id.button_text_to_mp3:
                textToMp3();
                TestUtil.updateApi("textToMp3");
                break;
            case R.id.button_play_mp3:
                checkStoragePermission();
                break;
            case R.id.button_set_light:
                setLight();
                break;
            case R.id.button_back_to_module_app:
                backToModuleApp();
                break;
            case R.id.button_install_apk:
                installApk();
                break;
            default:
                break;
        }
    }



    private void showSNInfo(){
        Log.d(TAG,"showSNInfo");
        SystemSkill.getInstance().getRobotSn(new CommandListener(){
            @Override
            public void onResult(int result, String message) {
                if (Definition.RESULT_OK == result){
                    mRobotSNTextView.setText(message);
                }
            }
        });
    }

    private void textToMp3(){
        if (TextUtils.isEmpty(mInputText.getText())){
            return;
        }
        File file = new File("/sdcard/test.mp3");
        if (file.exists()){
            file.delete();
        }
        SystemSkill.getInstance().textToMp3(mInputText.getText().toString(),new CommandListener(){
            @Override
            public void onResult(int result, String message) {

            }
        });
    }

    private void playMp3(){
        Log.d(TAG,"playMp3");
        try {
            mediaPlayer.setDataSource("/sdcard/test.mp3");
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.reset();
                }
            });
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            mediaPlayer.reset();
        }
    }

    private void checkStoragePermission(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }else {
            playMp3();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 0:
                if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    playMp3();
                }
                break;
        }
    }

    private void setLight(){

        int startColor,endColor,startColorTime,endColorTime,lightDuration,loopTimes,finalColor;

        if (TextUtils.isEmpty(mStartColor.getText())){
            ToastUtil.getInstance().toast("请输入开始颜色");
            return;
        }else {
            startColor = checkColor(mStartColor.getText().toString());
            if (startColor == Integer.MIN_VALUE){
                ToastUtil.getInstance().toast("开始颜色格式错误");
                return;
            }
        }

        if (TextUtils.isEmpty(mEndColor.getText())){
            ToastUtil.getInstance().toast("请输入结束颜色");
            return;
        }else {
            endColor = checkColor(mEndColor.getText().toString());
            if (endColor == Integer.MIN_VALUE){
                ToastUtil.getInstance().toast("结束颜色格式错误");
                return;
            }
        }

        if (TextUtils.isEmpty(mFinalColor.getText())){
            ToastUtil.getInstance().toast("请输入循环结束颜色");
            return;
        }else {
            finalColor = checkColor(mFinalColor.getText().toString());
            if (finalColor == Integer.MIN_VALUE){
                ToastUtil.getInstance().toast("循环结束颜色格式错误");
                return;
            }
        }

        if (TextUtils.isEmpty(mStartColorTime.getText())){
            ToastUtil.getInstance().toast("请输入开始颜色时间");
            return;
        }else {
            startColorTime = checkTime(mStartColorTime.getText().toString());
            if (startColorTime == Integer.MIN_VALUE){
                ToastUtil.getInstance().toast("开始时间格式错误");
                return;
            }
        }

        if (TextUtils.isEmpty(mEndColorTime.getText())){
            ToastUtil.getInstance().toast("请输入结束颜色时间");
            return;
        }else {
            endColorTime = checkTime(mEndColorTime.getText().toString());
            if (endColorTime == Integer.MIN_VALUE){
                ToastUtil.getInstance().toast("结束时间格式错误");
                return;
            }
        }

        if (TextUtils.isEmpty(mLightDuration.getText())){
            ToastUtil.getInstance().toast("请输入渐变持续时间");
            return;
        }else {
            lightDuration = checkTime(mLightDuration.getText().toString());
            if (lightDuration == Integer.MIN_VALUE){
                ToastUtil.getInstance().toast("渐变持续时间格式错误");
                return;
            }
        }

        if (TextUtils.isEmpty(mLoopTimes.getText())){
            ToastUtil.getInstance().toast("请输入循环次数");
            return;
        }else {
            loopTimes = checkTime(mLoopTimes.getText().toString());
            if (loopTimes == Integer.MIN_VALUE){
                ToastUtil.getInstance().toast("循环次数格式错误");
                return;
            }
        }

        TestUtil.updateApi("setLight");
        SystemSkill.getInstance().setLight(startColor,endColor,finalColor,startColorTime,endColorTime,loopTimes,lightDuration);
    }

    private int checkColor(String color){
        int result = Integer.MIN_VALUE;

        if (color.length() != 6){
            return result;
        }

        try {
            result = Integer.parseInt(color,16);
        }catch (NumberFormatException e){
            e.printStackTrace();
            return Integer.MIN_VALUE;
        }

        return result;
    }

    private int checkTime(String time){
        int result = Integer.MIN_VALUE;

        try {
            result = Integer.parseInt(time);
        }catch (NumberFormatException e){
            e.printStackTrace();
            return Integer.MIN_VALUE;
        }

        return result;
    }

    private void backToModuleApp(){
        PackageManager pm = getActivity().getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(Definition.MODULE_PACKAGE_NAME);
        if (intent != null){
            startActivity(intent);
        }
    }

    private void installApk(){

        File file = new File("/sdcard/test.apk");
        if (file.exists()){
            TestUtil.updateApi("installApk");
            SystemSkill.getInstance().installApk();
        }else {
            ToastUtil.getInstance().toast("请检查apk是否存在");
        }
    }

}
