package com.zxy.demo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zxy.demo.databinding.ActivityMainBinding;

/**
 * Android Data Binding Library 使用
 * 1.配置
 * module build.gradle
 * dataBinding{
 * enabled true
 * }
 * <br/>
 * 2.在布局中表示:syntax  @{}
 * <br/>
 * tips:标签
 * <plurals></plurals> 表示复数形式
 * PS:
 * <plurals name="buy_kindle">
 * <item quantity="one">I want to buy a Kindle</item>
 * <item quantity="other">I want to buy some Kindles</item>
 * </plurals>
 * <xliff:g></xliff:g> 表示无需翻译
 * 3.UI随着数据更新而更新，使用的数据类型
 * 4.绑定的classes类生成
 * 5.绑定适配器1
 */
public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setUser(new User("ok first","ok last"));
    }
}
