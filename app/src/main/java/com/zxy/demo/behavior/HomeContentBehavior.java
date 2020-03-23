package com.zxy.demo.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

public class HomeContentBehavior extends CoordinatorLayout.Behavior<ViewPager> {

    public HomeContentBehavior() {
    }

    public HomeContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ViewPager child, View dependency) {
        return dependency instanceof ConstraintLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ViewPager child, View dependency) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)child.getLayoutParams();
        params.height = parent.getHeight()-dependency.getHeight();
        child.setLayoutParams(params);
        child.setY(dependency.getHeight()+dependency.getY());
        return true;
    }
}
