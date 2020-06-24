package com.zxy.demo.mediaplayer;

import android.content.Context;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.media.TimedText;
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
public class MediaPlayerWrapper implements ICommonMediaPlayerWrapper,IMediaplayerFunc {

    private MediaPlayer mMediaPlayer;

    public MediaPlayerWrapper() {
        mMediaPlayer = new MediaPlayer();
    }

    @Override
    public void setDisplay(SurfaceHolder sh) {
        mMediaPlayer.setDisplay(sh);
    }

    @Override
    public void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mMediaPlayer.setDataSource(context, uri);
    }

    @Override
    public void setDataSource(Context context, Uri uri, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mMediaPlayer.setDataSource(context, uri, headers);
    }

    @Override
    public void setDataSource(FileDescriptor fd) throws IOException, IllegalArgumentException, IllegalStateException {
        mMediaPlayer.setDataSource(fd);
    }

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mMediaPlayer.setDataSource(path);
    }

    @Override
    public String getDataSource() {
        return null;
    }

    @Override
    public void prepareAsync() throws IllegalStateException {
        mMediaPlayer.prepareAsync();
    }

    @Override
    public void start() throws IllegalStateException {
        mMediaPlayer.start();
    }

    @Override
    public void stop() throws IllegalStateException {
        mMediaPlayer.stop();
    }

    @Override
    public void pause() throws IllegalStateException {
        mMediaPlayer.pause();
    }

    @Override
    public void setScreenOnWhilePlaying(boolean screenOn) {
        mMediaPlayer.setScreenOnWhilePlaying(screenOn);
    }

    @Override
    public int getVideoWidth() {
        return mMediaPlayer.getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        return mMediaPlayer.getVideoHeight();
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long msec) throws IllegalStateException {
        mMediaPlayer.seekTo((int) msec);
    }

    @Override
    public long getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public void release() {
        mMediaPlayer.release();
    }

    @Override
    public void reset() {
        mMediaPlayer.reset();
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        mMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @Override
    public int getAudioSessionId() {
        return mMediaPlayer.getAudioSessionId();
    }


    @Override
    public void setOnPreparedListener(OnPreparedListener listener) {
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (listener != null) {
                    listener.onPrepared(MediaPlayerWrapper.this);
                }
            }
        });
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (listener != null) {
                    listener.onCompletion(MediaPlayerWrapper.this);
                }
            }
        });
    }

    @Override
    public void setOnBufferingUpdateListener(OnBufferingUpdateListener listener) {
        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                if (listener != null) {
                    listener.onBufferingUpdate(MediaPlayerWrapper.this, percent);
                }
            }
        });
    }

    @Override
    public void setOnSeekCompleteListener(OnSeekCompleteListener listener) {
        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                if(listener!=null){
                    listener.onSeekComplete(MediaPlayerWrapper.this);
                }
            }
        });
    }

    @Override
    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener listener) {
        mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                if(listener!=null){
                    listener.onVideoSizeChanged(MediaPlayerWrapper.this,width,height,0,0);
                }
            }
        });
    }

    @Override
    public void setOnErrorListener(OnErrorListener listener) {
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if(listener!=null){
                    return listener.onError(MediaPlayerWrapper.this,what,extra);
                }
                return false;
            }
        });
    }

    @Override
    public void setOnInfoListener(OnInfoListener listener) {
        mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if(listener!=null){
                    return listener.onInfo(MediaPlayerWrapper.this,what,extra);
                }
                return false;
            }
        });
    }

    @Override
    public void setAudioStreamType(int streamtype) {
        mMediaPlayer.setAudioStreamType(streamtype);
    }

    @Override
    public void setWakeMode(Context context, int mode) {
        mMediaPlayer.setWakeMode(context,mode);
    }

    @Override
    public void setLooping(boolean looping) {
        mMediaPlayer.setLooping(looping);
    }

    @Override
    public boolean isLooping() {
        return mMediaPlayer.isLooping();
    }

    @Override
    public void setSurface(Surface surface) {
        mMediaPlayer.setSurface(surface);
    }

    @Override
    public void setOnTimedTextListener(MediaPlayer.OnTimedTextListener listener) {
        mMediaPlayer.setOnTimedTextListener(new MediaPlayer.OnTimedTextListener() {
            @Override
            public void onTimedText(MediaPlayer mp, TimedText text) {
                if(listener!=null){
                    listener.onTimedText(mp,text);
                }
            }
        });
    }

    @Override
    public MediaPlayer.TrackInfo[] getTrackInfo() {
        return mMediaPlayer.getTrackInfo();
    }

    @Override
    public void setDataSource(MediaDataSource mediaDataSource) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mMediaPlayer.setDataSource(mediaDataSource);
        }
    }
}
