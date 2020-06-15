package com.zxy.demo;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Android 平台播放音视频主要：
 * 1.AudioManager 播放 音频
 * 2.MediaPlayer：播放 音/视频
 * MediaPlayer 开发指南： https://developer.android.google.cn/guide/topics/media/mediaplayer
 * 状态：
 *
 1.存在特定状态图。
 2.某些方法，在特定状态才有效。
 Idle 空闲状态， new 时；
 Initialed状态  setDataSource()
 Preparing 状态
 Prepared 状态  prepare()/prepareAsync()
 Started start()
 Paused   pause()/seekTo()

 Stopped stop() 之后无法再调用start() 需要再次准备MediaPlayer
 PlaybackCompleted 完成状态

 End release() 释放状态
 Error  OnErrorListener#onError() 错误状态

 所指的系统资源是什么？

 Service 后台使用MediaPlayer

 系统休眠时，使用唤醒锁定状态
 CPU :setWakeMode()  PowerManager.PARTIAL_WAKE_LOCK
 WIFI: WifiLock#acquire()

 WifiLock#release()

 *
 *
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView mTv;
    @BindView(R.id.sv_content)
    SurfaceView svContent;

    MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMediaPlayer = new MediaPlayer();
//        mMediaPlayer.setWakeMode(PowerManager.);
    }

    @OnClick(R.id.tv)
    public void onViewClicked() {

    }
}
