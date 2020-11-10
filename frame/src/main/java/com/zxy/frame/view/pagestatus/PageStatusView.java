package com.zxy.frame.view.pagestatus;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PageStatusView extends FrameLayout {

    private Map<Integer, ViewStub> mViewStubMap;
    private Map<Integer,Boolean> mViewStubInflateMap = new HashMap<>();
    private int mStatus;
    private Callback mCallback;

    public PageStatusView(@NonNull Context context) {
        this(context, null, 0);
    }

    public PageStatusView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageStatusView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setStatusView(Map<Integer, ViewStub> map) {
        mViewStubMap = map != null ? map : new HashMap<>();
        mViewStubInflateMap.clear();
        if (getChildCount() > 0) {
            removeAllViews();
        }
        for (Map.Entry<Integer, ViewStub> entry : mViewStubMap.entrySet()) {
            addView(entry.getValue());
            mViewStubInflateMap.put(entry.getKey(),false);
        }
    }

    public void setStatus(int status) {
        if (mViewStubMap != null) {
            mStatus = status;
            for (Map.Entry<Integer, ViewStub> entry : mViewStubMap.entrySet()) {
                ViewStub viewStub = entry.getValue();
                if (viewStub != null) {
                    if (status == entry.getKey()) {
                        Boolean inflated = mViewStubInflateMap.get(status);
                        if(inflated!=null&&!inflated){
                            mViewStubInflateMap.put(status,true);
                            if(mCallback!=null){
                                mCallback.onInflated(viewStub.inflate());
                            }
                            viewStub.setVisibility(View.VISIBLE);
                        }else{
                            viewStub.setVisibility(View.VISIBLE);
                        }
                    } else {
                        viewStub.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    public int getStatus() {
        return mStatus;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback{
        void onInflated(View view);
    }
}
