package com.news.mediaplayer;

import com.news.mediaplayer.view.ListSelectView;

/**
 * Created by Admin
 * on 2020/7/8
 */
public class VideoBean implements ListSelectView.IListSelectViewData {

    public static final int VIDEO_LANDSCAPE = 0;
    public static final int VIDEO_PORTRAIT = 1;

    private String mName;
    private String mUrl;
    private int mWidth;
    private int mHeight;
    private int mOrientation;
    private boolean mSelected;

    public VideoBean() {
    }

    public VideoBean(String name, String url, int orientation, boolean selected) {
        mName = name;
        mUrl = url;
        mOrientation = orientation;
        mSelected = selected;
    }

    public String getUrl() {
        return mUrl;
    }

    public int getOrientation() {
        return mOrientation;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public boolean isSelected() {
        return mSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        mSelected = selected;
    }

    @Override
    public String toString() {
        return "VideoBean{" +
                "mName='" + mName + '\'' +
                ", mUrl='" + mUrl + '\'' +
                ", mWidth=" + mWidth +
                ", mHeight=" + mHeight +
                ", mOrientation=" + mOrientation +
                ", mSelected=" + mSelected +
                '}';
    }
}
