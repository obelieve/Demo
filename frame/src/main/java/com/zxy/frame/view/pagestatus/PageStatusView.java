package com.zxy.frame.view.pagestatus;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zxy.frame.R;

import java.util.HashMap;
import java.util.Map;

public class PageStatusView extends FrameLayout {

    private Map<Integer, ViewStub> mViewStubMap;
    private Map<Integer, Boolean> mViewStubInflateMap = new HashMap<>();
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
        mViewStubMap = getPageStatusView(getContext());
        for (Map.Entry<Integer, ViewStub> entry : mViewStubMap.entrySet()) {
            addView(entry.getValue());
            mViewStubInflateMap.put(entry.getKey(), false);
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
                        if (inflated != null && !inflated) {
                            viewStubInflated(status, viewStub);
                            viewStub.setVisibility(View.VISIBLE);
                        } else {
                            viewStub.setVisibility(View.VISIBLE);
                        }
                    } else {
                        viewStub.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private void viewStubInflated(int status, ViewStub viewStub) {
        mViewStubInflateMap.put(status, true);
        if (mCallback != null) {
            View view = viewStub.inflate();
            if (view instanceof FailurePageStatusView && ((FailurePageStatusView) view).getCallback() == null) {
                ((FailurePageStatusView) view).setCallback(new FailurePageStatusView.Callback() {
                    @Override
                    public void onRefresh() {
                        mCallback.onRefresh();
                    }
                });
            }
        }
    }

    public int getStatus() {
        return mStatus;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        void onRefresh();
    }

    public interface Status {
        int LOADING = 0;
        int FAILURE = 1;
        int SUCCESS = 2;
    }

    private static Map<Integer, ViewStub> getPageStatusView(Context context) {
        Map<Integer, ViewStub> map = new HashMap<>();
        map.put(Status.LOADING, new ViewStub(context, R.layout.viewstub_loading_page_status));
        map.put(Status.FAILURE, new ViewStub(context, R.layout.viewstub_failure_page_status));
        return map;
    }
}
