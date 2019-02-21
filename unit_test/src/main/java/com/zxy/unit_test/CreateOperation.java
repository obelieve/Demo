package com.zxy.unit_test;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;

import static com.zxy.unit_test.Log.println;

/**
 * 创建操作符：
 * 1.create  *
 * 2.defer   *
 * 3.empty/never/error
 * 4.from(Iterable、数组转为Observable)
 * 5.interval(固定间隔发送无限递增的序列)
 * 6.just 几个数据组成Observable
 * 7.range 递增范围，发送
 * 8.repeat 重复
 * 9.start
 * 10.timer
 */
public class CreateOperation {

    public static void test_create() {
        Observable.<String>create(s -> {
            s.onNext("next");
            s.onComplete();
        }).subscribe(s -> {
                    println("onNext() " + s);
                }, s -> {
                    s.printStackTrace();
                    println(s.getLocalizedMessage());
                },
                () -> println("onComplete()"));
    }

    /**
     * 只有订阅时才创建Observable,并且每个观察者获得新的Observable
     */
    public static void test_defer() {
        Observable<String> observable = Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                return Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        emitter.onNext("defer_next " + this);
                        emitter.onComplete();
                    }
                });
            }
        });
        for (int i = 0; i < 2; i++) {
            observable.subscribe(s -> {
                        println(" defer subscribe " + s);
                    }
                    , Throwable::printStackTrace, () -> {
                        println("defer onComplete()");
                    });
        }
    }
}
