package com.zxy.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.zxy.utility.LogUtil;

import java.lang.ref.WeakReference;

/**
 lifecycle:
 1.标准模式下 启动->关闭 Activity
 (launcher mode = standard)
 Activity:onCreate()->onStart()->onResume()->onPause()->onStop()->onDestroy()
 <p>
 Fragment:onAttach()->onCreate()->onCreateView()->onViewCreated()->onActivityCreated()->onViewStateRestored()->
 onStart()->onResume()->onPause()->onStop()->onDestroyView()->onDestroy()->onDetach()
 <p>
 View:<init>->onAttachedToWindow()->onMeasure(),1~n->onLayout()->onDraw()->onDetachedFromWindow()
 2.标准模式下,配置变更时(PS:翻转屏幕)
 Activity: onActivity()->onStart()->onResume()->onPause()->onSaveInstanceState()->onStop()->onDestroy()
 (重建)->onCreate()->onStart()->onRestoreInstanceState()->onResume()
 <p>
 (Activity#onCreate()中,add Fragment。由于Fragment状态保存后会重新实例化，所以会有2个Fragment实例存在。)
 Fragment:
 onAttach()->onCreate()->onCreateView()->onViewCreated()->onActivityCreated()->onViewStateRestored()
 ->onStart()->Resume()->onPause()->onSaveInstanceState()->onStop()->onDestroyView()->onDestroy()->onDetach()
 (重建)->onAttach()->onCreate()->onCreateView()->onViewCreated()->onActivityCreated()->onViewState()Restored()
 ->onAttach()->onCreate()->onCreateView()->onViewCreated()->onActivityCreated()->onViewStateRestored()
 ->onStart()->onStart()->onResume()->onResume()
 <p>
 (View存在在Fragment#onCreateView(...)中，所以也有2个实例存在。)
 View:
 <init>->onAttachedToWindow()->onMeasure(),1~n->onLayout()->onDraw()->onDetachedFromWindow()
 (重建)-><init> -> <init> ->onAttachedToWindow()->onAttachedToWindow()->onMeasure(),2~2n
 ->onLayout()->onLayout()->onDraw()->onDraw()
 <p>
 3.当配置变更（PS:翻转屏幕）导致Activity重建时，setRetainInstance(true)的Fragment实例不销毁，存在内存中。
 （如果是系统内存不足导致Activity重建，那么Fragment实例还是会销毁，只会根据Fragment状态重新实例化）
 Fragment：{ onCreate(),onDestroy()不会调用 }
 onAttach()->onCreate()->onCreateView()->onViewCreated()->onActivityCreated()->onViewStateRestored()
 ->onStart()->onResume()->onPause()->onSaveInstanceState()->onStop()->onDestroyView()->onDetach()
 (重建)->onAttach()->onCreateView()->onViewCreated()->onActivityCreated()->onViewStateRestored()
 ->onStart()->onResume()
 <p>
 4.当Fragment#onCreateView()返回null时，
 Fragment：(没有调用，onViewCreated()、onViewStateRestored())
 onAttach()->onCreate()->onCreateView()->onActivityCreated()
 ->onStart()->onResume()->onPause()->onStop()->onDestroyView()
 ->onDestroy()->onDetach()
 <p>
 5.当FragmentTransaction#add(0,fragment,"Tag")不添加containerViewId，只使用tag标记时，
 只是Fragment的View没有插入到Activity容器中。

 Fragment(调用没变)：
 onAttach()->onCreate()->onCreateView()->onViewCreated()->onActivityCreated()
 ->onViewStateRestored()->onStart()->onResume()->onPause()->onStop()
 ->onDestroyView()->onDestroy()->onDetach()
 View:<init>

 6.当FragmentTransaction#show()和FragmentTransaction#hide()时，

 (正常)Fragment：
 onAttach()->onCreate()->onCreateView()->onViewCreated()
 ->onActivityCreated()->onViewStateRestored()->onStart()
 ->onResume()->onPause()->onStop()->onDestroyView()
 ->onDestroy()->onDetach()
 View:
 1.当FragmentTransaction#add()时，
 <init> ->onAttachedToWindow()->onMeasure(),1~n->onLayout()->onDraw()
 2.当FragmentTransaction#hide()时，
 没有相关调用
 3.当又执行FragmentTransaction#show()时，
 ->onMeasure(),1~n->onLayout()->onDraw()
 4.当FragmentTransaction#hide()时，
  没有相关调用
 5.Activity#Destroy()时，
  onDetachedFromWindow()
 */
public class MainActivity extends BaseActivity {

    FrameLayout fl_container;

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fl_container = findViewById(R.id.fl_container);
        if (savedInstanceState == null) {
            BaseFragment fragment = new BaseFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fl_container, fragment)
                    .commit();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getSupportFragmentManager()
                        .findFragmentById(R.id.fl_container);
                LogUtil.e("1. HideHandler#Callback" + fragment);
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(fragment)
                        .commit();
            }
        }, 1000);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getSupportFragmentManager()
                        .findFragmentById(R.id.fl_container);
                LogUtil.e("2.Show Handler#Callback" + fragment);
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(fragment)
                        .commit();
            }
        }, 2000);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getSupportFragmentManager()
                        .findFragmentById(R.id.fl_container);
                LogUtil.e("3.Hide Handler#Callback" + fragment);
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(fragment)
                        .commit();
            }
        }, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacksAndMessages(null);
    }
}
