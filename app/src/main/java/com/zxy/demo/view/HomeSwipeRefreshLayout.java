package com.zxy.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;

import com.zxy.utility.LogUtil;

public class HomeSwipeRefreshLayout extends SwipeRefreshLayout {

    public HomeSwipeRefreshLayout(@NonNull Context context) {
        this(context,null);
    }

    public HomeSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setProgressViewOffset(false, HomeTopView.getRealHeight(),HomeTopView.getRealHeight()+getProgressViewEndOffset());
    }
}
