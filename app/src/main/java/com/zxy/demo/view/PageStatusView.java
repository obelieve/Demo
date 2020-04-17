package com.zxy.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zxy.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PageStatusView extends FrameLayout {

    @BindView(R.id.pb_loading)
    ProgressBar mPbLoading;
    @BindView(R.id.btn_refresh)
    Button mBtnRefresh;

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
        ButterKnife.bind(this,view);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @OnClick(R.id.btn_refresh)
    public void onViewClicked() {
        if(mCallback!=null){
            mCallback.onClickRefresh();
        }
    }



    public void loading(){
        setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.VISIBLE);
        mBtnRefresh.setVisibility(View.GONE);
    }

    public void failure(){
        setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.GONE);
        mBtnRefresh.setVisibility(View.VISIBLE);
    }

    public void success(){
        setVisibility(View.GONE);
        mPbLoading.setVisibility(View.GONE);
        mBtnRefresh.setVisibility(View.GONE);
    }

    public interface Callback{
        void onClickRefresh();
    }
}
