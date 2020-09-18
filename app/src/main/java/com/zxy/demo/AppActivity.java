package com.zxy.demo;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.qihoo360.replugin.RePlugin;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin
 * on 2020/9/17
 */
public class AppActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv)
    public void onViewClicked() {
        RePlugin.startActivity(AppActivity.this, RePlugin.createIntent("plugin",
                "com.news.project.SplashActivity"));
    }
}
