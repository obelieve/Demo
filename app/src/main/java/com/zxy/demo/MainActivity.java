package com.zxy.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

/**
 * lifecycle:
 * 1.标准模式下 启动->关闭 Activity
 * (launcher mode = standard)
 * Activity:onCreate()->onStart()->onResume()->onPause()->onStop()->onDestroy()
 *
 * Fragment:onAttach()->onCreate()->onCreateView()->onViewCreated()->onActivityCreated()->onViewStateRestored()->
 * onStart()->onResume()->onPause()->onStop()->onDestroyView()->onDestroy()->onDetach()
 *
 * View:<init>->onAttachedToWindow()->onMeasure(),1~n->onLayout()->onDraw()->onDetachedFromWindow()
 */
public class MainActivity extends BaseActivity {

    FrameLayout fl_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fl_container = findViewById(R.id.fl_container);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container, new BaseFragment())
                .commit();
    }
}
