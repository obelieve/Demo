package com.zxy.demo;

import android.app.Application;

import com.zxy.frame.net.HttpUtil;
import com.zxy.frame.utils.ToastUtil;

public class App extends Application {

    private static ServiceInterface mServiceInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.init(this);
        mServiceInterface = HttpUtil.build().baseUrl(ServiceInterface.BASE_URL)
                .addInterceptor(new HttpInterceptor())
                .addInterceptor(new LogInterceptor())
                .create(ServiceInterface.class);
    }

    public static ServiceInterface getServiceInterface() {
        return mServiceInterface;
    }

}
