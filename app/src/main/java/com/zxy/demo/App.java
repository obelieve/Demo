package com.zxy.demo;

import com.zxy.demo.http.HttpInterceptor;
import com.zxy.demo.http.ServiceInterface;
import com.zxy.frame.application.BaseApplication;
import com.zxy.frame.net.ApiService;
import com.zxy.frame.net.HttpUtil;
import com.zxy.frame.net.convert.ApiCustomGsonConverterFactory;
import com.zxy.frame.net.download.DownloadInterface;
import com.zxy.frame.utils.log.LogInterceptor;
import com.zxy.frame.utils.ToastUtil;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class App extends BaseApplication {

    private static ServiceInterface mServiceInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.init(this);
        mServiceInterface = HttpUtil.build().baseUrl(ServiceInterface.BASE_URL)
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
    }

    public static ServiceInterface getServiceInterface() {
        return mServiceInterface;
    }

}
