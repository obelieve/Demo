package com.news.captchalib;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.annotation.Nullable;

import java.util.Random;


public class SwipeCaptcha extends LinearLayout {

    int[] mSrcArray = new int[]{R.drawable.ic_bg_1,R.drawable.ic_bg_2,R.drawable.ic_bg_3,R.drawable.ic_bg_4,R.drawable.ic_bg_5,
            R.drawable.ic_bg_6,R.drawable.ic_bg_7,R.drawable.ic_bg_8,R.drawable.ic_bg_9};
    CaptchaImageView ivBg;
    CaptchaSeekBar sbProgress;
    ImageView refresh;
    //LinearLayout llSuccess;
    LinearLayout llFailure;

    private boolean mTouch = false;
    private SwipeCaptchaHelper.EndCallback mEndCallback;
    private Handler mHandler = new Handler();

    public void setEndCallback(SwipeCaptchaHelper.EndCallback endCallback) {
        mEndCallback = endCallback;
    }

    public SwipeCaptcha(Context context) {
        super(context);
        init();
    }

    public SwipeCaptcha(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        ivBg.setImageResource(mSrcArray[new Random().nextInt(mSrcArray.length)]);
        ivBg.setEndCallback(new SwipeCaptchaHelper.EndCallback() {
            @Override
            public void onSuccess() {
                if (mEndCallback != null) {
                    mEndCallback.onSuccess();
                }
                sbProgress.setEnabled(false);
//                llSuccess.setVisibility(VISIBLE);
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        llSuccess.setVisibility(GONE);
//                    }
//                },1000);
            }

            @Override
            public void onFailure() {
                if (mEndCallback != null) {
                    mEndCallback.onFailure();
                }
                sbProgress.setEnabled(true);
                sbProgress.setProgress(0);
                ivBg.resetBlock();
                llFailure.setVisibility(VISIBLE);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        llFailure.setVisibility(GONE);
                    }
                },1000);
            }

            @Override
            public void onMaxFailed() {
                if (mEndCallback != null) {
                    mEndCallback.onMaxFailed();
                }
                reset();
                llFailure.setVisibility(VISIBLE);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        llFailure.setVisibility(GONE);
                    }
                },1000);
            }
        });
    }

    public void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_swipe_captcha, this, true);
        //llSuccess = view.findViewById(R.id.ll_success);
        llFailure = view.findViewById(R.id.ll_failure);
        ivBg = view.findViewById(R.id.iv_bg);
        sbProgress = view.findViewById(R.id.sb_progress);
        refresh = view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mTouch) {
                    ivBg.move(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mTouch = false;
                ivBg.end();
            }
        });
    }

    public void reset() {
        sbProgress.setEnabled(true);
        sbProgress.setProgress(0);
        llFailure.setVisibility(GONE);
        ivBg.setImageResource(mSrcArray[new Random().nextInt(mSrcArray.length)]);
        ivBg.reset();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
