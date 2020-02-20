package com.zxy.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SwipeVerificationView extends LinearLayout {

    @BindView(R.id.iv_bg)
    SwipeVerificationImageView ivBg;
    @BindView(R.id.sb_progress)
    SeekBar sbProgress;
    @BindView(R.id.btn_reset)
    Button btnReset;
    private boolean mTouch = false;

    public SwipeVerificationView(Context context) {
        super(context);
        init();
    }

    public SwipeVerificationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        ivBg.setCallback(new SwipeVerificationImageView.Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(),"成功",Toast.LENGTH_SHORT).show();
                sbProgress.setEnabled(false);
            }

            @Override
            public void onFailure() {
                Toast.makeText(getContext(),"失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_swipe_verification_code, this, true);
        ButterKnife.bind(view);
        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mTouch){
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
                ivBg.up();
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
