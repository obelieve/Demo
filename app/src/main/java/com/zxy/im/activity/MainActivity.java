package com.zxy.im.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zxy.im.R;
import com.zxy.im.view.BottomTabView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
{
    @BindView(R.id.view_bottom_tab)
    BottomTabView mViewBottomTab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mViewBottomTab.setCallback(new BottomTabView.Callback()
        {
            @Override
            public void onSelected(int index, View view)
            {
                switch (index)
                {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        });
    }
}
