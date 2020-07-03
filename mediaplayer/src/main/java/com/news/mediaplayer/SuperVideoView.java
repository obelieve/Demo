package com.news.mediaplayer;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.news.mediaplayer.utils.StatusBarUtil;
import com.news.mediaplayer.utils.Utils;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.pili.pldroid.player.widget.PLVideoView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin
 * on 2020/7/2
 */
public class SuperVideoView extends FrameLayout implements View.OnClickListener {

    private PLVideoView viewVideoPlayer;
    private ImageButton controllerStopPlay;
    private View viewLive;
    private TextView controllerCurrentTime;
    private SeekBar controllerProgressBar;
    private TextView controllerEndTime;
    private ImageButton fullScreenImage;
    private MediaController mediaController;

    private ImageButton ibBack;
    private ImageView coverImage;
    private LinearLayout loadingView;
    //切换线路
    private TextView tvLineTitle;
    private TextView tvLine1;
    private TextView tvLine2;
    private FrameLayout flSwitchLine;

    private List<String> mVideoPathList;//视频地址列表
    private String mCurVideoPath;//当前视频播放地址
    private String mCoverPath;//封面地址
    private ViewGroup mNormalScreenContainer;//小屏幕播放的容器
    private ViewGroup mFullScreenContainer;//全屏播放的容器
    private Activity mActivity;
    private OnFullScreenListener mOnFullScreenListener;
    private boolean mIsLive;

    public SuperVideoView(@NonNull Context context) {
        this(context, null, 0);


    }

