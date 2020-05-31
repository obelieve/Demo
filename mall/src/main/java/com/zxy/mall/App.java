package com.zxy.mall;

import android.app.Application;

import com.zxy.frame.net.HttpUtil;
import com.zxy.frame.net.convert.CustomGsonConverterFactory;
import com.zxy.frame.utils.LogInterceptor;
import com.zxy.frame.utils.ToastUtil;
import com.zxy.mall.entity.mock.MockRepository;
import com.zxy.mall.http.HttpInterceptor;
import com.zxy.mall.http.ServiceInterface;
import com.zxy.utility.SystemUtil;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class App extends Application {

    private static ServiceInterface mServiceInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        SystemUtil.init(this);
        ToastUtil.init(this);
        MockRepository.init(this);
        mServiceInterface = HttpUtil.build().baseUrl(ServiceInterface.BASE_URL)
                .addInterceptor(new HttpInterceptor())
                .addInterceptor(new LogInterceptor())
                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .create(ServiceInterface.class);
    }

    public static ServiceInterface getServiceInterface() {
        return mServiceInterface;
    }

}
