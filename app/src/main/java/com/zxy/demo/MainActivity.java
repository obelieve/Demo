package com.zxy.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.zxy.utility.LogUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava是什么？Rx是一个使用可观察数据流进行异步编程的编程接口；
 * http://introtorx.com/Content/v1.0.10621.0/00_Foreword.html
 * Map操作：Observable<> ->转为另一种类型的Observable<>
 */
public class MainActivity extends AppCompatActivity {
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tv.setText("ad卡戴珊来得快洒落都是坑");
            }
        });
        LogUtil.e("猪：" + Thread.currentThread());

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Completable.create(new CompletableOnSubscribe()
                {
                    @Override
                    public void subscribe(@NonNull CompletableEmitter emitter) throws Exception
                    {

                        try
                        {
                            TimeUnit.SECONDS.sleep(1);
                            int i = 1;
                            if (i == 1)
                            {
                                throw new InterruptedException("aa");
                            }
                            emitter.onComplete();
                            LogUtil.e();
                        } catch (InterruptedException e)
                        {
                            LogUtil.e(1+"");
                            emitter.onError(e);

                        }
                    }
                }).andThen(Observable.range(1, 10))
                        .subscribe(new Consumer<Integer>()
                        {
                            @Override
                            public void accept(@NonNull Integer integer) throws Exception
                            {
                                LogUtil.e("" + integer);
                            }
                        }, new Consumer<Throwable>()
                        {
                            @Override
                            public void accept(Throwable throwable) throws Exception
                            {
                                LogUtil.e(""+throwable.getLocalizedMessage());
                            }
                        });
            }
        }, 1000);
    }

    private void testFlatMap_concatMap_concatMapEager()
    {
        //                Flowable.range(1, 10)
//                        .observeOn(Schedulers.io())
//                        .map(v ->
//                        {
//                            LogUtil.e(Thread.currentThread() + " map");
//                            return v * v;
//                        })
//                        .blockingSubscribe((v) ->
//                        {
//                            Thread.sleep(10000);
//                            LogUtil.e(Thread.currentThread() + "" + v);
//                        });
        Flowable.range(1, 10)
                .concatMapEager(v ->
                        {
                            LogUtil.e(Thread.currentThread() + " flatMap" + v);
                            return
                                    Flowable.just(v)
                                            .subscribeOn(Schedulers.computation())
                                            .map(w ->
                                            {
//                                                        if(w==6){
//                                                            Thread.sleep(3000);
//                                                        }
                                                if (w == 5)
                                                {
                                                    Thread.sleep(5000);
                                                }
                                                LogUtil.e(Thread.currentThread() + " map:" + w);
                                                return w * w;
                                            });
                        }
                )
                .blockingSubscribe((v) ->
                {
                    LogUtil.e(Thread.currentThread() + " subscribe:" + v);
                });
    }


}
