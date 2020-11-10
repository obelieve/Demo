package com.zxy.frame.view.pagestatus;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zxy.frame.R;


/**
 * Created by Admin
 * on 2020/11/10
 */
public class FailurePageStatusView extends FrameLayout {

    private Button btnRefresh;
    private Callback mCallback;

    public FailurePageStatusView(@NonNull Context context) {
        this(context, null, 0);
    }

    public FailurePageStatusView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FailurePageStatusView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.view_page_status_failure, this, true);
        btnRefresh = view.findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallback!=null){
                    mCallback.onRefresh();
                }
            }
        });
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public Callback getCallback() {
        return mCallback;
    }

    public interface Callback{
        void onRefresh();
    }
}
