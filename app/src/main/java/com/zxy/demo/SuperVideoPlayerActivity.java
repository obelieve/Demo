package com.zxy.demo;

import android.app.Activity;
import android.app.PictureInPictureParams;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Rational;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.news.mediaplayer.SuperVideoView;
import com.news.mediaplayer.VideoBean;

import java.util.ArrayList;
import java.util.List;

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


    List<VideoBean> mVideoBeanList = new ArrayList<>();


    {

//        mVideoBeanList.add(new VideoBean("线路-2","https://yy.hnzczs.com/live/p5u7099stream.m3u8?txSecret=0c8036a6f2b1647f572c2e9764c211bd&txTime=5f0c77e4",VideoBean.VIDEO_LANDSCAPE,true));
//        mVideoBeanList.add(new VideoBean("线路-1","https://yy.hnzczs.com/live/p5u14stream.m3u8?txSecret=3e886a5e4f94b8b3d0828b66128dca43&txTime=5f0c780b",VideoBean.VIDEO_LANDSCAPE,false));
        mVideoBeanList.add(new VideoBean("线路0", "https://yy.hnzczs.com/live/p5u14stream.m3u8?txSecret=3e886a5e4f94b8b3d0828b66128dca43&txTime=5f0c780b", VideoBean.VIDEO_LANDSCAPE, false));
        mVideoBeanList.add(new VideoBean("线路1", "https://yy.hongbaoguoji.com/live/p5u14stream.m3u8?txSecret=5042d8f1e6def3a399043cd7da8f2f10&txTime=5efdfb65", VideoBean.VIDEO_LANDSCAPE, false));
        mVideoBeanList.add(new VideoBean("线路2", "http://demo-videos.qnsdk.com/shortvideo/nike.mp4", VideoBean.VIDEO_PORTRAIT, false));
        mVideoBeanList.add(new VideoBean("线路3", "http://cctvalih5ca.v.myalicdn.com/live/cctv1_2/index.m3u8", VideoBean.VIDEO_LANDSCAPE, true));
        mVideoBeanList.add(new VideoBean("线路4", "http://image.imjihua.com/video/video_1.mp4", VideoBean.VIDEO_LANDSCAPE, false));
    }

    String coverPath = "https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2164724814,1401845036&fm=26&gp=0.jpg";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_video_player);
        ButterKnife.bind(this);
        viewSuperVideo.setLive(true);
        viewSuperVideo.loadData("标题", mVideoBeanList, coverPath);
        viewSuperVideo.setContainer(flNormal, flFull);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewSuperVideo.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewSuperVideo.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewSuperVideo.release();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            PictureInPictureParams.Builder builder = new PictureInPictureParams.Builder();
            builder.setAspectRatio(new Rational(16, 9));
            try {
                enterPictureInPictureMode(builder.build());
            } catch (Exception e) {
                /*异常情况
                java.lang.IllegalStateException: enterPictureInPictureMode: Device doesn't support picture-in-picture mode.*/
                e.printStackTrace();
            }
        }
    }


}
