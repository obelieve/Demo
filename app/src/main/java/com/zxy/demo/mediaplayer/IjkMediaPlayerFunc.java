package com.zxy.demo.mediaplayer;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;
import tv.danmaku.ijk.media.player.MediaInfo;
import tv.danmaku.ijk.media.player.misc.IMediaDataSource;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

/**
 * Created by Admin
 * on 2020/6/23
 */
public interface IjkMediaPlayerFunc {

    void setDataSource(IMediaDataSource mediaDataSource);
    void setKeepInBackground(boolean keepInBackground);
    void setOnTimedTextListener(IjkMediaPlayer.OnTimedTextListener listener);
    int getVideoSarNum();
    int getVideoSarDen();
    MediaInfo getMediaInfo();
    ITrackInfo[] getTrackInfo();

    interface OnTimedTextListener {
        void onTimedText(IjkMediaPlayer mp, IjkTimedText text);
    }
}
