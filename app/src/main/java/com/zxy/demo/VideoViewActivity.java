package com.zxy.demo;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zxy.utility.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin
 * on 2020/6/15
 */
public class VideoViewActivity extends AppCompatActivity {


    @BindView(R.id.vv_content)
    VideoView vvContent;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_pause)
    Button btnPause;
    @BindView(R.id.btn_stop)
    Button btnStop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoview);
        ButterKnife.bind(this);
        Util.requestPermission(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        vvContent.setMediaController(new MediaController(this));
        vvContent.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                LogUtil.e(mp + "");
                vvContent.start();
            }
        });
        vvContent.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                LogUtil.e(mp + "");
                return false;
            }
        });
        vvContent.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                LogUtil.e(mp + "");
            }
        });
        vvContent.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                LogUtil.e(mp + " " + what + " " + extra);
                return false;
            }
        });
        vvContent.setVideoPath(Util.getVideoPath());
    }

    @OnClick({R.id.btn_start, R.id.btn_pause, R.id.btn_stop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                vvContent.resume();
                break;
            case R.id.btn_pause:
                if (vvContent.isPlaying()) {
                    vvContent.pause();
                } else {
                    vvContent.start();
                }
                break;
            case R.id.btn_stop:
                vvContent.stopPlayback();
                break;
        }
    }
}
