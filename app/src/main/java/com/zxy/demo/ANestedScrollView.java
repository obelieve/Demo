package com.zxy.demo;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

/**
 * 阻止ScrollView自动滚动（ScrollView会获取到焦点滚动）
 * 1.子View中加入：
 * android:descendantFocusability="blocksDescendants"
 * 2.重写#computeScrollDeltaToGetChildRectOnScreen(Rect) ==0
 */
public class ANestedScrollView extends ScrollView {

    public ANestedScrollView(@NonNull Context context) {
        super(context);
    }

    public ANestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
//
//        return 0;
//    }
}
