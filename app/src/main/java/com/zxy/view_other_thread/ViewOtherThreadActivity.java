package com.zxy.view_other_thread;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

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

    TextView tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_other_thread);
        tv = findViewById(R.id.tv);
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                tv.setText("aa");
            }
        }).start();
    }

}
