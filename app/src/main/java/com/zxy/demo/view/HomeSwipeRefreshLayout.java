package com.zxy.demo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

public class HomeSwipeRefreshLayout extends SwipeRefreshLayout {
    public HomeSwipeRefreshLayout(@NonNull Context context) {
        this(context,null);
    }

    public HomeSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        float density = context.getResources().getDisplayMetrics().density;
        setProgressViewOffset(false, (int)(density*8),(int)(density*16));//消除撤销刷新和tabLayout重叠阴影
    }
}
