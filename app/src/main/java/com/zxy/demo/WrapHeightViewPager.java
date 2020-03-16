package com.zxy.demo;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class WrapHeightViewPager extends ViewPager {
    public WrapHeightViewPager(@NonNull Context context) {
        super(context);
    }

    public WrapHeightViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
