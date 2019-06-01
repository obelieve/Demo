package com.zxy.demo.behavior;

import android.content.Context;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.zxy.utility.LogUtil;

public class HomeTopBehavior extends CoordinatorLayout.Behavior<ConstraintLayout> {

    public HomeTopBehavior() {
    }

    public HomeTopBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull ConstraintLayout child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull ConstraintLayout child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        boolean isDown = true;
        if (target instanceof RecyclerView) {
            int pos = ((LinearLayoutManager) ((RecyclerView) target).getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            isDown = pos == 0;
        } else if (target instanceof SwipeRefreshLayout) {
            isDown = !((SwipeRefreshLayout)target).canChildScrollUp();
        }
        if (dy < 0) {//向下滑
            if (isDown && child.getTranslationY() < 0) {
                float consumedY = dy;
                if (child.getTranslationY() - consumedY > 0) {
                    consumedY = child.getTranslationY();
                }
                consumed[1] = (int) consumedY;
                child.setTranslationY(child.getTranslationY() - consumedY);
            }
        } else {//向上滑
            if (child.getTranslationY() > -child.getTop()) {
                float consumedY = dy;
                if (child.getTranslationY() - consumedY < -child.getTop()) {
                    consumedY = child.getTop() + child.getTranslationY();
                }
                consumed[1] = (int) consumedY;
                child.setTranslationY(child.getTranslationY() - consumedY);
            }
        }

    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull ConstraintLayout child, @NonNull View target, final float velocityX, final float velocityY) {
        boolean isFling = child.getTranslationY() == -child.getTop() && velocityY < -4096;
        if (isFling) {
            FlingAnimation animation = new FlingAnimation(child, DynamicAnimation.TRANSLATION_Y);
            animation.setStartVelocity(-velocityY)
                    .setMinValue(-child.getTop())
                    .setMaxValue(0)
                    .start();
        }
        return false;
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull ConstraintLayout child, @NonNull View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
        //当嵌套滚动后，还未到达top高度一半自动设置还原或超过高度一半后自动设置置顶
        View directChildView = getDirectChild(coordinatorLayout, target);
        if (directChildView != null) {
            if (child.getY() > 0 && child.getY() <= child.getTop() / 2) {
                child.setTranslationY(-child.getTop());
                directChildView.setY(child.getY() + child.getHeight());
            } else if (child.getY() > child.getTop() / 2 && child.getY() < child.getTop()) {
                child.setTranslationY(0);
                directChildView.setY(child.getY() + child.getHeight());
            }
        }
    }

    /**
     * 根据indirect找到parent的直接子View
     *
     * @param parent
     * @param indirect
     * @return direct view or null
     */
    private View getDirectChild(CoordinatorLayout parent, View indirect) {
        View tempParent = (indirect.getParent() instanceof View) ? (View) indirect.getParent() : null;
        View tempChild = indirect;
        View child = null;
        while (tempParent != null) {
            if (tempParent == parent) {
                child = tempChild;
                break;
            }
            tempChild = tempParent;
            tempParent = (tempParent.getParent() instanceof View) ? (View) tempParent.getParent() : null;
        }
        return child;
    }
}
