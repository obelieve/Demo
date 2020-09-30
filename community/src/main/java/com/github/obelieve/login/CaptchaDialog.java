package com.github.obelieve.login;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.github.obelieve.community.R;
import com.news.captchalib.SwipeCaptcha;
import com.news.captchalib.SwipeCaptchaHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CaptchaDialog extends Dialog {

    @BindView(R.id.swipe_captcha)
    SwipeCaptcha swipeCaptcha;

    SwipeCaptchaHelper.EndCallback mEndCallback;

    public CaptchaDialog(@NonNull Context context) {
        super(context, R.style.BaseDialog);
        init();
    }

    public CaptchaDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    public void setEndCallback(SwipeCaptchaHelper.EndCallback endCallback) {
        mEndCallback = endCallback;
    }

    private void init() {
        setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_captcha, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = getContext().getResources().getDisplayMetrics().widthPixels;
        view.setLayoutParams(layoutParams);
        swipeCaptcha.setEndCallback(new SwipeCaptchaHelper.EndCallback() {
            @Override
            public void onSuccess() {
                if(mEndCallback!=null)
                    mEndCallback.onSuccess();
                dismiss();
            }

            @Override
            public void onFailure() {
                if(mEndCallback!=null)
                    mEndCallback.onFailure();
            }

            @Override
            public void onMaxFailed() {
                if(mEndCallback!=null)
                    mEndCallback.onMaxFailed();
            }
        });
    }

    @Override
    public void show() {
        swipeCaptcha.reset();
        super.show();
    }
}
