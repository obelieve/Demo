package com.zxy.demo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
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
 * <xliff:g></xliff:g> 表示数据就是原本的内容
 * 3.UI随着数据更新而更新，使用的数据类型
 * 4.绑定的classes类生成
 * 5.绑定适配器
 */
public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        final User user = new User("final first","final last");
        binding.setUser(user);
        //binding.setPresenter(new Presenter());
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                user.setFirstName("ok");
                user.setLastName("sadadasda");

            }
        },1000);
    }
}
