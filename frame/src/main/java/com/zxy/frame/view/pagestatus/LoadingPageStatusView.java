package com.zxy.frame.view.pagestatus;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zxy.frame.R;

/**
 * Created by Admin
 * on 2020/11/10
 */
public class LoadingPageStatusView extends FrameLayout {

    public LoadingPageStatusView(@NonNull Context context) {
        this(context,null,0);
    }

    public LoadingPageStatusView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingPageStatusView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.view_page_status_loading,this,true);
    }
}
