package com.zxy.demo;

import android.app.Activity;
import android.app.PictureInPictureParams;
import android.os.Build;
import android.os.Bundle;
import android.util.Rational;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.news.mediaplayer.SuperVideoView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin
 * on 2020/7/2
 */
public class SuperVideoPlayerActivity extends Activity {

    @BindView(R.id.fl_normal)
    FrameLayout flNormal;
    @BindView(R.id.fl_full)
    FrameLayout flFull;
    @BindView(R.id.view_super_video)
    SuperVideoView viewSuperVideo;


    String[] videoPaths = new String[]
            {
                    "http://cctvalih5ca.v.myalicdn.com/live/cctv1_2/index.m3u8",
                    "http://demo-videos.qnsdk.com/shortvideo/nike.mp4",
                    "https://yy.hongbaoguoji.com/live/p5u14stream.m3u8?txSecret=5042d8f1e6def3a399043cd7da8f2f10&txTime=5efdfb65",
                    "https://yy.hongbaoguoji.com/live/p5u7658stream.m3u8?txSecret=21bfb04dc9a5ab7acd83e44d23ac5e36&txTime=5efdf07e",
            };
    String coverPath = "https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2164724814,1401845036&fm=26&gp=0.jpg";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_video_player);
        ButterKnife.bind(this);
        viewSuperVideo.setLive(true);
        viewSuperVideo.loadData(Arrays.asList(videoPaths), coverPath);
        viewSuperVideo.setContainer(flNormal, flFull);
        viewSuperVideo.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewSuperVideo.stop();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PictureInPictureParams.Builder builder = new PictureInPictureParams.Builder();
            builder.setAspectRatio(new Rational(16,9));
            enterPictureInPictureMode(builder.build());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewSuperVideo.release();
    }
}
