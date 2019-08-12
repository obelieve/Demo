package com.ainirobot.sdk_demo.skill;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.ainirobot.coreservice.client.Definition;
import com.ainirobot.coreservice.client.RobotApi;
import com.ainirobot.coreservice.client.listener.ActionListener;
import com.ainirobot.coreservice.client.listener.CommandListener;
import com.ainirobot.coreservice.client.listener.Person;
import com.ainirobot.coreservice.client.listener.PersonInfoListener;
import com.ainirobot.sdk_demo.model.bean.PersonInfo;
import com.ainirobot.sdk_demo.model.bean.VisualDetectBean;
import com.ainirobot.sdk_demo.utils.Constants;
import com.ainirobot.sdk_demo.utils.MessageParser;
import com.ainirobot.sdk_demo.utils.VisualDetectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class VisualDetectManager {

    private static final String TAG = VisualDetectManager.class.getSimpleName();
    private static final float PRE_WAKE_AVAILABLE_DISTANCE = 3f;
    private static final float RECOGNIZE_AVAILABLE_DISTANCE = 1.3f;
    private static final double MAX_FACE_ANGLE_X = 45;
    private static final double MAX_FOCUS_FACE_ANGLE_X = 60;
    private static final long LOST_PERSON_TIMEOUT = 1000;
    private static final float LOST_PERSON_DISTANCE = 1.5f;
    private static final int MAX_PICTURE_NUM = 1;
    private static final long INVALID_TIME = Long.MAX_VALUE;
    private static final long NOT_NEED_TRACKED_DETECT_TIMEOUT = 3000;
    private static final int CANCEL_PRE_WAKEUP = 0X02;
    private static final int CANCEL_PRE_WAKEUP_WAIT = 0X03;
    private final static int CANCEL_PRE_WAKE_WAIT_TIME = 2000;
    private final static int PLAY_PRE_WAKE_LOOP_TIME = 23000;

    private int mReqId = 0;
    private static VisualDetectManager sVisualDetectManager;
    private volatile SearchFaceState mSearchFaceState;
    private Person mCurrentPerson;
    private PersonInfo mPersonInfo;
    private volatile VisualDetectListener mVisualDetectListener;
    private volatile Timer mTrackingTimer;
    private Object mLockTrackingTimer = new Object();
    private Timer mNetSearchTimer;
    private long mNotNeedTrackedDetectTime = INVALID_TIME;
    private boolean mNeedToPlayPreWakeInfo = true;
    protected Handler mHandler;
    protected HandlerThread mHandlerThread;
    private float mPreWakeMaxDistance = PRE_WAKE_AVAILABLE_DISTANCE;
    private VisualDetectBean mVisualDetectBean;
    private volatile boolean mIsStop;

    /**
     * 搜索人脸状态
     */
    private enum SearchFaceState {
        /**
         * 搜索人脸状态
         */
        //空闲，预唤醒，唤醒中，唤醒但未检测，仅唤醒，检测中，重新搜索
        IDLE, PRE_WAKE, WAKING, WAKED_BUT_NO_TRACK, WAKED_ONLY, TRACKING, RE_SEARCHING
    }


    public static VisualDetectManager getInstance() {
        if (sVisualDetectManager == null) {
            sVisualDetectManager = new VisualDetectManager();
        }

        return sVisualDetectManager;
    }

    private VisualDetectManager() {
        mCurrentPerson = null;
        updateSearchFaceState(SearchFaceState.IDLE);
        mIsStop = true;
        mHandlerThread = new HandlerThread("VisualDetectHandler");
        mHandlerThread.start();
        mHandler = new VisualDetectHandler(mHandlerThread.getLooper());
        updateWelcomeConfigData();
    }

    class VisualDetectHandler extends Handler {

        public VisualDetectHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, getLooper().getThread().getName() + ", handleMsg = " + msg.what);
            switch (msg.what) {
                case CANCEL_PRE_WAKEUP:
                    Log.d(TAG, "VisualDetectHandler CANCEL_PRE_WAKEUP");
                    mNeedToPlayPreWakeInfo = true;
                    mHandler.sendEmptyMessageDelayed(CANCEL_PRE_WAKEUP_WAIT, CANCEL_PRE_WAKE_WAIT_TIME);
                    break;
                case CANCEL_PRE_WAKEUP_WAIT:
                    Log.d(TAG, "VisualDetectHandler CANCEL_PRE_WAKEUP_WAIT");
                    if (mIsStop) {
                        Log.e(TAG, "CANCEL_PRE_WAKEUP_WAIT has stopped");
                        return;
                    }

                    if (SearchFaceState.PRE_WAKE == getSearchFaceState()) {
                        if (mVisualDetectListener != null) {
                            mVisualDetectListener.onPreWakeupEnd();
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * 开始检测人脸
     *
     * @param bean
     * @param listener
     */
    public void startVisualDetect(VisualDetectBean bean, VisualDetectListener listener) {
        Log.d(TAG, "startVisualDetect SearchFaceState: " + getSearchFaceState());
        mVisualDetectListener = listener;
        mVisualDetectBean = bean;
        if (mIsStop) {
            mIsStop = false;
            updateSearchFaceState(SearchFaceState.IDLE);
            startTrackingTimer(true);
        }
        startGetAllPersonInfo();
    }

    /**
     * 停止检测人脸 停止焦点跟随
     */
    public void stopVisualDetect() {
        Log.d(TAG, "stopVisualDetect SearchFaceState: " + getSearchFaceState() + ", isStop: " + mIsStop);
        if (mVisualDetectListener == null || mIsStop) {
            Log.e(TAG, "stopDetectFace has stopped");
            return;
        }
        mIsStop = true;
        if (SearchFaceState.TRACKING == getSearchFaceState()) {
            RobotApi.getInstance().stopFocusFollow(mReqId);
        }
        updateSearchFaceState(SearchFaceState.IDLE);
        RobotApi.getInstance().stopGetAllPersonInfo(mReqId, mWelcomePersonInfoListener);
        cancelNetSearchTimer();
        cancelTrackingTimer();
        mCurrentPerson = null;
        mVisualDetectListener.onStop();
        mVisualDetectListener = null;
        mNotNeedTrackedDetectTime = INVALID_TIME;
    }

    /**
     * 开始检测人脸
     */
    private void startGetAllPersonInfo() {
        Log.d(TAG, "startGetAllPersonInfo");
        RobotApi.getInstance().startGetAllPersonInfo(mReqId, mWelcomePersonInfoListener, true);//调用人脸检测接口
    }

    /**
     * 人脸信息回调监听
     */
    private PersonInfoListener mWelcomePersonInfoListener = new PersonInfoListener() {

        @Override
        public void onData(int errorCode, List<Person> personList) {
            if (mIsStop) {
                Log.e(TAG, "startGetAllPersonInfo has stopped");
                return;
            }

            Person person = MessageParser.getOnePersonWithFace(null, personList,
                    mPreWakeMaxDistance);//筛选出一个Person-face

            switch (getSearchFaceState()) {
                case IDLE:
                case PRE_WAKE:
                    Person personBody = MessageParser.getOnePersonWithBody(personList,
                            mPreWakeMaxDistance);//筛选出一个Person-body
                    if (person != null) {
                        Log.e(TAG, "看见前方有人");
                        if (Math.abs(person.getFaceAngleX()) <= MAX_FACE_ANGLE_X
                                && isRecognizeAvailableDistance(person.getDistance())) {//Person-face<=45 & Person-distance<=1.3m
                            Log.e(TAG, "满足唤醒条件");//满足唤醒条件
                            if (!canBeTracked(person)) {
                                Log.e(TAG, "红框条件 不处理");
                            } else {
                                Log.e(TAG, "蓝框条件");
                                mNotNeedTrackedDetectTime = INVALID_TIME;
                                handleDetectResult(person);
                            }
                        } else {
                            Log.e(TAG, "满足预唤醒条件");
                            startPreWake(person);
                        }
                    } else if (personBody != null) {
                        Log.i(TAG, "getAllPerson person body pre wake,");
                        startPreWake(personBody);
                    }
                    break;
                case RE_SEARCHING:
                case WAKED_BUT_NO_TRACK:
                    if (person != null && isTrackAvailableDistance(person.getDistance())
                            && Math.abs(person.getFaceAngleX()) <= MAX_FOCUS_FACE_ANGLE_X) {
                        mCurrentPerson = person;
                        if (canBeTracked(person)) {
                            cancelTrackingTimer();
                            updateSearchFaceState(SearchFaceState.TRACKING);
                            startTrackPerson();
                        } else {
                            startTrackingTimer(true);
                            updateSearchFaceState(SearchFaceState.WAKED_BUT_NO_TRACK);
                        }
                    }
                    break;
                case WAKED_ONLY:
                    if (person != null && isTrackAvailableDistance(person.getDistance())
                            && Math.abs(person.getFaceAngleX()) <= MAX_FOCUS_FACE_ANGLE_X) {
                        startTrackingTimer(true);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 远程识别人脸
     * 1.设置为“唤醒中”状态； SearchFaceState.WAKING
     * 2.有识别：
     *      -1.获取人脸图片路径
     *      -2.获取人脸网络信息
     *      -3.开始唤醒并跟随人脸(调用 visualWakeup())，并删除本地人脸图片
     * 3.无识别：
     *    开始唤醒并跟随人脸。(调用 visualWakeup())
     */
    private void handleDetectResult(Person person) {
        Log.d(TAG, "handle detect result isNeedRecognize: " + mVisualDetectBean.isNeedRecognize());
        if (mCurrentPerson == null
                || person.getId() != mCurrentPerson.getId()) {
            updateSearchFaceState(SearchFaceState.WAKING);//开启唤醒中状态
            mCurrentPerson = person;
            if (mVisualDetectBean.isNeedRecognize()) {//需要识别
                getPictureById();
                startNetSearchTimer();
                cancelTrackingTimer();
            } else {//不需要识别，直接唤醒
                visualWakeup();
                cancelNetSearchTimer();
            }
        }
    }

    private void handleNoTrackDetectResult(Person person) {
        Log.i(TAG, "handleNoTrackDetectResult id: " + person.getId());
        if (mCurrentPerson == null) {
            mCurrentPerson = person;
            startTrackingTimer(true);
            mNotNeedTrackedDetectTime = System.currentTimeMillis();
        } else if (!isNotNeedTrackedDetectTimeout()) {
            mCurrentPerson = person;
            startTrackingTimer(true);
        } else {
            updateSearchFaceState(SearchFaceState.WAKING);
            mCurrentPerson = person;
            mNotNeedTrackedDetectTime = INVALID_TIME;
            startNetSearchTimer();
            cancelTrackingTimer();
            visualWakeup();
        }
    }

    /**
     * 预唤醒-远程打招呼
     *
     * @param person 身体信息
     */
    private void startPreWake(Person person) {
        if (!mVisualDetectBean.isNeedPreWakeUp()) {
            Log.d(TAG, "startPreWake not need pre wakeup");
            return;
        }

        if (mIsStop) {
            Log.e(TAG, "startPreWake has stopped");
            return;
        }

        if (mNeedToPlayPreWakeInfo) {
            Log.d(TAG, "startPreWake");
            Log.e(TAG, "开始预唤醒");
            updateSearchFaceState(SearchFaceState.PRE_WAKE);
            mNeedToPlayPreWakeInfo = false;
            if (mVisualDetectListener != null) {
                mVisualDetectListener.onPreWakeup(person);
            }

            mHandler.removeMessages(CANCEL_PRE_WAKEUP_WAIT);
            mHandler.removeMessages(CANCEL_PRE_WAKEUP);
            mHandler.sendEmptyMessageDelayed(CANCEL_PRE_WAKEUP, PLAY_PRE_WAKE_LOOP_TIME);
        }
    }

    /**
     * 获取人脸照片
     */
    private void getPictureById() {
        Log.d(TAG, "getPictureById");
        if (mCurrentPerson == null) {
            Log.e(TAG, "getPictureById, current person is null, return !");
            return;
        }
        Log.e(TAG, "开始去TX1拿照");
        RobotApi.getInstance().getPictureById(mReqId, mCurrentPerson.getId(),
                MAX_PICTURE_NUM, new CommandListener() {
                    @Override
                    public void onResult(int result, String message) {
                        Log.d(TAG, "getPictureById result: " + result + ", message: " + message
                                + ", SearchFaceState: " + getSearchFaceState() + ", isStop: " + mIsStop);

                        if (mIsStop) {
                            Log.e(TAG, "getPictureById, has stopped");
                            return;
                        }

                        if (SearchFaceState.WAKING != getSearchFaceState()) {
                            Log.e(TAG, "getPictureById, not in waking and first recognize");
                            return;
                        }

                        List<String> pictureList = new ArrayList<>();
                        String status;
                        try {
                            JSONObject json = new JSONObject(message == null ? "" :
                                    message);
                            status = json.optString("status");
                            Log.d(TAG, "getPictureById status: " + status);
                            if (!TextUtils.isEmpty(status) && "failed".equals(status)) {
                                Log.e(TAG, "去tx1拿照片返回失败");
                                cancelNetSearchTimer();
                                visualWakeup();
                                return;
                            }
                            Log.e(TAG, "去tx1拿照片成功");
                            JSONArray pictures = json.optJSONArray("pictures");
                            if (!TextUtils.isEmpty(pictures.optString(0))) {
                                pictureList.add(pictures.optString(0));
                            }
                            String picture = pictures.optString(0);
                            Log.d(TAG, "getPictureById picture: " + picture);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            visualWakeup();
                            cancelNetSearchTimer();
                            VisualDetectUtils.deletePictures(pictureList);
                        }

                        if (pictureList.size() < MAX_PICTURE_NUM) {
                            Log.e(TAG, "getPictureById pictureList size < " +
                                    MAX_PICTURE_NUM);
                            VisualDetectUtils.deletePictures(pictureList);
                            return;
                        }

                        if (null != mCurrentPerson) {
                            remoteDetect(pictureList);
                        }
                    }
                });
    }

    /**
     * 去网络识别照片
     *
     * @param fileNames 获取到的照片地址
     */
    private void remoteDetect(final List<String> fileNames) {
        Log.d(TAG, "remoteDetect");
        Log.e(TAG, "开始去网络识别照片");
        RobotApi.getInstance().getPersonInfoFromNet(mReqId, String.valueOf(mCurrentPerson.getId()),
                fileNames, new CommandListener() {
                    @Override
                    public void onResult(int result, String message) {
                        Log.d(TAG, "remoteDetect onResult result: " + result + ", message: " + message
                                + ", SearchFaceState: " + getSearchFaceState() + ", isStop: " + mIsStop);
                        if (mIsStop) {
                            Log.e(TAG, "网络识别照片返回");
                            Log.e(TAG, "remoteDetect, has stopped");
                            return;
                        }

                        if (SearchFaceState.WAKING != getSearchFaceState()) {
                            Log.e(TAG, "remoteDetect, not in waking and first recognize");
                            return;
                        }
                        if (null != mCurrentPerson) {
                            if (VisualDetectUtils.isRemoteDetectSuccess(message)) {
                                mCurrentPerson = VisualDetectUtils.updatePersonData(mCurrentPerson, message);
                            }
                            buildPersonInfo(message);

                            cancelNetSearchTimer();
                            if (!TextUtils.isEmpty(mCurrentPerson.getName())) {
                                if (mVisualDetectListener != null) {
                                    visualWakeup();
                                    mVisualDetectListener.onRecognizeRetry(mCurrentPerson);
                                }
                            } else {
                                visualWakeup();
                            }
                        } else {
                            visualWakeup();
                        }
                        VisualDetectUtils.deletePictures(fileNames);
                    }
                });
    }

    /**
     * 开始唤醒并跟随人脸
     */
    private void visualWakeup() {
        Log.d(TAG, "visualWakeup SearchFaceState: " + getSearchFaceState()
                + ", CurrentPerson: " + mCurrentPerson + ", isStop: " + mIsStop);
        if (mIsStop) {
            Log.e(TAG, "visualWakeup, has stopped");
            return;
        }

        if (SearchFaceState.IDLE != getSearchFaceState()
                && SearchFaceState.WAKING != getSearchFaceState()
                && SearchFaceState.PRE_WAKE != getSearchFaceState()) {
            return;
        }

        if (mVisualDetectListener != null) {
            mVisualDetectListener.onWakeup(mCurrentPerson);//1.设置语音连续识别，UI显示已唤醒提示
        }
        if (!mVisualDetectBean.isNeedTrack()) {
            updateSearchFaceState(SearchFaceState.WAKED_ONLY);//2.设置仅唤醒
            startTrackingTimer(true);
        } else if (canBeTracked(mCurrentPerson)) {
            updateSearchFaceState(SearchFaceState.TRACKING);
            startTrackPerson();
        } else {
            Log.e(TAG, "红框唤醒");
            updateSearchFaceState(SearchFaceState.WAKED_BUT_NO_TRACK);
            startTrackingTimer(true);
        }
    }

    /**
     * 焦点跟随
     */
    private void startTrackPerson() {
        Log.d(TAG, "startTrackPerson mCurrentPerson: " + mCurrentPerson);
        if (mCurrentPerson != null) {
            Log.e(TAG, "开始焦点跟随");
            RobotApi.getInstance().startFocusFollow(mReqId, mCurrentPerson.getId(),
                    LOST_PERSON_TIMEOUT, LOST_PERSON_DISTANCE, new ActionListener() {

                        @Override
                        public void onStatusUpdate(int status, String data) {
                            Log.d(TAG, "startTrackPerson onStatusUpdate status: " + status);
                            switch (status) {
                                case Definition.STATUS_TRACK_TARGET_SUCCEED:
                                    if (mIsStop) {
                                        Log.e(TAG, "startTrackPerson, has stopped");
                                        return;
                                    }
                                    if (SearchFaceState.TRACKING == getSearchFaceState()) {
                                        if (mVisualDetectListener != null) {
                                            mVisualDetectListener.onTracking(mCurrentPerson);
                                        }
                                    }
                                    break;
                                case Definition.STATUS_GUEST_LOST:
                                case Definition.STATUS_GUEST_FARAWAY:
                                    if (mIsStop) {
                                        Log.e(TAG, "startTrackPerson, has stopped");
                                        return;
                                    }
                                    if (SearchFaceState.TRACKING == mSearchFaceState) {
                                        RobotApi.getInstance().stopFocusFollow(mReqId);
                                    }
                                    break;
                                default:

                                    break;
                            }
                        }

                        @Override
                        public void onError(int errorCode, String errorString) {
                            Log.d(TAG, "startTrackPerson onError errorCode: " + errorCode);
                            if (mIsStop) {
                                Log.e(TAG, "startTrackPerson, has stopped");
                                return;
                            }
                            if (-1 != errorCode && SearchFaceState.TRACKING == getSearchFaceState()) {
                                startTrackingTimer(false);
                                updateSearchFaceState(SearchFaceState.RE_SEARCHING);
                                if (mVisualDetectListener != null) {
                                    mVisualDetectListener.onTrackEnd();
                                }
                            }
                        }

                        @Override
                        public void onResult(int status, String responseString) {
                            Log.d(TAG, "startTrackPerson onResult status: " + status);
                            switch (status) {
                                case Definition.ACTION_RESPONSE_STOP_SUCCESS:
                                    if (mIsStop) {
                                        Log.e(TAG, "startTrackPerson, has stopped");
                                        return;
                                    }
                                    if (SearchFaceState.TRACKING == getSearchFaceState()) {
                                        startTrackingTimer(false);
                                        updateSearchFaceState(SearchFaceState.RE_SEARCHING);
                                        if (mVisualDetectListener != null) {
                                            mVisualDetectListener.onTrackEnd();
                                        }
                                    }
                                    break;
                                default:
                                    Log.d(TAG, "default");
                                    break;
                            }
                        }
                    });
        }
    }

    /**
     * 丢失焦点一段时间后，设置为空闲状态并提示关闭唤醒
     */
    private void invalid() {
        Log.i(TAG, "invalid");
        mCurrentPerson = null;
        updateSearchFaceState(SearchFaceState.IDLE);
        cancelTrackingTimer();
        cancelNetSearchTimer();
        if (mVisualDetectListener != null) {
            mVisualDetectListener.onWakeupEnd();
        }
        mNotNeedTrackedDetectTime = INVALID_TIME;
        mNeedToPlayPreWakeInfo = true;
    }

    public void continueSearchFace() {
        Log.d(TAG, "continueSearchFace");
        if (mIsStop) {
            Log.e(TAG, "continueSearchFace, has stopped");
            return;
        }
        if (SearchFaceState.RE_SEARCHING == getSearchFaceState()
                || SearchFaceState.IDLE == getSearchFaceState()) {
            startTrackingTimer(true);
        }
    }

    /**
     * 经过几秒后，还未停止，设置为空闲状态并提示唤醒
     *
     * @param isLongTimer
     */
    private void startTrackingTimer(boolean isLongTimer) {
        Log.d(TAG, "startTrackingTimer isLongTimer: " + isLongTimer);
        cancelTrackingTimer();
        long timeout;
        if (!mVisualDetectBean.isNeedAssignedPersonLostTimer()) {
            timeout = isLongTimer ? 7000 : 5000;
        } else {
            timeout = mVisualDetectBean.getAssignedPersonLostTimer();
        }
        synchronized (mLockTrackingTimer) {
            mTrackingTimer = new Timer();
            mTrackingTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.d(TAG, "startTrackingTimer timeout mIsStop: " + mIsStop);
                    if (!mIsStop) {
                        invalid();
                    }
                    cancelTrackingTimer();
                }
            }, timeout);
        }
    }


    private void cancelTrackingTimer() {
        Log.d(TAG, "cancelTrackingTimer");
        if (mTrackingTimer != null) {
            synchronized (mLockTrackingTimer) {
                if (mTrackingTimer != null) {
                    mTrackingTimer.cancel();
                    mTrackingTimer = null;
                }
            }
        }
    }

    /**
     * 2秒后，执行人脸唤醒操作
     */
    private synchronized void startNetSearchTimer() {
        cancelNetSearchTimer();
        mNetSearchTimer = new Timer();
        mNetSearchTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG, "startNetSearchTimer netSearchTimer timeout isStop: " + mIsStop);
                if (!mIsStop) {
                    visualWakeup();
                }
                cancelNetSearchTimer();
            }
        }, 2000);
    }

    /**
     * 取消NetSearchTimer定时器
     */
    private synchronized void cancelNetSearchTimer() {
        if (mNetSearchTimer != null) {
            mNetSearchTimer.cancel();
            mNetSearchTimer = null;
        }
    }

    private boolean canBeTracked(Person person) {
        if (person != null) {
            return person.getId() >= 0;
        }
        return false;
    }

    private boolean isNotNeedTrackedDetectTimeout() {
        long currentTime = System.currentTimeMillis();
        return mNotNeedTrackedDetectTime != INVALID_TIME
                && (currentTime - mNotNeedTrackedDetectTime > NOT_NEED_TRACKED_DETECT_TIMEOUT);
    }

    private boolean isTrackAvailableDistance(double distance) {
        return distance < RECOGNIZE_AVAILABLE_DISTANCE;
    }

    private boolean isRecognizeAvailableDistance(double distance) {
        return (distance > 0.0 && distance < RECOGNIZE_AVAILABLE_DISTANCE);
    }

    private synchronized SearchFaceState getSearchFaceState() {
        return mSearchFaceState;
    }

    private synchronized void updateSearchFaceState(SearchFaceState searchFaceState) {
        if (mSearchFaceState != searchFaceState) {
            Log.d(TAG, "change searchFaceState from " + mSearchFaceState + " to " +
                    searchFaceState);
            mSearchFaceState = searchFaceState;
        }
    }


    private synchronized void buildPersonInfo(String jsonMsg) {

        PersonInfo.Builder personBuilder = new PersonInfo.Builder();

        if (TextUtils.isEmpty(jsonMsg)) {
            personBuilder.registerName("");
            personBuilder.id("");
            personBuilder.role(Constants.INVALID);
        } else {
            try {
                JSONObject json = new JSONObject(jsonMsg);
                JSONObject obj = new JSONObject(json.optString("data"));
                JSONObject personObj = new JSONObject(obj.optString("people"));

                personBuilder.registerName(personObj.optString("name"));
                personBuilder.id(personObj.optString("user_id"));
                personBuilder.gender(personObj.optString("gender"));
                personBuilder.age(personObj.optInt("age"));
                personBuilder.role(personObj.optInt("role_id"));

            } catch (JSONException e) {
                Log.e(TAG, Log.getStackTraceString(e));
                personBuilder.registerName("");
                personBuilder.id("");
                personBuilder.role(Constants.INVALID);
            }
        }

        mPersonInfo = personBuilder.build();
    }

    public boolean isWaked() {
        Log.d(TAG, "isPreWaked SearchFaceState: " + getSearchFaceState());
        return getSearchFaceState() == SearchFaceState.WAKING
                || getSearchFaceState() == SearchFaceState.TRACKING
                || getSearchFaceState() == SearchFaceState.WAKED_BUT_NO_TRACK
                || getSearchFaceState() == SearchFaceState.WAKED_ONLY
                || getSearchFaceState() == SearchFaceState.RE_SEARCHING;
    }

    public boolean isPreWaked() {
        Log.d(TAG, "isPreWaked SearchFaceState: " + getSearchFaceState());
        return getSearchFaceState() == SearchFaceState.PRE_WAKE;
    }

    public boolean isPreWakedOrIdle() {
        Log.d(TAG, "isPreWakedOrIdle SearchFaceState: " + getSearchFaceState());
        return getSearchFaceState() == SearchFaceState.PRE_WAKE
                || getSearchFaceState() == SearchFaceState.IDLE;
    }

    public synchronized PersonInfo getPersonInfo() {
        return mPersonInfo;
    }

    public interface VisualDetectListener {
        void onWakeup(Person person);

        void onRecognizeRetry(Person person);

        void onTracking(Person person);

        void onTrackEnd();

        void onWakeupEnd();

        void onPreWakeup(Person person);

        void onPreWakeupEnd();

        void onStop();
    }

    public void updateWelcomeConfigData() {
        float distance = 3;
        Log.d(TAG, "updateWelcomeConfigData distance: " + distance);
        if (distance >= 2) {
            mPreWakeMaxDistance = distance;
        }
    }


}
