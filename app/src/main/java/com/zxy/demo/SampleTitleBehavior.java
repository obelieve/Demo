package com.zxy.demo;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.zxy.utility.LogUtil;

public class SampleTitleBehavior extends CoordinatorLayout.Behavior<View> {

    private float mInitialParentY = 0;

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
        if (mInitialParentY == 0) {
            mInitialParentY = dependency.getY();
        }
        float translationY = 0;
        float curDependencyY = dependency.getY();
        int childHeight = child.getHeight();
        if (curDependencyY >= childHeight) {
            float a = (childHeight * (dependency.getY() - childHeight));
            float b = (childHeight - mInitialParentY);
            LogUtil.e(a + "," + b);
            translationY = a / b;
        }
        LogUtil.e(translationY + "," + curDependencyY + "," + mInitialParentY + "," + childHeight);
        child.setTranslationY(translationY);
        // LogUtil.e("dependency.getY()=" + dependency.getY() + " child.getHeight()=" + child.getHeight() + " deltaY:" + deltaY + " dy:" + dy + " y:" + y);
        return true;
    }
}
