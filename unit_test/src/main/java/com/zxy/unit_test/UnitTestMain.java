package com.zxy.unit_test;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;

/**
 * https://mcxiaoke.gitbooks.io/rxdocs/content/
 */
public class UnitTestMain {

    public static void main(String[] args) {
        //CreateOperation.test_create();
        CreateOperation.test_defer();
    }

    public static void println() {
        println("");
    }

    public static void println(String s) {
        System.out.println(s);
    }

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
    public static class CreateOperation {

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

    /**
     * Observable数据执行 变换操作
     * 1.buffer  (打包发送数据，不是一个个发)
     * 2.flatMap (数据转为->Observable)
     * 3.groupBy
     * 4.map
     * 5.scan
     * 6.window
     */
    public static class MapOperation {
        public static void flatMap() {

        }
    }

    /**
     * 过滤和选择Observable发射的数据序列
     */
    public static class FilterOperation {

    }

    /**
     * 组合多个Observable
     */
    public static class CombineOperation {

    }

    /**
     * 对Observable onError()通知进行处理
     */
    public static class OnErrorNoticeHandler {

    }

    /**
     * 辅助操作
     */
    public static class AssistOperation {

    }

    /**
     * 条件和布尔操作
     */
    public static class ConditionAndBoolOperation {

    }

    /**
     * 算术和聚合操作
     */
    public static class MathOperation {

    }

    /**
     * 异步操作
     */
    public static class AsyncOperation {

    }

    /**
     * 连接操作
     */
    public static class ConnectionOperation {

    }

    /**
     * 转换操作
     */
    public static class TransferOperation {

    }

    /**
     * 阻塞操作
     */
    public static class BlockingOperation {

    }

    /**
     * 字符串操作
     */
    public static class StringOperation {

    }
}
