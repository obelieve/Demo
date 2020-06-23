package com.zxy.demo.mediaplayer;

import android.content.Context;
import android.net.Uri;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;
import tv.danmaku.ijk.media.player.MediaInfo;
import tv.danmaku.ijk.media.player.misc.IMediaDataSource;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

/**
 * Created by Admin
 * on 2020/6/23
 */
public class IjkMediaPlayerWrapper implements ICommonMediaPlayerWrapper, IjkMediaPlayerFunc {

    private IjkMediaPlayer mIjkMediaPlayer;

    public IjkMediaPlayerWrapper() {
        mIjkMediaPlayer = new IjkMediaPlayer();
    }

    @Override
    public void setDisplay(SurfaceHolder sh) {
        mIjkMediaPlayer.setDisplay(sh);
    }

    @Override
    public void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mIjkMediaPlayer.setDataSource(context, uri);
    }

    @Override
    public void setDataSource(Context context, Uri uri, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mIjkMediaPlayer.setDataSource(context, uri, headers);
    }

    @Override
    public void setDataSource(FileDescriptor fd) throws IOException, IllegalArgumentException, IllegalStateException {
        mIjkMediaPlayer.setDataSource(fd);
    }

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mIjkMediaPlayer.setDataSource(path);
    }

    @Override
    public String getDataSource() {
        return mIjkMediaPlayer.getDataSource();
    }

    @Override
    public void prepareAsync() throws IllegalStateException {
        mIjkMediaPlayer.prepareAsync();
    }

    @Override
    public void start() throws IllegalStateException {
        mIjkMediaPlayer.start();
    }

    @Override
    public void stop() throws IllegalStateException {
        mIjkMediaPlayer.stop();
    }

    @Override
    public void pause() throws IllegalStateException {
        mIjkMediaPlayer.pause();
    }

    @Override
    public void setScreenOnWhilePlaying(boolean screenOn) {
        mIjkMediaPlayer.setScreenOnWhilePlaying(screenOn);
    }

    @Override
    public int getVideoWidth() {
        return mIjkMediaPlayer.getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        return mIjkMediaPlayer.getVideoHeight();
    }

    @Override
    public boolean isPlaying() {
        return mIjkMediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long msec) throws IllegalStateException {
        mIjkMediaPlayer.seekTo(msec);
    }

    @Override
    public long getCurrentPosition() {
        return mIjkMediaPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return mIjkMediaPlayer.getDuration();
    }

    @Override
    public void release() {
        mIjkMediaPlayer.release();
    }

    @Override
    public void reset() {
        mIjkMediaPlayer.reset();
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        mIjkMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @Override
    public int getAudioSessionId() {
        return mIjkMediaPlayer.getAudioSessionId();
    }

    @Override
    public void setOnPreparedListener(OnPreparedListener listener) {
        mIjkMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                if (listener != null) {
                    listener.onPrepared(IjkMediaPlayerWrapper.this);
                }
            }
        });
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {
        mIjkMediaPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                if (listener != null) {
                    listener.onCompletion(IjkMediaPlayerWrapper.this);
                }
            }
        });
    }

    @Override
    public void setOnBufferingUpdateListener(OnBufferingUpdateListener listener) {
        mIjkMediaPlayer.setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
                if (listener != null) {
                    listener.onBufferingUpdate(IjkMediaPlayerWrapper.this, i);
                }
            }
        });
    }

    @Override
    public void setOnSeekCompleteListener(OnSeekCompleteListener listener) {
        mIjkMediaPlayer.setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                if (listener != null) {
                    listener.onSeekComplete(IjkMediaPlayerWrapper.this);
                }
            }
        });
    }

    @Override
    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener listener) {
        mIjkMediaPlayer.setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
                if (listener != null) {
                    listener.onVideoSizeChanged(IjkMediaPlayerWrapper.this, i, i1, i2, i3);
                }
            }
        });
    }

    @Override
    public void setOnErrorListener(OnErrorListener listener) {
        mIjkMediaPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                if (listener != null) {
                    listener.onError(IjkMediaPlayerWrapper.this, i, i1);
                }
                return false;
            }
        });
    }

    @Override
    public void setOnInfoListener(OnInfoListener listener) {
        mIjkMediaPlayer.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                if (listener != null) {
                    listener.onInfo(IjkMediaPlayerWrapper.this, i, i1);
                }
                return false;
            }
        });
    }

    @Override
    public void setAudioStreamType(int streamtype) {
        mIjkMediaPlayer.setAudioStreamType(streamtype);
    }

    @Override
    public void setWakeMode(Context context, int mode) {
        mIjkMediaPlayer.setWakeMode(context, mode);
    }

    @Override
    public void setLooping(boolean looping) {
        mIjkMediaPlayer.setLooping(looping);
    }

    @Override
    public boolean isLooping() {
        return mIjkMediaPlayer.isLooping();
    }

    @Override
    public void setSurface(Surface surface) {
        mIjkMediaPlayer.setSurface(surface);
    }

    @Override
    public void setDataSource(IMediaDataSource mediaDataSource) {
        mIjkMediaPlayer.setDataSource(mediaDataSource);
    }

    @Override
    public void setKeepInBackground(boolean keepInBackground) {
        mIjkMediaPlayer.setKeepInBackground(keepInBackground);
    }

    @Override
    public void setOnTimedTextListener(IjkMediaPlayer.OnTimedTextListener listener) {
        mIjkMediaPlayer.setOnTimedTextListener(new IMediaPlayer.OnTimedTextListener() {
            @Override
            public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
                if(listener!=null){
                    listener.onTimedText(mIjkMediaPlayer,ijkTimedText);
                }
            }
        });
    }

    @Override
    public int getVideoSarNum() {
        return mIjkMediaPlayer.getVideoSarNum();
    }

    @Override
    public int getVideoSarDen() {
        return mIjkMediaPlayer.getVideoSarDen();
    }

    @Override
    public MediaInfo getMediaInfo() {
        return mIjkMediaPlayer.getMediaInfo();
    }

    @Override
    public ITrackInfo[] getTrackInfo() {
        return mIjkMediaPlayer.getTrackInfo();
    }
}
