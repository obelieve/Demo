package com.zxy.demo.behavior_2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.zxy.utility.LogUtil;

public class SampleHeader2Behavior extends CoordinatorLayout.Behavior<TextView> {

    private int lastFirstVisiblePosition = -1;//上一次第一个可见的RecyclerView Item的位置，用于判断下滑处理
    private boolean isProcessedScroll = false;//用于一次触摸事件只能处理一次滚动(限制嵌套滚动和RecyclerView滚动连起来处理)

    public SampleHeader2Behavior() {
    }

    public SampleHeader2Behavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull TextView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, TextView child, MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            isProcessedScroll = false;
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull TextView child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {

        if (target instanceof RecyclerView) {
            int pos = ((LinearLayoutManager) ((RecyclerView) target).getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (dy > 0) {//向上滑动
                if (child.getTranslationY() > -child.getHeight()) {
                    float finalTranslationY = child.getTranslationY() - dy;
                    if (finalTranslationY < -child.getHeight()) {
                        finalTranslationY = -child.getHeight();
                    }
                    child.setTranslationY(finalTranslationY);
                    consumed[1] = dy;
                    isProcessedScroll = true;
                } else if (isProcessedScroll && child.getTranslationY() == -child.getHeight()) {
                    //当嵌套滚动后，TextView正好隐藏时，RecyclerView不再进行滚动(只能等待下次可以再滚动)
                    consumed[1] = dy;
                }
            } else if (dy < 0) {//向下滑动
                if (child.getTranslationY() < 0 && child.getTranslationY() >= -child.getHeight()) {
                    if (pos == 0) {
                        if (lastFirstVisiblePosition == 0) {
                            if (!isProcessedScroll) {
                                float finalTranslationY = child.getTranslationY() - dy;
                                if (finalTranslationY > 0) {
                                    finalTranslationY = 0;
                                }
                                child.setTranslationY(finalTranslationY);
                            }
                        } else if (lastFirstVisiblePosition > 0) {
                            //当RecyclerView滚动后，正好到达TextView隐藏的边缘时，不再进行嵌套滚动TextView(只能等待下次可以再滚动)
                            isProcessedScroll = true;
                            consumed[1] = dy;
                        }
                    }
                }
                lastFirstVisiblePosition = pos;
                LogUtil.e("lastFirstVisiblePosition:" + lastFirstVisiblePosition);
            }

        }

    }


}
