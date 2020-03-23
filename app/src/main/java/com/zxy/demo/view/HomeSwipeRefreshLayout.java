package com.zxy.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.zxy.demo.R;

public class HomeSwipeRefreshLayout extends SwipeRefreshLayout {

    public HomeSwipeRefreshLayout(@NonNull Context context) {
        this(context,null);
    }

    public HomeSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setProgressViewOffset(false, HomeTopView.getRealHeight(),HomeTopView.getRealHeight()+getProgressViewEndOffset());
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.HomeSwipeRefreshLayout);
        boolean bool  = a.getBoolean(R.styleable.HomeSwipeRefreshLayout_needTopPadding,false);
        if(bool)
            setPadding(0,HomeTopView.getRealHeight(),0,0);
    }
}
