package com.zxy.demo.mediaplayer;

import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.os.Build;

import androidx.annotation.RequiresApi;


/**
 * Created by Admin
 * on 2020/6/23
 */
public interface IMediaplayerFunc {
    void setOnTimedTextListener(MediaPlayer.OnTimedTextListener listener);
    MediaPlayer.TrackInfo[] getTrackInfo();
    @RequiresApi(api = Build.VERSION_CODES.M)
    void setDataSource(MediaDataSource mediaDataSource);
}
