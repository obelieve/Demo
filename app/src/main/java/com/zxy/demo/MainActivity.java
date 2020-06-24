package com.zxy.demo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zxy.demo.mediaplayer.ICommonMediaPlayerWrapper;
import com.zxy.demo.mediaplayer.IjkMediaPlayerWrapper;

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
    @BindView(R.id.view_custom_media_controller)
    CustomMediaControllerView viewCustomMediaController;

    ICommonMediaPlayerWrapper mMediaPlayerWrapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMediaPlayerWrapper = new IjkMediaPlayerWrapper();
        viewCustomMediaController.setMediaPlayerWrapper(mMediaPlayerWrapper);
        viewCustomMediaController.setVideoCoverImage(Util.getVideoPath());
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewCustomMediaController.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewCustomMediaController.release();
    }

    @OnClick(R.id.tv)
    public void onViewClicked() {

    }


}
