package com.zxy.frame.net;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseSubscribe<T> implements Observer<T> {


    @Override
    public void onSubscribe(Disposable d) {

    }


    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiServiceException) {
            onError((ApiServiceException) e);
        }else {
            ApiServiceException exception = new ApiServiceException(e.getMessage(),ApiStatusCode.NOKNOWN_ERROR_CODE,"");
            e.setStackTrace(e.getStackTrace());
            onError(exception);
        }
    }

    protected abstract void onError(ApiServiceException e);
}
