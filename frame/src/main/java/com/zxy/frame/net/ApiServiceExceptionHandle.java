package com.zxy.frame.net;

import android.app.Activity;

import com.zxy.frame.R;
import com.zxy.frame.application.BaseApplication;
import com.zxy.frame.dialog.SimpleAlertDialog;

import java.net.ConnectException;

import retrofit2.adapter.rxjava2.HttpException;


public class ApiServiceExceptionHandle {

    private static ApiExtendRespondThrowableListener sApiExtendRespondThrowableListener;

    public static void setApiExtendRespondThrowableListener(ApiExtendRespondThrowableListener listener) {
        sApiExtendRespondThrowableListener = listener;
    }

    public static ApiServiceException handleException(Activity activity, Throwable e, boolean needLogin) {
        ApiServiceException ex;
        if (e instanceof HttpException) {
            return handleHttpException((HttpException) e);
        } else if (e instanceof ApiServiceException) {
            return handleApiServiceException(activity, needLogin, (ApiServiceException) e);
        } else if (e instanceof ConnectException) {
            return handleConnectException((ConnectException)e);
        } else {
            ex = new ApiServiceException(e, ApiErrorCode.CODE_UNKNOWN, BaseApplication.getContext().getString(R.string.common_request_error_tip));
            return ex;
        }
    }

    private static ApiServiceException handleHttpException(HttpException e){
        ApiServiceException ex = new ApiServiceException(e, ApiErrorCode.CODE_HTTP_ERROR, BaseApplication.getContext().getString(R.string.common_request_error_tip));
        return ex;
    }

    private static ApiServiceException handleApiServiceException(Activity activity, boolean needLogin, ApiServiceException e) {
        switch (e.code) {
            case ApiErrorCode.CODE_DUPLICATE_NICKNAME:
            case ApiErrorCode.CODE_SENSITIVE_WORDS:
            case ApiErrorCode.CODE_TIME_INTERVAL:
            case ApiErrorCode.CODE_TOOLONG_NICKNAME:
            case ApiErrorCode.CODE_DONOT_CANCEL:
                if (e.window == 1 && activity != null && !activity.isFinishing()) {
                    new SimpleAlertDialog(activity)
                            .setSimple(true)
                            .setContent(e.getMessage())
                            .setOk("知道了").show();
                }
                e.isProcessed = true;
                break;
            default:
                if (sApiExtendRespondThrowableListener != null) {
                    sApiExtendRespondThrowableListener.defHandleException(activity, e, e.window, e.toast);
                }
                break;
        }
        return e;
    }

    private static ApiServiceException handleConnectException(ConnectException e){
        ApiServiceException  ex = new ApiServiceException(e, ApiErrorCode.CODE_NET_ERROR, BaseApplication.getContext().getString(R.string.common_network_error_tip));
        return ex;
    }

    public interface ApiExtendRespondThrowableListener {
        void defHandleException(Activity activity, ApiServiceException ex, int window, int toast);
    }
}

