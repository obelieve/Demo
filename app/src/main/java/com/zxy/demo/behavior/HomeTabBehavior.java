package com.zxy.demo.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.zxy.utility.LogUtil;

public class HomeTabBehavior extends CoordinatorLayout.Behavior<LinearLayout> {

    public HomeTabBehavior() {
    }

    public HomeTabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull LinearLayout child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull LinearLayout child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        if (target instanceof RecyclerView) {
            LogUtil.e("child:Y=" + child.getY());
            int pos = ((LinearLayoutManager) ((RecyclerView) target).getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (dy < 0) {//向下滑
                if (pos == 0 && child.getTranslationY() < 0) {
                    float consumedY = dy;
                    if (child.getTranslationY() - consumedY > 0) {
                        consumedY = child.getTranslationY();
                    }
                    consumed[1] = (int) consumedY;
                    child.setTranslationY(child.getTranslationY() - consumedY);
                }
            } else {//向上滑
                if (pos == 0 && child.getTranslationY() > -child.getTop()) {
                    float consumedY = dy;
                    if (child.getTranslationY() - consumedY < -child.getTop()) {
                        consumedY = child.getTop() + child.getTranslationY();
                    }
                    consumed[1] = (int) consumedY;
                    child.setTranslationY(child.getTranslationY() - consumedY);
                }
            }
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull LinearLayout child, @NonNull View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
        //当嵌套滚动后，还未到达top高度一半自动设置还原或超过高度一半后自动设置置顶
        if (child.getY() > 0 && child.getY() <= child.getTop() / 2) {
            child.setTranslationY(-child.getTop());
            target.setY(child.getY() + child.getHeight());
        } else if (child.getY() > child.getTop() / 2 && child.getY() < child.getTop()) {
            child.setTranslationY(0);
            target.setY(child.getY() + child.getHeight());
        }
    }
}
