package com.zxy.unit_test;

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
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.zxy.unit_test.Log.println;

/**
 * https://mcxiaoke.gitbooks.io/rxdocs/content/
 */
public class UnitTestMain {

    public static void main(String[] args) throws InterruptedException {
        final Subscription[] ms = new Subscription[1];
        // 被观察者：一共需要发送500个事件，但真正开始发送事件的前提 = FlowableEmitter.requested()返回值 ≠ 0
// 观察者：每次接收事件数量 = 48（点击按钮）

        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {

                println("观察者可接收事件数量 = " + emitter.requested());
                boolean flag; //设置标记位控制

                // 被观察者一共需要发送500个事件
                for (int i = 0; i < 500; i++) {
                    flag = false;

                    // 若requested() == 0则不发送
                    while (emitter.requested() == 0) {
                        if (!flag) {
                            println("不再发送");
                            flag = true;
                        }
                    }
                    // requested() ≠ 0 才发送
                    println("发送了事件" + i + "，观察者可接收事件数量 = " + emitter.requested());
                    emitter.onNext(i);


                }
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
                .observeOn(Schedulers.newThread()) // 设置观察者在主线程中进行
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        println("onSubscribe");
                        ms[0] = s;
                        // 初始状态 = 不接收事件；通过点击按钮接收事件
                    }

                    @Override
                    public void onNext(Integer integer) {
                        println("接收到了事件" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        println("onError: " + t);
                    }

                    @Override
                    public void onComplete() {
                        println("onComplete");
                    }
                });


        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (i < 10) {
                    // 点击按钮才会接收事件 = 48 / 次
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ms[0].request(129);
                    i++;
                    // 点击按钮 则 接收48个事件
                }
            }
        }).start();
        Thread.sleep(100000);
    }

}
