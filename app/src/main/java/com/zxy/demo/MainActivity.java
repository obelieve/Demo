package com.zxy.demo;

import android.os.Bundle;

import com.obelieve.frame.base.ApiBaseActivity2;
import com.obelieve.frame.net.ApiBaseResponse;
import com.obelieve.frame.net.convert.ApiCustomGsonConverterFactory;
import com.obelieve.frame.utils.log.LogInterceptor;
import com.obelieve.frame.utils.log.LogUtil;
import com.obelieve.frame.utils.secure.MD5Util;
import com.zxy.demo.databinding.ActivityMainBinding;
import com.zxy.demo.entity.LogConfigEntity;
import com.zxy.demo.entity.UserInfo;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.Field;


/**
1.Retrofit 接口方法得到很多参数数据：
 1.方法和方法参数注解得到一些请求方法、请求参数、等
 2.返回类型和方法注解获得特定的CallAdapter
 3.返回类型和方法注解获取特定的Converter
 5.最后获取装配完成的CallAdapted->继承自HttpServiceMethod->继承自ServiceMethod
 6.动态代理invoke调用：
 6.1
 @Override final @Nullable ReturnT invoke(Object[] args) {
 Call<ResponseT> call = new OkHttpCall<>(requestFactory, args, callFactory, responseConverter); //RequestFactory->RequestBuilder
 return adapt(call, args);
 }
 6.2
 @Override protected ReturnT adapt(Call<ResponseT> call, Object[] args) {
 return callAdapter.adapt(call);
 }
 6.3 RxJava 返回处理方式
 RxJava2CallAdapterFactory -> RxJava2CallAdapter
 **/
public class MainActivity extends ApiBaseActivity2<ActivityMainBinding> {

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        reqGet();
    }


    /**
     * get请求
     */
    private void reqGet() {
        ServiceInterface in = new Retrofit.Builder().baseUrl(ServiceInterface.Companion.getBASE_URL()).client(
                new OkHttpClient.Builder()
                        .addInterceptor(new LogInterceptor()).build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ApiCustomGsonConverterFactory.create())
                .build().create(ServiceInterface.class);
        in.getUrl("https://www.baidu.com").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    LogUtil.e("reqGet百度 "+response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {

            }
        });
    }


}
