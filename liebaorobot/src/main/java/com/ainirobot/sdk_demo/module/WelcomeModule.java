package com.ainirobot.sdk_demo.module;

import android.text.TextUtils;
import android.util.Log;

import com.ainirobot.coreservice.client.RobotApi;
import com.ainirobot.coreservice.client.listener.Person;
import com.ainirobot.sdk_demo.R;
import com.ainirobot.sdk_demo.RobotApplication;
import com.ainirobot.sdk_demo.model.bean.MessageEvent;
import com.ainirobot.sdk_demo.model.bean.VisualDetectBean;
import com.ainirobot.sdk_demo.skill.ControlManager;
import com.ainirobot.sdk_demo.skill.SkillManager;
import com.ainirobot.sdk_demo.skill.SpeechSkill;
import com.ainirobot.sdk_demo.skill.VisualDetectManager;
import com.ainirobot.sdk_demo.utils.Constants;
import com.ainirobot.sdk_demo.utils.MessageParser;

import org.greenrobot.eventbus.EventBus;

/**
 * 欢迎模式 包含 人脸检测，人脸识别，问候语，自定义问答
 */
public class WelcomeModule extends BaseModule {
    private static final String TAG = WelcomeModule.class.getSimpleName();
    private static WelcomeModule sWelcomeModule;
    private Person mCurrentPerson;
    private WelcomeListener mWelcomeListener;
    /**
     * 是否已经人脸唤醒，人脸唤醒后可以闲聊
     * true 已经唤醒
     * false 未唤醒
     */
    private boolean isWakedUp = false;

    public static WelcomeModule getInstance() {
        if (null == sWelcomeModule) {
            sWelcomeModule = new WelcomeModule();
        }
        return sWelcomeModule;
    }

    @Override
    public void start(String params) {
        EventBus.getDefault().post(new MessageEvent(Constants.FRAGMENT_TYPE.WELCOME));
        //设置语音连续识别
        SpeechSkill.getInstance().getSkillApi().setRecognizeMode(true);

        if (mWelcomeListener != null) {
            mWelcomeListener.standby();
        }
        doFaceSearch();
    }

    /**
     * 自定义问答
     *
     * @param params 问题和答案
     */
    @Override
    public void update(String params) {
        Log.e(TAG, "params is " + params);
        if (isWakedUp) {
            String answerText = MessageParser.getAnswerText(params);
            String userText = MessageParser.getUserText(params);
            if (!TextUtils.isEmpty(answerText)) {
                mWelcomeListener.chat(userText, answerText);
                SpeechSkill.getInstance().playTxt(answerText);
            }
            VisualDetectManager.getInstance().continueSearchFace();
        }
    }

    @Override
    public void stop() {
        VisualDetectManager.getInstance().stopVisualDetect();
        if (mWelcomeListener != null) {
            mWelcomeListener.stop();
        }
        SpeechSkill.getInstance().getSkillApi().setRecognizeMode(false);
        isWakedUp = false;
        ControlManager.getInstance().resetMode();
    }

    private VisualDetectManager.VisualDetectListener mVisualDetectListener = new VisualDetectManager
            .VisualDetectListener() {
        /**
         * 唤醒
         * 1.设置连续识别
         * 2.提示已唤醒
         * 3.机器人播放欢迎语
         * @param person
         */
        @Override
        public void onWakeup(Person person) {
            SpeechSkill.getInstance().getSkillApi().setRecognizeMode(true);
            mCurrentPerson = person;
            isWakedUp = true;
            if (mWelcomeListener != null) {
                mWelcomeListener.wakeUp();
            }
            doWakeUpWords();
        }

        @Override
        public void onRecognizeRetry(Person person) {
            mCurrentPerson = person;
            Log.e(TAG, "识别出了该人，名字：" + mCurrentPerson.getName());
        }

        /**
         * 跟踪中
         * 1.开始识别人脸数据
         * @param person
         */
        @Override
        public void onTracking(Person person) {
            Log.d(TAG, "onTracking WelcomeState: ");
            RobotApi.getInstance().startRecordFaceData();
        }

        /**
         * 跟踪结束
         * 1.结束识别人脸数据
         */
        @Override
        public void onTrackEnd() {
            Log.d(TAG, "onTrackEnd WelcomeState: ");
            RobotApi.getInstance().stopRecordFaceData();
        }

        /**
         * 唤醒结束
         * 1.设置设备初始角度
         * 2.撤销连续识别语音；停止语音操作
         * 3.提示人脸检测中
         * 4.设置人脸搜索
         */
        @Override
        public void onWakeupEnd() {
            Log.d(TAG, "onWakeupEnd WelcomeState: ");
            mCurrentPerson = null;
            RobotApi.getInstance().resetHead(0, null);
            SkillManager.getInstance().cancelWakeupUi();
            isWakedUp = false;
            if (mWelcomeListener != null) {
                mWelcomeListener.standby();
            }
            doFaceSearch();//感觉调不调用无所谓
        }

        /**
         * 预唤醒说话
         * @param person
         */
        @Override
        public void onPreWakeup(Person person) {
            Log.e(TAG, "开始预唤醒说话");
            SpeechSkill.getInstance().playTxt(RobotApplication.getInstance().getString(R.string.orion_interview));
        }

        /**
         * 预唤醒结束，重设初始角度
         */
        @Override
        public void onPreWakeupEnd() {
            Log.d(TAG, "onPreWakeupEnd");
            RobotApi.getInstance().resetHead(0, null);
        }


        @Override
        public void onStop() {
            Log.d(TAG, "onStop");
        }
    };

    /**
     * 人脸检测，人脸识别
     */
    private void doFaceSearch() {
        VisualDetectBean bean = new VisualDetectBean();
        bean.setNeedRecognize(true);
        bean.setNeedPreWakeUp(false);
        bean.setNeedTrack(true);
        bean.setNeedAssignedPersonLostTimer(false);
        VisualDetectManager.getInstance().startVisualDetect(bean, mVisualDetectListener);
    }

    /**
     * 问候语
     */
    private void doWakeUpWords() {
        String wakeUpWords;
        if (mCurrentPerson == null || TextUtils.isEmpty(mCurrentPerson.getName())) {
            Log.d(TAG, "mCurrentPerson | name is null");
            Log.e(TAG, "小豹直接唤醒");
            wakeUpWords = RobotApplication.getInstance().getString(R.string.orion_ask_me);
            SpeechSkill.getInstance().playTxt(wakeUpWords);
        } else {
            Log.e(TAG, "小豹人名上屏唤醒，人名："
                    + mCurrentPerson.getName());
            wakeUpWords = mCurrentPerson.getName() + "\t" + RobotApplication.getInstance().getString(R.string.orion_ask_me);
            SpeechSkill.getInstance().playTxt(wakeUpWords);
        }
        if (mWelcomeListener != null) {
            mWelcomeListener.chat("问候语：", wakeUpWords);
        }
    }

    public void registerWelcomeListener(WelcomeListener listener) {
        this.mWelcomeListener = listener;
    }

    public void unWelcomeLeadListener() {
        this.mWelcomeListener = null;
    }

    public interface WelcomeListener {
        /**
         * 已唤醒
         */
        void wakeUp();

        /**
         * 初始化或者人脸丢失一段时间
         */
        void standby();

        /**
         * 闲聊回调
         * @param question 问题
         * @param answer 答案
         */
        void chat(String question, String answer);

        /**
         * 本次唤醒结束
         */
        void stop();

    }
}
