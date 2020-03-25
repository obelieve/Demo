package com.zxy.demo.test;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TestRx {

    public static void main(String[] args) {
        final Disposable[] disposable = new Disposable[1];
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                emitter.onNext("空空如也");
                emitter.onComplete();

            }
        }).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                System.out.println("doOnSubscribe");
            }
        }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("doOnComplete");
            }
        }).doOnNext(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("doOnNext");
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("doOnError");
            }
        }).doOnDispose(new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("doOnDispose:"+Thread.currentThread());
            }
        }).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribeOn(Schedulers.computation()).observeOn(Schedulers.computation()).flatMap(new Function<String, ObservableSource<?>>() {
            @Override
            public ObservableSource<String> apply(String s) throws Exception {
                return Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        emitter.onNext(s+"kajk");
                        emitter.onComplete();
                    }
                });
            }
        }).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe");
                disposable[0] =d;
            }

            @Override
            public void onNext(Object o) {
                System.out.println("onNext "+o);

            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError "+e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onNext ");
            }

        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        disposable[0].dispose();
        System.out.println("disposable="+disposable[0].isDisposed());
    }
}
