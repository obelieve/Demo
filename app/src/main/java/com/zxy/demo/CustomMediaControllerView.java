package com.zxy.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin
 * on 2020/6/15
 */
public class CustomMediaControllerView extends FrameLayout {

    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.ll_state)
    LinearLayout llState;
    @BindView(R.id.iv_state)
    ImageView ivState;
    @BindView(R.id.tv_restart)
    TextView tvRestart;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.tv_current_time)
    TextView tvCurrentTime;
    @BindView(R.id.sb_progress)
    SeekBar sbProgress;
    @BindView(R.id.tv_total_time)
    TextView tvTotalTime;
    @BindView(R.id.iv_switch_orientation)
    ImageView ivSwitchOrientation;
    @BindView(R.id.fl_controller)
    FrameLayout flController;

    static final int MSG_WHAT = 1;
    final int MILLISECOND = 1000;
    State mState = State.PREPARE;
    RotateAnimation mRotateAnimation;
    Callback mCallback;
    int mDuration = -1;
    int mCurDuration = -1;
    GoneRunnable mGoneRunnable;
    //处理 播放进度条显示
    Thread mThread;
    Handler mHandler;
    boolean mLoop = true;

    public CustomMediaControllerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CustomMediaControllerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomMediaControllerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mLoop = false;
    }

    @OnClick({R.id.ll_state, R.id.iv_switch_orientation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_state:
                if (mCallback != null) {
                    if (mState == State.PREPARE) {
                        mCallback.onStart();
                        setState(State.LOADING);
                    } else if (mState == State.START) {
                        mCallback.onPause();
                        setState(State.PAUSE);
                    } else if (mState == State.PAUSE) {
                        mCallback.onResume();
                        setState(State.START);
                    } else if (mState == State.STOP) {
                        mCallback.onStop();
                        setState(State.STOP);
                    } else if (mState == State.COMPLETED) {
                        mCallback.onRestart();
                        setState(State.START);
                    }
                }
                break;
            case R.id.iv_switch_orientation:
                if (mCallback != null)
                    mCallback.onSwitchOrientation();
                break;
        }
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_custom_media_controller, this, true);
        ButterKnife.bind(this, view);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showControllerView(flController.getVisibility() == GONE, true);
            }
        });
        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean touch = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mCallback != null && touch) {
                    mCurDuration = (int) (mDuration * (progress / 100.f));
                    tvCurrentTime.setText(getTime(mCurDuration, mDuration % 3600 != 0));
                    mCallback.onProgressChanged(mCurDuration);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                touch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                touch = false;
            }
        });
        mHandler = new CustomHandler(this);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void setVideoCoverImage(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        Bitmap bitmap = mmr.getFrameAtTime();
        ivBg.setImageBitmap(bitmap);
        mmr.release();
    }

    public void setDuration(int duration) {
        mDuration = duration;
        tvCurrentTime.setText(getTime(0, mDuration % 3600 != 0));
        tvTotalTime.setText(getTime(mDuration, mDuration % 3600 != 0));
    }

    private String getTime(int duration, boolean show_0_hour) {
        int totalSecond = duration / MILLISECOND;
        int hour = totalSecond / 3600;
        int minute = (totalSecond - hour * 3600) / 60;
        int second = totalSecond - hour * 3600 - minute * 60;
        StringBuilder sb = new StringBuilder();
        if (show_0_hour) {
            if (hour < 10) {
                sb.append(0);
            }
            sb.append(hour).append(":");
        }
        if (minute < 10) {
            sb.append(0);
        }
        sb.append(minute).append(":");
        if (second < 10) {
            sb.append(0);
        }
        sb.append(second);
        return sb.toString();
    }

    public void startState() {
        setState(State.START);
    }

    public void completedState() {
        sbProgress.postDelayed(new Runnable() {
            @Override
            public void run() {
                setState(State.COMPLETED);
            }
        }, 1000);
    }

    private void setState(State state) {
        mState = state;
        switch (mState) {
            case PREPARE:
                ivBg.setVisibility(VISIBLE);
                tvRestart.setVisibility(GONE);
                ivState.setImageResource(R.drawable.ic_play_normal);
                llBottom.setVisibility(GONE);
                break;
            case LOADING:
                ivBg.setVisibility(VISIBLE);
                tvRestart.setVisibility(GONE);
                ivState.setImageResource(R.drawable.ic_play_loading);
                llBottom.setVisibility(GONE);
                break;
            case START:
                ivBg.setVisibility(GONE);
                tvRestart.setVisibility(GONE);
                ivState.setImageResource(R.drawable.ic_pause_normal);
                llBottom.setVisibility(VISIBLE);
                if (mThread == null) {
                    mThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (mLoop) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (mCallback != null && mState == State.START) {
                                    int pos = mCallback.getCurrentPosition();
                                    Message msg = mHandler.obtainMessage(MSG_WHAT, pos);
                                    mHandler.sendMessage(msg);
                                }
                            }
                        }
                    });
                    mThread.start();
                }
                showControllerView(true, true);
                break;
            case PAUSE:
                ivBg.setVisibility(GONE);
                tvRestart.setVisibility(GONE);
                ivState.setImageResource(R.drawable.ic_play_normal);
                llBottom.setVisibility(VISIBLE);
                showControllerView(true, false);
                break;
            case COMPLETED:
                ivBg.setVisibility(GONE);
                ivState.setImageResource(R.drawable.ic_play_normal);
                tvRestart.setVisibility(VISIBLE);
                llBottom.setVisibility(GONE);
                sbProgress.setProgress(0);
                setDuration(mDuration);
                showControllerView(true, false);
                break;
            case STOP:
                ivBg.setVisibility(GONE);
                tvRestart.setVisibility(GONE);
                ivState.setImageResource(R.drawable.ic_play_normal);
                llBottom.setVisibility(GONE);
                break;
        }
        stateAnimation();
    }

    private void stateAnimation() {
        if (mRotateAnimation == null) {
            mRotateAnimation = new RotateAnimation(0, 360);
            mRotateAnimation.setRepeatCount(-1);
            mRotateAnimation.setDuration(1000);
        }
        mRotateAnimation.cancel();
        if (mState == State.LOADING) {
            mRotateAnimation.start();
        }
    }

    public void showControllerView(boolean visible, boolean delayGone) {
        if (mGoneRunnable == null) {
            mGoneRunnable = new GoneRunnable();
        } else {
            mHandler.removeCallbacks(mGoneRunnable);
        }
        if (visible) {
            flController.setVisibility(VISIBLE);
            if (delayGone) {
                mHandler.postDelayed(mGoneRunnable, 3600);
            }
        } else {
            flController.setVisibility(GONE);
        }
    }

    public enum State {
        PREPARE, LOADING, START, PAUSE, STOP, COMPLETED
    }

    public class GoneRunnable implements Runnable {

        @Override
        public void run() {
            flController.setVisibility(GONE);
        }
    }

    public static class CustomHandler extends Handler {

        WeakReference<CustomMediaControllerView> mViewWeakReference;

        public CustomHandler(CustomMediaControllerView view) {
            mViewWeakReference = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_WHAT) {
                CustomMediaControllerView view = mViewWeakReference.get();
                if (view != null) {
                    int curPosition = (Integer) msg.obj;
                    if (view.mDuration != 0) {
                        int progress = (int) ((curPosition / (view.mDuration * 1.0f)) * 100);
                        view.tvCurrentTime.setText(view.getTime(curPosition, view.mDuration % 3600 != 0));
                        view.sbProgress.setProgress(progress);
                    }
                }
            }
        }
    }

    public interface Callback {

        void onStart();

        void onPause();

        void onResume();

        void onStop();

        void onRestart();

        void onProgressChanged(int curDuration);

        void onSwitchOrientation();

        int getCurrentPosition();
    }
}
