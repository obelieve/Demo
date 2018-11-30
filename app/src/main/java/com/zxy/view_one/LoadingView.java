package com.zxy.view_one;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zxy.demo.R;

/**
 * Created by zxy on 2018/11/30 08:54.
 */

public class LoadingView extends FrameLayout
{

    public LoadingView(@NonNull Context context)
    {
        this(context, null, 0);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    int mCurrentIndex = 0;
    ImageView iv_point0, iv_point1, iv_point2, iv_point3;

    private void init()
    {
        removeAllViews();
        LayoutInflater.from(getContext()).inflate(R.layout.loading, this, true);
        iv_point0 = findViewById(R.id.iv_point0);
        iv_point1 = findViewById(R.id.iv_point1);
        iv_point2 = findViewById(R.id.iv_point2);
        iv_point3 = findViewById(R.id.iv_point3);

        postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                currentPoint(mCurrentIndex % 4);
                mCurrentIndex++;
                postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void currentPoint(int index)
    {
        switch (index)
        {
            case 0:
                iv_point0.setVisibility(VISIBLE);
                iv_point1.setVisibility(GONE);
                iv_point2.setVisibility(GONE);
                iv_point3.setVisibility(GONE);
                break;
            case 1:
                iv_point0.setVisibility(GONE);
                iv_point1.setVisibility(VISIBLE);
                iv_point2.setVisibility(GONE);
                iv_point3.setVisibility(GONE);
                break;
            case 2:
                iv_point0.setVisibility(GONE);
                iv_point1.setVisibility(GONE);
                iv_point2.setVisibility(VISIBLE);
                iv_point3.setVisibility(GONE);
                break;
            case 3:
                iv_point0.setVisibility(GONE);
                iv_point1.setVisibility(GONE);
                iv_point2.setVisibility(GONE);
                iv_point3.setVisibility(VISIBLE);
                break;
        }
    }


    private int dp2px(int dp)
    {
        return (int) (dp * density());
    }

    private float density()
    {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }
}
