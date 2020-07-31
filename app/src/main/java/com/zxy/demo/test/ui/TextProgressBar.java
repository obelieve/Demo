package com.zxy.demo.test.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zxy.demo.R;
import com.zxy.utility.SystemUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin
 * on 2020/7/29
 */
public class TextProgressBar extends FrameLayout {

    private static final int DEF_PROGRESS_COLOR = Color.parseColor("#0077FF");
    private static final int DEF_MAX_COLOR = Color.parseColor("#F0F0F0");
    public static final int LEFT_ORIENTATION = 0;
    public static final int RIGHT_ORIENTATION = 1;

    @BindView(R.id.fl_current)
    FrameLayout flCurrent;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.fl_max)
    FrameLayout flMax;

    private int mOrientation = LEFT_ORIENTATION;
    private int mMax = 100;
    private int mProgress;
    private int mWidth;
    private int mHeight;
    private int mMaxColor;
    private int mProgressColor;
    GradientDrawable mMaxGradientDrawable;
    GradientDrawable mProgressGradientDrawable;

    public TextProgressBar(@NonNull Context context) {
        this(context, null, 0);
    }

    public TextProgressBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_text_progress_bar, this, true);
        ButterKnife.bind(this, view);
        mMaxColor = DEF_MAX_COLOR;
        mProgressColor = DEF_PROGRESS_COLOR;
    }

    public void setMax(int max) {
        mMax = max;
    }

    public void setProgress(int progress) {
        mProgress = progress;
    }

    public void setProgressColor(int color) {
        mProgressColor = color;
        tvContent.setTextColor(color);
    }

    public void setText(String text) {
        tvContent.setText(text);
    }

    /**
     *
     * @param orientation {@link com.zxy.demo.test.ui.TextProgressBar#LEFT_ORIENTATION}
     *                    {@link com.zxy.demo.test.ui.TextProgressBar#RIGHT_ORIENTATION}
     */
    public void setOrientation(int orientation){
        mOrientation = orientation;
    }

    public void refreshUI(int width, int height) {
        mWidth = width;
        mHeight = height;

        mMaxGradientDrawable = genGradientDrawable(mMaxColor, mHeight / 2);
        mProgressGradientDrawable = genGradientDrawable(mProgressColor, mHeight / 2);
        flMax.setBackground(mMaxGradientDrawable);
        flCurrent.setBackground(mProgressGradientDrawable);
        LayoutParams params = (LayoutParams) flCurrent.getLayoutParams();
        params.height = mHeight;
        if (mMax > 0) {
            params.width = (int) (mProgress / (mMax * 1.0f) * mWidth);
        } else {
            params.width = 0;
        }
        if (mOrientation == LEFT_ORIENTATION) {
            if (params.width > mWidth / 2) {
                tvContent.setTextColor(Color.WHITE);
                ((LayoutParams) tvContent.getLayoutParams()).gravity = Gravity.RIGHT;
                tvContent.setPadding(0, 0, mWidth - params.width + SystemUtil.dp2px(8), 0);
            } else {
                tvContent.setTextColor(mProgressColor);
                ((LayoutParams) tvContent.getLayoutParams()).gravity = Gravity.LEFT;
                tvContent.setPadding(params.width + SystemUtil.dp2px(8), 0, 0, 0);
            }
            params.gravity = Gravity.LEFT;
        } else {
            if (params.width > mWidth / 2) {
                tvContent.setTextColor(Color.WHITE);
                ((LayoutParams) tvContent.getLayoutParams()).gravity = Gravity.LEFT;
                tvContent.setPadding(mWidth - params.width + SystemUtil.dp2px(8), 0,0,  0);
            } else {
                tvContent.setTextColor(mProgressColor);
                ((LayoutParams) tvContent.getLayoutParams()).gravity = Gravity.RIGHT;
                tvContent.setPadding(0, 0, params.width + SystemUtil.dp2px(8), 0);
            }
            params.gravity = Gravity.RIGHT;
        }
        flCurrent.setLayoutParams(params);
    }

    private GradientDrawable genGradientDrawable(int color, int radiusDp) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(SystemUtil.dp2px(radiusDp));
        drawable.setColor(color);
        return drawable;
    }
}
