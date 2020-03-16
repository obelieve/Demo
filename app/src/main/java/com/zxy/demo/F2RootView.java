package com.zxy.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.zxy.utility.LogUtil;

public class F2RootView extends LinearLayout {


    public F2RootView(Context context) {
        this(context,null);
    }

    public F2RootView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LogUtil.e();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LogUtil.e("onMeasure(): w="+getWidth()+" h:"+getHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        LogUtil.e("onLayout(): w="+getWidth()+" h:"+getHeight());
    }
}
