package com.news.mediaplayer;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.news.mediaplayer.utils.StatusBarUtil;
import com.news.mediaplayer.utils.Utils;
import com.news.mediaplayer.view.HorizontalItemDivider;
import com.news.mediaplayer.view.HorizontalListSelectViewImpl;
import com.news.mediaplayer.view.ListSelectView;
import com.news.mediaplayer.view.VerticalItemDivider;
import com.news.mediaplayer.view.VerticalListSelectViewImpl;
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
    private ConstraintLayout clMediaController;
    private ListSelectView lsvContent;
    private FrameLayout flMore;

    private TextView tvTitle;
    private ImageButton ibBack;
    private ImageButton ibClose;
    private ImageButton ibMore;
    private LinearLayout llCover;
    private LinearLayout loadingView;

    //切换线路
    private FrameLayout flSwitchLine;
    private LinearLayout llSwitchNoLine;
    private ConstraintLayout clSwitchHasLine;
    private ImageButton ibLineBack;
    private TextView tvLineTitle;
    private ImageButton ibLineClose;
    private ImageButton fullLineScreenImage;
    private TextView tvLineTip;
    private ListSelectView lsvHorContent;

    private List<VideoBean> mVideoBeanList;
    private VideoBean mCurVideoBean;
    private String mCoverPath;
    private ViewGroup mNormalScreenContainer;
    private ViewGroup mFullScreenContainer;
    private Activity mActivity;
    private OnFullScreenListener mOnFullScreenListener;
    private boolean mIsLive;

    private int mStatusBarHeight;
    private int mNavigationBarHeight;

    private Callback mCallback;

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
        clMediaController = view.findViewById(R.id.cl_media_controller);
        lsvContent = view.findViewById(R.id.lsv_content);
        flMore = view.findViewById(R.id.fl_more);

        tvTitle = findViewById(R.id.tv_title);
        ibBack = findViewById(R.id.ib_back);
        ibClose = findViewById(R.id.ib_close);
        ibMore = findViewById(R.id.ib_more);
        llCover = view.findViewById(R.id.ll_cover);
        loadingView = view.findViewById(R.id.loading_view);

        flSwitchLine = view.findViewById(R.id.fl_switch_line);
        llSwitchNoLine = view.findViewById(R.id.ll_switch_no_line);
        clSwitchHasLine = findViewById(R.id.cl_switch_has_line);

        ibLineBack = findViewById(R.id.ib_line_back);
        tvLineTitle = findViewById(R.id.tv_line_title);
        ibLineClose = findViewById(R.id.ib_line_close);
        fullLineScreenImage = findViewById(R.id.full_line_screen_image);
        tvLineTip = findViewById(R.id.tv_line_tip);
        lsvHorContent = findViewById(R.id.lsv_hor_content);
    }

    private void init(Context context) {
        initView(context);
        mActivity = (Activity) context;
        mStatusBarHeight = StatusBarUtil.getStatusBarHeight(mActivity);
        mNavigationBarHeight = StatusBarUtil.getNavigationBarHeight(mActivity);
        setStatusBar(false);
        setOnFullScreenListener(new FullImpl());
        viewVideoPlayer.setAVOptions(Utils.createAVOptions());
        viewVideoPlayer.setBufferingIndicator(loadingView);
        viewVideoPlayer.setMediaController(mediaController);
        viewVideoPlayer.setLooping(false);
        viewVideoPlayer.setOnErrorListener(new PLOnErrorListener() {
            @Override
            public boolean onError(int i) {
                Log.e("onError", "i:" + i);
                if (i == MEDIA_ERROR_UNKNOWN ||
                        i == ERROR_CODE_OPEN_FAILED ||
                        i == ERROR_CODE_HW_DECODE_FAILURE) {
                    flSwitchLine.setVisibility(VISIBLE);
                    if (mVideoBeanList.size() <= 1) {
                        llSwitchNoLine.setVisibility(VISIBLE);
                        clSwitchHasLine.setVisibility(GONE);
                    } else {
                        llSwitchNoLine.setVisibility(GONE);
                        clSwitchHasLine.setVisibility(VISIBLE);
                    }
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
                }
            }
        });
        viewVideoPlayer.setCoverView(llCover);
        setControllerLayout(false);
        ibBack.setOnClickListener(this);
        ibClose.setOnClickListener(this);
        ibMore.setOnClickListener(this);
        fullScreenImage.setOnClickListener(this);
        ibLineBack.setOnClickListener(this);
        ibLineClose.setOnClickListener(this);
        fullLineScreenImage.setOnClickListener(this);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onClick(View v) {
        if (v == ibBack || v == ibLineBack) {
            if (getParent() == mFullScreenContainer) {
                switchFullScreen(false);
            } else {
                mActivity.finish();
            }
        } else if (v == ibClose || v == ibLineClose) {
            if (getParent() == mFullScreenContainer) {
                switchFullScreen(false);
            }
            pauseVideoView();
            if (mCallback != null) {
                mCallback.onClose();
            }
        } else if (v == ibMore) {
            if (flMore.getVisibility() == View.VISIBLE) {
                flMore.setVisibility(View.GONE);
            } else {
                flMore.setVisibility(View.VISIBLE);
            }
        } else if (v == fullScreenImage || v == fullLineScreenImage) {
            if (mOnFullScreenListener != null) {
                mOnFullScreenListener.onFullScreen(viewVideoPlayer, mediaController);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
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

    public void loadData(String title, List<VideoBean> videoBeanList, String coverPath) {
        tvTitle.setText(title);
        mVideoBeanList = videoBeanList != null ? videoBeanList : new ArrayList<VideoBean>();
        int curVideoPosition = 0;
        if (videoBeanList != null) {
            for (int i = 0; i < videoBeanList.size(); i++) {
                if (videoBeanList.get(i).isSelected()) {
                    mCurVideoBean = videoBeanList.get(i);
                    curVideoPosition = i;
                }
            }
        }
        if (mCurVideoBean == null) {
            return;
        }
        mCoverPath = coverPath;
        viewVideoPlayer.setVideoPath(mCurVideoBean.getUrl());
        if (mCurVideoBean.getOrientation() == VideoBean.VIDEO_LANDSCAPE) {
            viewVideoPlayer.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
        } else {
            viewVideoPlayer.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_FIT_PARENT);
        }
        lsvContent.loadData(new RecyclerView.ItemDecoration[]{new VerticalItemDivider(true, 25, Color.TRANSPARENT).dividerExceptTop(true).dividerToTop(true)},
                new LinearLayoutManager(mActivity), new VerticalListSelectViewImpl(), (List) mVideoBeanList, curVideoPosition, ListSelectView.SINGLE_TYPE);
        lsvContent.setCallback(new ListSelectView.Callback() {
            @Override
            public void onSingleSelected(ListSelectView.IListSelectViewData data, int position) {
                lsvHorContent.setCurSelectedPosition(position);
                switchLineToPlayer(data, position);
            }
        });
        lsvHorContent.loadData(new RecyclerView.ItemDecoration[]{new HorizontalItemDivider(true, 10, Color.TRANSPARENT)},
                new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false), new HorizontalListSelectViewImpl(), (List) mVideoBeanList, curVideoPosition, ListSelectView.SINGLE_TYPE);
        lsvHorContent.setCallback(new ListSelectView.Callback() {
            @Override
            public void onSingleSelected(ListSelectView.IListSelectViewData data, int position) {
                lsvContent.setCurSelectedPosition(position);
                switchLineToPlayer(data, position);
            }
        });
    }

    /**
     * 切换线路 播放
     *
     * @param data
     * @param position
     */
    private void switchLineToPlayer(ListSelectView.IListSelectViewData data, int position) {
        flMore.setVisibility(GONE);
        flSwitchLine.setVisibility(GONE);
        mediaController.setVisibility(VISIBLE);
        if (data instanceof VideoBean) {
            if (getParent() == mFullScreenContainer) {
                int orientation = ((VideoBean) data).getOrientation() == VideoBean.VIDEO_LANDSCAPE ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                if (orientation != mActivity.getRequestedOrientation()) {
                    mActivity.setRequestedOrientation(orientation);
                    setControllerLayout(true);
                }
            } else {
                if (((VideoBean) data).getOrientation() == VideoBean.VIDEO_LANDSCAPE) {
                    viewVideoPlayer.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
                } else {
                    viewVideoPlayer.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_FIT_PARENT);
                }
            }
        }
        mCurVideoBean = mVideoBeanList.get(position);
        viewVideoPlayer.setVideoPath(mCurVideoBean.getUrl());
        pauseVideoView();
        startVideoView();
    }

    public void setContainer(ViewGroup normalScreenContainer, ViewGroup fullScreenContainer) {
        mNormalScreenContainer = normalScreenContainer;
        mFullScreenContainer = fullScreenContainer;
    }

    public void start() {
        if (mActivity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setStatusBar(true);
        } else {
            setStatusBar(false);
        }
        startVideoView();
    }

    public void pause() {
        pauseVideoView();
    }

    public void release() {
        viewVideoPlayer.stopPlayback();
    }

    public void setOnFullScreenListener(OnFullScreenListener listener) {
        mOnFullScreenListener = listener;
    }

    private void startVideoView() {
        viewVideoPlayer.start();
    }

    private void pauseVideoView() {
        viewVideoPlayer.pause();
        mediaController.hide();
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
            if (getParent() != mFullScreenContainer) {
                switchFullScreen(true);
            } else {
                switchFullScreen(false);
            }
        }
    }

    public void switchFullScreen(boolean fullScreen) {
        setStatusBar(fullScreen);
        if (fullScreen) {
/*            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            flNormal.setLayoutParams(params);*/
            if (mCurVideoBean.getOrientation() == VideoBean.VIDEO_LANDSCAPE) {
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                viewVideoPlayer.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_FIT_PARENT);
            } else {
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                viewVideoPlayer.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_FIT_PARENT);
            }
            fullScreenImage.setImageResource(R.drawable.small_screen);
            mNormalScreenContainer.setVisibility(View.GONE);
            mNormalScreenContainer.removeView(SuperVideoView.this);
            mFullScreenContainer.setVisibility(View.VISIBLE);
            mFullScreenContainer.addView(SuperVideoView.this);
        } else {
/*            int height = (int) (getResources().getDisplayMetrics().density * 200);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            flNormal.setLayoutParams(params);*/
            if (mCurVideoBean.getOrientation() == VideoBean.VIDEO_LANDSCAPE) {
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                viewVideoPlayer.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
            } else {
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                viewVideoPlayer.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_FIT_PARENT);
            }
            fullScreenImage.setImageResource(R.drawable.full_screen);
            mFullScreenContainer.setVisibility(View.GONE);
            mFullScreenContainer.removeView(SuperVideoView.this);
            mNormalScreenContainer.setVisibility(View.VISIBLE);
            mNormalScreenContainer.addView(SuperVideoView.this);
        }
        setControllerLayout(fullScreen);
        if (mCallback != null) {
            mCallback.onSwitchFullScreen(fullScreen);
        }
    }

    public void setControllerLayout(boolean fullScreen) {
        if (fullScreen) {
            ibMore.setVisibility(VISIBLE);
            ibClose.setVisibility(GONE);
            if (mActivity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                clMediaController.setPadding(Utils.dip2px(mActivity, 25), Utils.dip2px(mActivity, 30), Utils.dip2px(mActivity, 25), Utils.dip2px(mActivity, 35));
            } else {
                clMediaController.setPadding(Utils.dip2px(mActivity, 10), mStatusBarHeight, Utils.dip2px(mActivity, 15), mNavigationBarHeight);
            }
        } else {
            flMore.setVisibility(GONE);
            ibMore.setVisibility(GONE);
            ibClose.setVisibility(VISIBLE);
            int padding = Utils.dip2px(mActivity, 10);
            clMediaController.setPadding(padding, padding, Utils.dip2px(mActivity, 15), padding);
        }
    }

    public void setStatusBar(boolean fullScreen) {
        if (fullScreen) {
            StatusBarUtil.setFullScreen(mActivity);
        } else {
            StatusBarUtil.setStatusBarTranslucentStatus(mActivity);
        }
    }

    public interface Callback {
        void onSwitchFullScreen(boolean fullScreen);

        void onClose();
    }
}
