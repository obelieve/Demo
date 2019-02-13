package com.zxy.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zxy.utility.LogUtil;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

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
        Observable.just(1, 2, 3, 4).map(new Function<Integer, Integer>()
        {
            @Override
            public Integer apply(Integer i) throws Exception
            {
                return i * i;
            }
        }).subscribe(new Consumer<Integer>()
        {
            @Override
            public void accept(Integer i) throws Exception
            {
                LogUtil.e(String.valueOf(i));
            }
        });

    }


}
