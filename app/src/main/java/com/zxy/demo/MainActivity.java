package com.zxy.demo;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Android 平台播放音视频主要：
 * 1.AudioManager 播放 音频
 * 2.MediaPlayer：播放 音/视频
 * MediaPlayer 开发指南： https://developer.android.google.cn/guide/topics/media/mediaplayer
 * 状态：
 * <p>
 * 1.存在特定状态图。
 * 2.某些方法，在特定状态才有效。
 * Idle 空闲状态， new 时；
 * Initialed状态  setDataSource()
 * Preparing 状态
 * Prepared 状态  prepare()/prepareAsync()
 * Started start()
 * Paused   pause()/seekTo()
 * <p>
 * Stopped stop() 之后无法再调用start() 需要再次准备MediaPlayer
 * PlaybackCompleted 完成状态
 * <p>
 * End release() 释放状态
 * Error  OnErrorListener#onError() 错误状态
 * <p>
 * 所指的系统资源是什么？
 * <p>
 * Service 后台使用MediaPlayer
 * <p>
 * 系统休眠时，使用唤醒锁定状态
 * CPU :setWakeMode()  PowerManager.PARTIAL_WAKE_LOCK
 * WIFI: WifiLock#acquire()
 * <p>
 * WifiLock#release()
 * =================================
 * SurfaceView
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView mTv;
    @BindView(R.id.sv_content)
    SurfaceView svContent;
    @BindView(R.id.view_custom_media_controller)
    CustomMediaControllerView viewCustomMediaController;

    MediaPlayer mMediaPlayer;
    SurfaceCallback mSurfaceCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMediaPlayer = new MediaPlayer();
        mSurfaceCallback = new SurfaceCallback();
        svContent.getHolder().addCallback(mSurfaceCallback);
        viewCustomMediaController.setVideoCoverImage(Util.getVideoPath());
        viewCustomMediaController.setCallback(new CustomMediaControllerView.Callback() {
            @Override
            public void onStart() {
                try {
                    mMediaPlayer.setDataSource(Util.getVideoPath());
                    mMediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPause() {
                mMediaPlayer.pause();
            }

            @Override
            public void onResume() {
                mMediaPlayer.start();
            }

            @Override
            public void onStop() {
                mMediaPlayer.stop();
            }

            @Override
            public void onRestart() {
                mMediaPlayer.start();
            }

            @Override
            public void onProgressChanged(int curDuration) {
                mMediaPlayer.seekTo(curDuration);
            }

            @Override
            public void onSwitchOrientation() {

            }

            @Override
            public int getCurrentPosition() {
                return mMediaPlayer.getCurrentPosition();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        svContent.getHolder().removeCallback(mSurfaceCallback);
    }

    @OnClick(R.id.tv)
    public void onViewClicked() {

    }

    public class SurfaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mMediaPlayer.setSurface(holder.getSurface());
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    viewCustomMediaController.completedState();
                }
            });
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    viewCustomMediaController.setDuration(mMediaPlayer.getDuration());
                    viewCustomMediaController.startState();
                    mMediaPlayer.start();
                }
            });
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mMediaPlayer.stop();
        }
    }
}
