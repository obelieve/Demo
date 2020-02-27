package com.zxy.demo.captcha.swipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.zxy.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SwipeCaptcha extends LinearLayout {

    @BindView(R.id.iv_bg)
    CaptchaImageView ivBg;
    @BindView(R.id.sb_progress)
    CaptchaSeekBar sbProgress;
    @BindView(R.id.btn_reset)
    Button btnReset;
    private boolean mTouch = false;

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
                Toast.makeText(getContext(), "成功", Toast.LENGTH_SHORT).show();
                sbProgress.setEnabled(false);
            }

            @Override
            public void onFailure() {
                Toast.makeText(getContext(), "失败", Toast.LENGTH_SHORT).show();
                sbProgress.setProgress(0);
                ivBg.resetBlock();
            }

            @Override
            public void onMaxFailed() {
                Toast.makeText(getContext(), "达到最大失败次数", Toast.LENGTH_SHORT).show();
                sbProgress.setProgress(0);
                ivBg.reset();
            }
        });
    }

    public void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_swipe_verification_code, this, true);
        ButterKnife.bind(this, view);
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

    @OnClick(R.id.btn_reset)
    public void onViewClicked() {
        sbProgress.setEnabled(true);
        sbProgress.setProgress(0);
        ivBg.reset();
    }
}
