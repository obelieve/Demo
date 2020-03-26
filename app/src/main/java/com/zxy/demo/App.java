package com.zxy.demo;

import android.app.Application;

import com.zxy.demo.http.HttpInterceptor;
import com.zxy.demo.http.ServiceInterface;
import com.zxy.frame.net.HttpUtil;
import com.zxy.frame.net.convert.CustomGsonConverterFactory;
import com.zxy.frame.utils.ToastUtil;
import com.zxy.utility.SystemUtil;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class App extends Application {

    private static ServiceInterface mServiceInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        SystemUtil.init(this);
        ToastUtil.init(this);
        mServiceInterface = HttpUtil.build().baseUrl(ServiceInterface.BASE_URL)
                .addInterceptor(new HttpInterceptor())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .create(ServiceInterface.class);
    }

    public static ServiceInterface getServiceInterface() {
        return mServiceInterface;
    }

}
