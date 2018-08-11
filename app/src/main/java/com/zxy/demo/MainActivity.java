package com.zxy.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * android 状态栏颜色：
 * colorPrimaryDark 是状态栏指定默认的颜色值
 * android:fitsSystemWindows 用于状态栏透明时，指定布局预留空间显示状态栏，显示的颜色是当前View的背景色
 * 1.android 4.4 v19以上引入 android:windowTranslucentStatus 状态栏透明的特性
 * 2.android 5.0 v21以上引入 android:statusBarColor 设置状态栏颜色 android:navigationBarColor 导航栏颜色
 * 3.android 6.0 v23以上引入 android:windowLightStatusBar 状态栏字体颜色 Light黑色，不是Light为白色
 */
public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
