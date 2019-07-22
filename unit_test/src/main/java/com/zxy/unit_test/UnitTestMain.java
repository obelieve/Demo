package com.zxy.unit_test;

import org.junit.Assert;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Callable;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.zxy.unit_test.Log.println;

/**
 * https://mcxiaoke.gitbooks.io/rxdocs/content/
 */
public class UnitTestMain {

    public static void main(String[] args) {

    }

    @Test
    public void test12() throws InterruptedException {
        ObservableOnSubscribe<Integer> observableOnSubscribe = new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.println(Thread.currentThread() + "");
                emitter.onNext(1);
                emitter.onComplete();
            }
        };
        //观察者
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.println(Thread.currentThread() + "");
            }

            @Override
            public void onNext(Integer integer) {
                Log.println(Thread.currentThread() + " " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.println(Thread.currentThread() + "");
            }

            @Override
            public void onComplete() {
                Log.println(Thread.currentThread() + "");
            }
        };
        //订阅
        Scheduler scheduler1 = Schedulers.newThread();
        Scheduler scheduler2 = Schedulers.io();
        //可观察者
        Observable<Integer> observableCreate = Observable.create(observableOnSubscribe);        //ObservableCreate
        Observable<Integer> observableMap = observableCreate.map((i) -> i * i);                 //ObservableMap
        Observable<Integer> observableSubscribeOn = observableMap.subscribeOn(scheduler1);      //ObservableSubscribeOn
        Observable<Integer> observableObserveOn = observableSubscribeOn.observeOn(scheduler2);      //ObservableObserveOn
        observableObserveOn.subscribe(observer);
        Thread.sleep(1000);
    }

}
