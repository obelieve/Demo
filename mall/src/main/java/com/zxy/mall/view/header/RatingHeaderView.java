package com.zxy.mall.view.header;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zxy.mall.R;

import butterknife.ButterKnife;

public class RatingHeaderView extends FrameLayout {

    public RatingHeaderView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public RatingHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RatingHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_rating_header, this, true);
        ButterKnife.bind(this, view);
    }
}
