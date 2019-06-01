package com.zxy.demo.behavior;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

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
