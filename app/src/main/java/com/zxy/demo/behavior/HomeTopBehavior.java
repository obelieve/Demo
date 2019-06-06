package com.zxy.demo.behavior;

import android.content.Context;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.zxy.demo.view.HomeTopView;
import com.zxy.utility.LogUtil;


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
        LogUtil.e("onStartNestedScroll(...)  directTargetChild="+directTargetChild+" target="+target);
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull HomeTopView child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        LogUtil.e("onNestedPreScroll(...)  child="+child+" target="+target+" dx="+dx+" dy="+dy+" consumed="+consumed+"type="+type);
//        boolean isDown = true;
//        if (target instanceof RecyclerView) {
//            int pos = ((LinearLayoutManager) ((RecyclerView) target).getLayoutManager()).findFirstCompletelyVisibleItemPosition();
//            isDown = pos == 0;
//        } else if (target instanceof SwipeRefreshLayout) {
//            isDown = !((SwipeRefreshLayout) target).canChildScrollUp();
//        }
//        if (dy < 0) {//向下滑
//            if (isDown && child.getTranslationY() < mChildMaxTranslationY) {
//                float consumedY = dy;
//                if (child.getTranslationY() - consumedY > mChildMaxTranslationY) {
//                    consumedY = child.getTranslationY();
//                }
//                consumed[1] = (int) consumedY;
//                child.setTranslationY(child.getTranslationY() - consumedY);
//            }
//        } else {//向上滑
//            if (child.getTranslationY() > mChildMinTranslationY) {
//                float consumedY = dy;
//                if (child.getTranslationY() - consumedY < mChildMinTranslationY) {
//                    consumedY = child.getTranslationY() - mChildMinTranslationY;
//                }
//                consumed[1] = (int) consumedY;
//                child.setTranslationY(child.getTranslationY() - consumedY);
//            }
//        }

    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull HomeTopView child, @NonNull View target, final float velocityX, final float velocityY) {
        LogUtil.e("onNestedPreFling(...)  child="+child+" target="+target+" velocityX="+velocityX+" velocityX="+velocityX+" velocityY="+velocityY);
        boolean isFling = child.getTranslationY() == mChildMinTranslationY && velocityY < -4096;
        if (isFling) {
            FlingAnimation animation = new FlingAnimation(child, DynamicAnimation.TRANSLATION_Y);
            animation.setStartVelocity(-velocityY)
                    .setMinValue(mChildMinTranslationY)
                    .setMaxValue(mChildMaxTranslationY)
                    .start();
        }
        return false;
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull HomeTopView child, @NonNull View target, int type) {
        LogUtil.e("onNestedPreFling(...)  child="+child+" target="+target+" type="+type);
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
