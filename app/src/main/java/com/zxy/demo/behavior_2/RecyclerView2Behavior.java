package com.zxy.demo.behavior_2;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class RecyclerView2Behavior extends CoordinatorLayout.Behavior<RecyclerView> {

    public RecyclerView2Behavior() {
    }

    public RecyclerView2Behavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RecyclerView child, View dependency) {
        return dependency instanceof TextView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RecyclerView child, View dependency) {
        float dy = dependency.getHeight() + dependency.getY();
        child.setY(dy < 0 ? 0 : dy);
        return true;
    }
}
