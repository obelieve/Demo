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

public class View2 extends FrameLayout {

    @BindView(R.id.tv)
    TextView mTv;

    public View2(@NonNull Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.view2, this, false);
        ButterKnife.bind(this,view);
        addView(view);
        mTv.setText("View2View2View2View2View2View2");
        LogUtil.e(mTv.getText().toString());
    }

    public View2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view2, this, false);
        ButterKnife.bind(this,view);
        addView(view);
        mTv.setText("View2View2View2View2View2View2");
        LogUtil.e(mTv.getText().toString());
    }
}
