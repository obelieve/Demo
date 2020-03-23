package com.zxy.demo.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.zxy.demo.view.HomeTopView;


public class HomeTopBehavior extends CoordinatorLayout.Behavior<HomeTopView> {

    private int mChildMinTranslationY = HomeTopView.getMinTranslationY();
    private int mChildMaxTranslationY = HomeTopView.getMaxTranslationY();

    public HomeTopBehavior() {
    }

    public HomeTopBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull HomeTopView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull HomeTopView child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        boolean isDown = true;
        if (target instanceof RecyclerView) {
            int pos = ((LinearLayoutManager) ((RecyclerView) target).getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            isDown = pos == 0;
        } else if (target instanceof SwipeRefreshLayout) {
            isDown = !((SwipeRefreshLayout) target).canChildScrollUp();
        }
        if (dy < 0) {//向下滑
            if (isDown && child.getTranslationY() < mChildMaxTranslationY) {
                float consumedY = dy;
                if (child.getTranslationY() - consumedY > mChildMaxTranslationY) {
                    consumedY = child.getTranslationY();
                }
                consumed[1] = (int) consumedY;
                child.setTranslationY(child.getTranslationY() - consumedY);
            }
        } else {//向上滑
            if (child.getTranslationY() > mChildMinTranslationY) {
                float consumedY = dy;
                if (child.getTranslationY() - consumedY < mChildMinTranslationY) {
                    consumedY = child.getTranslationY() - mChildMinTranslationY;
                }
                consumed[1] = (int) consumedY;
                child.setTranslationY(child.getTranslationY() - consumedY);
            }
        }

    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull HomeTopView child, @NonNull View target, final float velocityX, final float velocityY) {
        boolean isFling = child.getTranslationY() == mChildMinTranslationY && velocityY < -4096;
        if (isFling) {
            FlingAnimation animation = new FlingAnimation(child, DynamicAnimation.TRANSLATION_Y);
            animation.setStartVelocity(-mChildMinTranslationY * 5)
                    .setMinValue(mChildMinTranslationY)
                    .setMaxValue(mChildMaxTranslationY)
                    .start();
        }
        return false;
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull HomeTopView child, @NonNull View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
        //当嵌套滚动后，还未到达top高度一半自动设置还原或超过高度一半后自动设置置顶
        if (child.getY() <= mChildMinTranslationY / 2 && child.getY() > mChildMinTranslationY) {
            child.setTranslationY(mChildMinTranslationY);
        } else if (child.getTranslationY() > mChildMinTranslationY / 2 && child.getY() < mChildMaxTranslationY) {
            child.setTranslationY(mChildMaxTranslationY);
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
