package com.zxy.demo;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{

    BottomNavigationView bnv_view;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bnv_view = findViewById(R.id.bnv_view);
    }
}
