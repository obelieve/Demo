package com.zxy.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.zxy.im.database.AppCacheManager;
import com.zxy.im.database.UserInfo;
import com.zxy.utility.LogUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCacheManager.init(getApplicationContext());
        AppCacheManager.getInstance().getUserInfo("1");
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                UserInfo userInfo = AppCacheManager.getInstance().getUserInfo("1");
                LogUtil.e(userInfo + "");
            }
        }, 1000);
        tv = (TextView) findViewById(R.id.tv);
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("on");
                Thread.sleep(1000);
                emitter.onNext("tw");
                Thread.sleep(1000);
                int i = 0;
                int b = 100 / i;
                emitter.onNext("th");
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        tv.setText(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this, "onError()", Toast.LENGTH_SHORT).show();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Toast.makeText(MainActivity.this, "onComplete()", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
