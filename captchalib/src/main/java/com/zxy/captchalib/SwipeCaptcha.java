package com.zxy.captchalib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.annotation.Nullable;


public class SwipeCaptcha extends LinearLayout {

    CaptchaImageView ivBg;
    CaptchaSeekBar sbProgress;
    ImageView refresh;
    private boolean mTouch = false;
    private SwipeCaptchaHelper.EndCallback mEndCallback;

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
        ivBg.setEndCallback(new SwipeCaptchaHelper.EndCallback() {
            @Override
            public void onSuccess() {
                if(mEndCallback!=null){
                    mEndCallback.onSuccess();
                }
                sbProgress.setEnabled(false);
            }

            @Override
            public void onFailure() {
                if(mEndCallback!=null){
                    mEndCallback.onFailure();
                }
                sbProgress.setProgress(0);
                ivBg.resetBlock();
            }

            @Override
            public void onMaxFailed() {
                if(mEndCallback!=null){
                    mEndCallback.onMaxFailed();
                }
                sbProgress.setProgress(0);
                ivBg.reset();
            }
        });
    }

    public void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_swipe_captcha, this, true);
        ivBg = view.findViewById(R.id.iv_bg);
        sbProgress = view.findViewById(R.id.sb_progress);
        refresh = view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sbProgress.setEnabled(true);
                sbProgress.setProgress(0);
                ivBg.reset();
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
}