    public SuperVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_super_video, this, true);
        viewVideoPlayer = view.findViewById(R.id.view_video_player);
        controllerStopPlay = view.findViewById(R.id.controller_stop_play);
        viewLive = view.findViewById(R.id.view_live);
        controllerCurrentTime = view.findViewById(R.id.controller_current_time);
        controllerProgressBar = view.findViewById(R.id.controller_progress_bar);
        controllerEndTime = view.findViewById(R.id.controller_end_time);
        fullScreenImage = view.findViewById(R.id.full_screen_image);
        mediaController = view.findViewById(R.id.media_controller);

        ibBack = findViewById(R.id.ib_back);
        coverImage = view.findViewById(R.id.cover_image);
        loadingView = view.findViewById(R.id.loading_view);

        tvLineTitle = view.findViewById(R.id.tv_line_title);
        tvLine1 = view.findViewById(R.id.tv_line1);
        tvLine2 = view.findViewById(R.id.tv_line2);
        flSwitchLine = view.findViewById(R.id.fl_switch_line);
    }

    private void init(Context context) {
        initView(context);
        mActivity = (Activity) context;
        StatusBarUtil.setStatusBarColor(mActivity, Color.BLACK);
        setOnFullScreenListener(new FullImpl());
        viewVideoPlayer.setAVOptions(Utils.createAVOptions());
        viewVideoPlayer.setBufferingIndicator(loadingView);
        viewVideoPlayer.setMediaController(mediaController);
        viewVideoPlayer.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
        viewVideoPlayer.setLooping(false);
        viewVideoPlayer.setOnErrorListener(new PLOnErrorListener() {
            @Override
            public boolean onError(int i) {
                Log.e("onError", "i:" + i);
                if (i == MEDIA_ERROR_UNKNOWN ||
                        i == ERROR_CODE_OPEN_FAILED ||
                        i == ERROR_CODE_HW_DECODE_FAILURE) {
                    flSwitchLine.setVisibility(VISIBLE);
                    loadingView.setVisibility(GONE);
                    mediaController.setVisibility(GONE);
                    return true;
                }
                return false;
            }
        });
        viewVideoPlayer.setOnInfoListener(new PLOnInfoListener() {
            @Override
            public void onInfo(int i, int i1) {
                if (i == PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START) {
                    coverImage.setVisibility(View.GONE);
                    mediaController.hide();
                }
            }
        });

        ibBack.setOnClickListener(this);
        coverImage.setOnClickListener(this);
        fullScreenImage.setOnClickListener(this);
        tvLine1.setOnClickListener(this);
        tvLine2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ibBack) {
            if (mActivity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                switchFullScreen(false);
            } else {
                mActivity.finish();
            }
        } else if (v == coverImage) {
            stopVideoView();
            startVideoView();
        } else if (v == fullScreenImage) {
            if (mOnFullScreenListener != null) {
                mOnFullScreenListener.onFullScreen(viewVideoPlayer, mediaController);
            }
        } else if (v == tvLine1) {
            flSwitchLine.setVisibility(GONE);
            mediaController.setVisibility(VISIBLE);
            if (mVideoPathList.size() > 1) {
                mCurVideoPath = mVideoPathList.get(1);
            }
            stopVideoView();
            startVideoView();
        } else if (v == tvLine2) {
            flSwitchLine.setVisibility(GONE);
            mediaController.setVisibility(VISIBLE);
            if (mVideoPathList.size() > 2) {
                mCurVideoPath = mVideoPathList.get(2);
            }
            stopVideoView();
            startVideoView();
        }
    }

    public void setLive(boolean live) {
        mIsLive = live;
        if (mIsLive) {
            controllerProgressBar.setVisibility(GONE);
            controllerEndTime.setVisibility(GONE);
            viewLive.setVisibility(VISIBLE);
        } else {
            controllerProgressBar.setVisibility(VISIBLE);
            controllerEndTime.setVisibility(VISIBLE);
            viewLive.setVisibility(GONE);
        }
    }

    public void loadData(List<String> videoPaths, String coverPath) {
        mVideoPathList = videoPaths != null ? videoPaths : new ArrayList<String>();
        mCurVideoPath = mVideoPathList.size() > 0 ? mVideoPathList.get(0) : "";
        mCoverPath = coverPath;
        Glide.with(getContext()).load(coverPath).placeholder(R.drawable.defualt_bg).error(R.drawable.defualt_bg).into(coverImage);
    }

    public void setContainer(ViewGroup normalScreenContainer, ViewGroup fullScreenContainer) {
        mNormalScreenContainer = normalScreenContainer;
        mFullScreenContainer = fullScreenContainer;
    }

    public void start() {
        coverImage.performClick();
    }

    public void setOnFullScreenListener(OnFullScreenListener listener) {
        mOnFullScreenListener = listener;
    }

    private void startVideoView() {
        viewVideoPlayer.setVideoPath(mCurVideoPath);
        viewVideoPlayer.start();
        loadingView.setVisibility(View.VISIBLE);
    }

    private void stopVideoView() {
        viewVideoPlayer.setRotation(0);
        viewVideoPlayer.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
        viewVideoPlayer.pause();
        loadingView.setVisibility(View.GONE);
        coverImage.setVisibility(View.VISIBLE);
    }

    public void release() {
        viewVideoPlayer.stopPlayback();
    }


    public interface OnFullScreenListener {
        void onFullScreen(PLVideoView videoView, MediaController mediaController);
    }

    public class FullImpl implements OnFullScreenListener {

        @Override
        public void onFullScreen(PLVideoView videoView, MediaController mediaController) {
            if (videoView == null) {
                return;
            }
            if (mActivity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                switchFullScreen(true);
            } else {
                switchFullScreen(false);
            }
        }
    }

    private void switchFullScreen(boolean fullScreen) {
        if (fullScreen) {
/*            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            flNormal.setLayoutParams(params);*/
            StatusBarUtil.setFullScreen(mActivity);
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            fullScreenImage.setImageResource(R.drawable.small_screen);
            mNormalScreenContainer.setVisibility(View.GONE);
            mNormalScreenContainer.removeView(SuperVideoView.this);
            mFullScreenContainer.setVisibility(View.VISIBLE);
            mFullScreenContainer.addView(SuperVideoView.this);
        } else {
/*            int height = (int) (getResources().getDisplayMetrics().density * 200);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            flNormal.setLayoutParams(params);*/
            StatusBarUtil.setStatusBarColor(mActivity, Color.BLACK);
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            fullScreenImage.setImageResource(R.drawable.full_screen);
            mFullScreenContainer.setVisibility(View.GONE);
            mFullScreenContainer.removeView(SuperVideoView.this);
            mNormalScreenContainer.setVisibility(View.VISIBLE);
            mNormalScreenContainer.addView(SuperVideoView.this);
        }

    }
}
