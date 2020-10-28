package com.zxy.demo;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zxy.demo.http.HttpInterceptor;
import com.zxy.demo.http.ServiceInterface;
import com.zxy.frame.application.BaseApplication;
import com.zxy.frame.net.ApiService;
import com.zxy.frame.net.HttpUtil;
import com.zxy.frame.net.convert.ApiCustomGsonConverterFactory;
import com.zxy.frame.net.download.DownloadInterface;
import com.zxy.frame.utils.ToastUtil;
import com.zxy.frame.utils.log.LogInterceptor;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class App extends BaseApplication {

    private static ServiceInterface mServiceInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.init(this);
        initHttp();
    }

    private void initHttp() {
        HttpUtil httpUtil = HttpUtil.build();
        mServiceInterface = httpUtil.baseUrl(ServiceInterface.BASE_URL)
                .addInterceptor(new HttpInterceptor())
                .addInterceptor(new LogInterceptor())
                .addConverterFactory(ApiCustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .create(ServiceInterface.class);
        ApiService.setDownloadInterface(new DownloadInterface() {
            @Override
            public Observable<ResponseBody> downloadFile(String downParam, String fileUrl) {
                return mServiceInterface.downloadFile(downParam,fileUrl);
            }
        });
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                httpUtil.cancelAll();
            }
        });
    }

    public static ServiceInterface getServiceInterface() {
        return mServiceInterface;
    }

}
