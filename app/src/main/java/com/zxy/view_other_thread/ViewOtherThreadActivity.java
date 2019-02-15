package com.zxy.view_other_thread;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.zxy.demo.R;

/**
 * --------------
 * permission denied for this window type 需要添加：
 <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
 * 1正常情况下其他线程更新UI有异常：
 * “Only the original thread that created a view hierarchy can touch its views”
 * 1.1 Activity 中onCreate()使用其他线程，发现可以更新UI没有异常；
 * <p>
 * (Activity 的创建需要新建一个 ViewRootImpl 对象;
 * ViewRootImpl构造器会执行 mThread = Thread.currentThread();)
 * PS:
 * 原因是UI有调用requestLayout时，会调用ViewRootImpl.checkThread();
 * 如果当前线程不是初始创建所在的线程会报异常，但是如果在UI绘制之前调用就没关系。
 * 测试：
 * new Thread(new Runnable()
 * {
 *
 * @Override public void run()
 * {
 * tv.setText("AAAASDSDD");
 * }
 * }).start();
 * --------------
 * Created by zxy on 2019/2/15 11:54.
 */

public class ViewOtherThreadActivity extends Activity
{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_other_thread);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        /**
                         * 需要添加Looper :
                         java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()
                         ViewRootImpl 类内部会新建一个 ViewRootHandler 类型的 mHandler 用来处理相关消息，所以如果线程没有 Looper 是会报错的，添加 Looper
                         **/
                        Looper.prepare();
                        showWindow();
                        Looper.loop();
                    }
                }).start();
            }
        }, 2000);
    }


    private void showWindow()
    {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        params.format = PixelFormat.TRANSPARENT;
        params.gravity = Gravity.CENTER;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.window_other_thread, null);
        windowManager.addView(v, params);
    }
}
