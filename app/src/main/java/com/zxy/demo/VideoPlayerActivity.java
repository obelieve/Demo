package com.zxy.demo;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.qiniu.droid.niuplayer.widget.MediaController;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.qiniu.droid.niuplayer.utils.Config.DEFAULT_CACHE_DIR_NAME;
import static com.qiniu.droid.niuplayer.utils.Utils.createAVOptions;

/**
 * Created by Admin
 * on 2020/7/1
 */
public class VideoPlayerActivity extends Activity {

    @BindView(R.id.fl_video_player)
    FrameLayout flVideoPlayer;
    @BindView(R.id.video_texture_view)
    PLVideoTextureView videoView;
    @BindView(R.id.controller_stop_play)
    ImageButton controllerStopPlay;
    @BindView(R.id.controller_current_time)
    TextView controllerCurrentTime;
    @BindView(R.id.controller_progress_bar)
    SeekBar controllerProgressBar;
    @BindView(R.id.controller_end_time)
    TextView controllerEndTime;
    @BindView(R.id.full_screen_image)
    ImageButton fullScreenImage;
    @BindView(R.id.media_controller)
    MediaController mediaController;
    @BindView(R.id.cover_image)
    ImageView coverImage;
    @BindView(R.id.cover_stop_play)
    ImageButton stopPlayImage;
    @BindView(R.id.loading_view)
    LinearLayout loadingView;

    String videoPath = "https://yy.hongbaoguoji.com/live/p5u77stream.m3u8?txSecret=c0fe2173626bc39ed0b058b4ce8149ec&txTime=5efcaac2";
    String coverPath;

    OnFullScreenListener mOnFullScreenListener;
    @BindView(R.id.fl_normal)
    FrameLayout flNormal;
    @BindView(R.id.fl_full)
    FrameLayout flFull;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);
        DisplayImageOptions mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.defualt_bg)            //加载图片时的图片
                .showImageForEmptyUri(R.drawable.defualt_bg)         //没有图片资源时的默认图片
                .showImageOnFail(R.drawable.defualt_bg)              //加载失败时的图片
                .cacheInMemory(true)                               //启用内存缓存
                .cacheOnDisk(true)                                 //启用外存缓存
                .considerExifParams(true)                          //启用EXIF和JPEG图像格式
                .build();
        File cacheDir = StorageUtils.getOwnCacheDirectory(this, DEFAULT_CACHE_DIR_NAME);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default 可以自定义缓存路径
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13)
                .build();
        ImageLoader.getInstance().init(config);
        ImageLoader.getInstance().displayImage(coverPath, coverImage, mDisplayImageOptions);
        videoView.setAVOptions(createAVOptions());
        videoView.setBufferingIndicator(loadingView);
        videoView.setMediaController(mediaController);
        videoView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
        videoView.setLooping(true);
        videoView.setOnInfoListener(new PLOnInfoListener() {
            @Override
            public void onInfo(int i, int i1) {
                if (i == PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START) {
                    coverImage.setVisibility(View.GONE);
                    stopPlayImage.setVisibility(View.GONE);
                    mediaController.hide();
                }
            }
        });
        setOnFullScreenListener(new FullImpl());

        coverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopCurVideoView();
                startCurVideoView();
            }
        });

        fullScreenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnFullScreenListener != null) {
                    mOnFullScreenListener.onFullScreen(videoView, mediaController);
                }
            }
        });
    }

    public interface OnFullScreenListener {
        void onFullScreen(PLVideoTextureView videoView, MediaController mediaController);
    }

    public void setOnFullScreenListener(OnFullScreenListener listener) {
        mOnFullScreenListener = listener;
    }

    private void startCurVideoView() {
        videoView.setVideoPath(videoPath);
        videoView.start();
        loadingView.setVisibility(View.VISIBLE);
        stopPlayImage.setVisibility(View.GONE);
    }

    private void resetConfig() {
        videoView.setRotation(0);
        videoView.setMirror(false);
        videoView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
    }

    public void stopCurVideoView() {
        resetConfig();
        //videoView.stopPlayback();
        videoView.pause();
        loadingView.setVisibility(View.GONE);
        coverImage.setVisibility(View.VISIBLE);
        stopPlayImage.setVisibility(View.VISIBLE);
    }

    public class FullImpl implements OnFullScreenListener {
        @Override
        public void onFullScreen(PLVideoTextureView videoView, MediaController mediaController) {
            if (videoView == null) {
                return;
            }
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                flNormal.removeAllViews();
//                flNormal.setVisibility(View.GONE);
//                flFull.setVisibility(View.VISIBLE);
//                flFull.addView(flVideoPlayer);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                int height = (int) (getResources().getDisplayMetrics().density * 200);
//                flFull.removeAllViews();
//                flFull.setVisibility(View.GONE);
//                flNormal.setVisibility(View.VISIBLE);
//                flNormal.addView(flVideoPlayer);
            }
        }
    }
}
