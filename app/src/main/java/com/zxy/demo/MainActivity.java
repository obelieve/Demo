package com.zxy.demo;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hencoder.hencoderpracticedraw1.practice.MainActivity1;
import com.hencoder.hencoderpracticedraw2.MainActivity2;
import com.hencoder.hencoderpracticedraw3.MainActivity3;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv,R.id.tv1,R.id.tv2,R.id.tv3})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv1:
                startActivity(new Intent(this, MainActivity1.class));
                break;
            case R.id.tv2:
                startActivity(new Intent(this, MainActivity2.class));
                break;
            case R.id.tv3:
                startActivity(new Intent(this, MainActivity3.class));
                break;
        }
    }
}
