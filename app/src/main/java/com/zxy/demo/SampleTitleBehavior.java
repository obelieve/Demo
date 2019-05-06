package com.zxy.demo;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.zxy.utility.LogUtil;

public class SampleTitleBehavior extends CoordinatorLayout.Behavior<View> {

    private float mInitDeltaY = 0;

    public SampleTitleBehavior() {
    }

    public SampleTitleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof RecyclerView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (mInitDeltaY == 0) {
            mInitDeltaY = dependency.getY() - child.getHeight();
        }
        float dy = dependency.getY() - child.getHeight();
        dy = dy < 0 ? 0 : dy;
        float translationY = -(dy / mInitDeltaY) * child.getHeight();
        child.setTranslationY(translationY);
        child.setAlpha((1 - dy / mInitDeltaY));
        // LogUtil.e("dependency.getY()=" + dependency.getY() + " child.getHeight()=" + child.getHeight() + " deltaY:" + deltaY + " dy:" + dy + " y:" + y);
        return true;
    }
}
