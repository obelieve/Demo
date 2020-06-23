package com.zxy.demo.mediaplayer;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Admin
 * on 2020/6/23
 */
public interface ICommonMediaPlayerWrapper {


    void setDisplay(SurfaceHolder sh);

    void setDataSource(Context context, Uri uri)
            throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    void setDataSource(Context context, Uri uri, Map<String, String> headers)
            throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    void setDataSource(FileDescriptor fd)
            throws IOException, IllegalArgumentException, IllegalStateException;

    void setDataSource(String path)
            throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    String getDataSource();

    void prepareAsync() throws IllegalStateException;

    void start() throws IllegalStateException;

    void stop() throws IllegalStateException;

    void pause() throws IllegalStateException;

    void setScreenOnWhilePlaying(boolean screenOn);

    int getVideoWidth();

    int getVideoHeight();

    boolean isPlaying();

    void seekTo(long msec) throws IllegalStateException;

    long getCurrentPosition();

    long getDuration();

    void release();

    void reset();

    void setVolume(float leftVolume, float rightVolume);

    int getAudioSessionId();

    void setOnPreparedListener(OnPreparedListener listener);

    void setOnCompletionListener(OnCompletionListener listener);

    void setOnBufferingUpdateListener(
            OnBufferingUpdateListener listener);

    void setOnSeekCompleteListener(
            OnSeekCompleteListener listener);

    void setOnVideoSizeChangedListener(
            OnVideoSizeChangedListener listener);

    void setOnErrorListener(OnErrorListener listener);

    void setOnInfoListener(OnInfoListener listener);

    interface OnPreparedListener {
        void onPrepared(ICommonMediaPlayerWrapper mp);
    }

    interface OnCompletionListener {
        void onCompletion(ICommonMediaPlayerWrapper mp);
    }

    interface OnBufferingUpdateListener {
        void onBufferingUpdate(ICommonMediaPlayerWrapper mp, int percent);
    }

    interface OnSeekCompleteListener {
        void onSeekComplete(ICommonMediaPlayerWrapper mp);
    }

    interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(ICommonMediaPlayerWrapper mp, int width, int height,
                                int sar_num, int sar_den);
    }

    interface OnErrorListener {
        boolean onError(ICommonMediaPlayerWrapper mp, int what, int extra);
    }

    interface OnInfoListener {
        boolean onInfo(ICommonMediaPlayerWrapper mp, int what, int extra);
    }

    void setAudioStreamType(int streamtype);

    @Deprecated
    void setWakeMode(Context context, int mode);

    void setLooping(boolean looping);

    boolean isLooping();

    void setSurface(Surface surface);

}
