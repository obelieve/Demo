package com.zxy.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity
{

    FrameLayout mFl;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFl = (FrameLayout) findViewById(R.id.fl_content);
        startFragment(new ScrollFragment());
    }


    public void startFragment(Fragment fragment)
    {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_content, fragment, fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();
    }
}
