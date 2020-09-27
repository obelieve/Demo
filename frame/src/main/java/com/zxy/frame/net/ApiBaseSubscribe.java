package com.zxy.frame.net;

import android.app.Activity;

import com.zxy.frame.dialog.SimpleAlertDialog;
import com.zxy.frame.utils.ToastUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class ApiBaseSubscribe<T> implements Observer<T> {

    Activity mActivity;
    boolean mNeedLogin=true;

    public ApiBaseSubscribe() {
    }

    public ApiBaseSubscribe(Activity activity) {
        this.mActivity = activity;
    }
    public ApiBaseSubscribe(Activity activity,boolean isNeedLogin) {
        this.mActivity = activity;
        this.mNeedLogin = isNeedLogin;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }


    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(T t) {
        boolean isProcessed = false;
        if (t instanceof ApiBaseResponse) {
            ApiBaseResponse response = (ApiBaseResponse) t;
            if (response.getToast() == 1) {
                ToastUtil.show(response.getMsg());
                isProcessed = true;
            }else if(response.getWindow()==1){
                if (mActivity != null && !mActivity.isFinishing()) {
                    new SimpleAlertDialog(mActivity)
                            .setSimple(true)
                            .setContent(response.getMsg())
                            .setOk("知道了").show();
                    isProcessed = true;
                }
            }
        }
        onSuccess(t, isProcessed);
    }

    @Override
    public void onError(Throwable e) {
        ApiServiceException exception = ApiServiceExceptionHandle.handleException(mActivity, e, mNeedLogin);
        onError(exception);
    }

    public abstract void onError(ApiServiceException e);

    public abstract void onSuccess(T t, boolean isProcessed);
}
