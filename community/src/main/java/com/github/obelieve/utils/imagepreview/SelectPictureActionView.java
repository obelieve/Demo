package com.github.obelieve.utils.imagepreview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.obelieve.community.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectPictureActionView extends FrameLayout {

    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_2)
    TextView tv2;

    Callback mCallback;

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public SelectPictureActionView(@NonNull Context context) {
        this(context, null);
    }

    public SelectPictureActionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_view_select_picture_action, null);
        ButterKnife.bind(this, view);
        addView(view);
    }

    @OnClick({R.id.tv_1, R.id.tv_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_1:
                if(mCallback!=null){
                    mCallback.onSavePicture();
                }
                dismiss();
                break;
            case R.id.tv_2:
                dismiss();
                break;
        }
    }


    private void dismiss() {
        if (mCallback != null) {
            mCallback.onCancel();
        }
    }


    public interface Callback {
        void onSavePicture();
        void onCancel();
    }
}
