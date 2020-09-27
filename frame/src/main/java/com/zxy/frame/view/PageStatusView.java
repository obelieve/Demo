package com.zxy.frame.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zxy.frame.R;

public class PageStatusView extends FrameLayout implements View.OnClickListener {

    private ProgressBar pbLoading;
    private Button btnRefresh;
    Callback mCallback;

    public PageStatusView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PageStatusView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PageStatusView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_page_status, this, true);
        pbLoading = view.findViewById(R.id.pb_loading);
        btnRefresh = view.findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(this);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onClick(View v) {
        if (v == btnRefresh) {
            if (mCallback != null) {
                mCallback.onClickRefresh();
            }
        }
    }

    public void loading() {
        setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.GONE);
    }

    public void failure() {
        setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.GONE);
        btnRefresh.setVisibility(View.VISIBLE);
    }

    public void success() {
        setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
        btnRefresh.setVisibility(View.GONE);
    }

    public interface Callback {
        void onClickRefresh();
    }

    public enum Status {
        LOADING, FAILURE, SUCCESS
    }
}
