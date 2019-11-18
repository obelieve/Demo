package com.zxy.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zxy.utility.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class View1 extends FrameLayout {

    @BindView(R.id.tv)
    TextView mTv;

    public View1(@NonNull Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.view1, this, false);
        ButterKnife.bind(this,view);
        addView(view);
        mTv.setText("View1View1View1View1View1View1");
        LogUtil.e(mTv.getText().toString());
    }

    public View1(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view1, this, false);
        addView(view);
        ButterKnife.bind(this,view);
        mTv.setText("View1View1View1View1View1View1");
        LogUtil.e(mTv.getText().toString());
    }
}
