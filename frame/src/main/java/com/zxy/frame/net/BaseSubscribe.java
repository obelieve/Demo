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
}
