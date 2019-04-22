package com.zxy.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

/**
 lifecycle:
 1.标准模式下 启动->关闭 Activity
 (launcher mode = standard)
 Activity:onCreate()->onStart()->onResume()->onPause()->onStop()->onDestroy()

 Fragment:onAttach()->onCreate()->onCreateView()->onViewCreated()->onActivityCreated()->onViewStateRestored()->
 onStart()->onResume()->onPause()->onStop()->onDestroyView()->onDestroy()->onDetach()

 View:<init>->onAttachedToWindow()->onMeasure(),1~n->onLayout()->onDraw()->onDetachedFromWindow()
 2.标准模式下,配置变更时(PS:翻转屏幕)
 Activity: onActivity()->onStart()->onResume()->onPause()->onSaveInstanceState()->onStop()->onDestroy()
 (重建)->onCreate()->onStart()->onRestoreInstanceState()->onResume()

 (Activity#onCreate()中,add Fragment。由于Fragment状态保存后会重新实例化，所以会有2个Fragment实例存在。)
 Fragment:
 onAttach()->onCreate()->onCreateView()->onViewCreated()->onActivityCreated()->onViewStateRestored()
 ->onStart()->Resume()->onPause()->onSaveInstanceState()->onStop()->onDestroyView()->onDestroy()->onDetach()
 (重建)->onAttach()->onCreate()->onCreateView()->onViewCreated()->onActivityCreated()->onViewState()Restored()
 ->onAttach()->onCreate()->onCreateView()->onViewCreated()->onActivityCreated()->onViewStateRestored()
 ->onStart()->onStart()->onResume()->onResume()

 (View存在在Fragment#onCreateView(...)中，所以也有2个实例存在。)
 View:
 <init>->onAttachedToWindow()->onMeasure(),1~n->onLayout()->onDraw()->onDetachedFromWindow()
 (重建)-><init> -> <init> ->onAttachedToWindow()->onAttachedToWindow()->onMeasure(),2~2n
 ->onLayout()->onLayout()->onDraw()->onDraw()

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
