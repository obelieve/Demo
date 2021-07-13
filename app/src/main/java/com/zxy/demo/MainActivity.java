package com.zxy.demo;

import android.os.Bundle;

import com.obelieve.frame.base.ApiBaseActivity2;
import com.zxy.demo.databinding.ActivityMainBinding;

import javax.inject.Inject;


public class MainActivity extends ApiBaseActivity2<ActivityMainBinding> {

    @Inject
    UserRepository repository;

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        repository = DaggerApplicationGraph.builder()
                .build().repository();
        mViewBinding.tv.setText(repository.toString());
    }
}
