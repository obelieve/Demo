package com.zxy.view_one;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zxy.demo.R;

/**
 * Created by zxy on 2018/11/28 09:37.
 */

public class PKProgressBar extends FrameLayout
{
    private static final int DEF_RADIUS = 10;
    private static final String WHITE_COLOR = "#FFFFFF";
    private static final String YELLOW_COLOR = "#FFFF00";

    ProgressBar mProgressBar;
    TextView mTvLeft, mTvRight;

    public PKProgressBar(@NonNull Context context)
    {
        this(context, null, 0);
    }

    public PKProgressBar(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public PKProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        removeAllViews();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_pk_progress, this, true);
        mProgressBar = view.findViewById(R.id.progress);
        mTvLeft = view.findViewById(R.id.tv_left);
        mTvRight = view.findViewById(R.id.tv_right);

        LayerDrawable layerDrawable = getLayerDrawable(dp2px(DEF_RADIUS));
        mProgressBar.setProgressDrawable(layerDrawable);
        mProgressBar.setMax(2);
        mProgressBar.setProgress(1);
    }

    public void setLeftAndRightProgress(int leftValue, int rightValue)
    {
        leftValue = Math.abs(leftValue);
        rightValue = Math.abs(rightValue);
        int maxValue = leftValue + rightValue;
        if (maxValue != 0)
        {
            mProgressBar.setMax(maxValue);
            mProgressBar.setProgress(leftValue);
        } else
        {
            mProgressBar.setMax(2);
            mProgressBar.setProgress(1);
        }
        mTvLeft.setText(String.valueOf(leftValue));
        mTvRight.setText(String.valueOf(rightValue));
    }

    @NonNull
    private LayerDrawable getLayerDrawable(int radius)
    {
        GradientDrawable whiteDrawable = new GradientDrawable();
        whiteDrawable.setColor(Color.parseColor(WHITE_COLOR));
        whiteDrawable.setCornerRadius(radius);

        GradientDrawable yellowDrawable = new GradientDrawable();
        yellowDrawable.setColor(Color.parseColor(YELLOW_COLOR));
        yellowDrawable.setCornerRadius(radius);

        ClipDrawable clipYellowDrawable = new ClipDrawable(yellowDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);

        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{whiteDrawable, clipYellowDrawable, clipYellowDrawable});
        layerDrawable.setId(0, android.R.id.background);
        layerDrawable.setId(1, android.R.id.secondaryProgress);
        layerDrawable.setId(2, android.R.id.progress);
        return layerDrawable;
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
