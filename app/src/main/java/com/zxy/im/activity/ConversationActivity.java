package com.zxy.im.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.zxy.im.R;
import com.zxy.im.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zxy on 2018/12/14 19:16.
 */

public class ConversationActivity extends BaseActivity
{

    @BindView(R.id.iv_back)
    ImageView mIvBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_conversation);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked()
    {
        finish();
    }
}
