package com.zxy.frame.net;

import android.app.Activity;

import com.zxy.frame.R;
import com.zxy.frame.application.BaseApplication;

import java.net.ConnectException;

import retrofit2.adapter.rxjava2.HttpException;


public class ApiServiceExceptionHandle {

    private static ApiExtendRespondThrowableListener sApiExtendRespondThrowableListener;

    public static void setApiExtendRespondThrowableListener(ApiExtendRespondThrowableListener listener) {
        sApiExtendRespondThrowableListener = listener;
    }

    public static ApiServiceException convertException(Throwable e) {
        ApiServiceException ex;
        if (e instanceof HttpException) {
            return convertHttpException((HttpException) e);
        } else if (e instanceof ApiServiceException) {
            return (ApiServiceException) e;
        } else if (e instanceof ConnectException) {
            return convertConnectException((ConnectException) e);
        } else {
            ex = new ApiServiceException(e, ApiErrorCode.CODE_UNKNOWN, BaseApplication.getContext().getString(R.string.common_request_error_tip));
            return ex;
        }
    }

    private static ApiServiceException convertHttpException(HttpException e) {
        ApiServiceException ex = new ApiServiceException(e, ApiErrorCode.CODE_HTTP_ERROR, BaseApplication.getContext().getString(R.string.common_request_error_tip));
        return ex;
    }

    public static ApiServiceException preProcessException(ApiServiceException e) {
        if (sApiExtendRespondThrowableListener != null) {
            sApiExtendRespondThrowableListener.preProcessException(e);
        }
        return e;
    }

    public static ApiServiceException handleApiServiceException(Activity activity, boolean needLogin, ApiServiceException e) {
        if (sApiExtendRespondThrowableListener != null) {
            sApiExtendRespondThrowableListener.defHandleException(activity, e, e.getWindow(), e.getToast());
        }
        return e;
    }

    private static ApiServiceException convertConnectException(ConnectException e) {
        ApiServiceException ex = new ApiServiceException(e, ApiErrorCode.CODE_NET_ERROR, BaseApplication.getContext().getString(R.string.common_network_error_tip));
        return ex;
    }

    public interface ApiExtendRespondThrowableListener {
        void preProcessException(ApiServiceException ex);
        void defHandleException(Activity activity, ApiServiceException ex, int window, int toast);
    }
}

