package com.zxy.demo;

import android.os.Bundle;

import com.obelieve.frame.base.ApiBaseActivity2;
import com.obelieve.frame.net.ApiBaseResponse;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
//        postGetUserInfo();
//        postModifyUserInfo(new Random().nextInt(1000)+"");
//        postGetLogstoreConfig();
    }

    /**
     * 外部URL
     */
    private void postGetLogstoreConfig() {
        String str = "oktestlog_6099dcfa413db" + '_' + "16" + '_'  + "f3e3a187627d7f096f70c9616a320f78c80336de";
        String sign= MD5Util.md5(str);
        App.getServiceInterface().getLogstoreConfig(ServiceInterface.Companion.getEXTERNAL_URL(), "16","oktestlog_6099dcfa413db",sign).subscribeOn(Schedulers.io()).subscribe(new Observer<ApiBaseResponse<LogConfigEntity>>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {

            }

            @Override
            public void onNext(@NotNull ApiBaseResponse<LogConfigEntity> response) {
                LogUtil.e("getLogstoreConfig日志上报:"+response.getData());
            }

            @Override
            public void onError(@NotNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 带form参数的Post请求
     * @param nickname
     */
    private void postModifyUserInfo(String nickname) {
        App.getServiceInterface().modifyUserInfo("nickname","",nickname,"").subscribeOn(Schedulers.io()).subscribe(new Observer<ApiBaseResponse<String>>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {

            }

            @Override
            public void onNext(@NotNull ApiBaseResponse<String> response) {
                LogUtil.e("modifyUserInfo修改用户信息:"+response.getData());
            }

            @Override
            public void onError(@NotNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * post请求
     */
    private void postGetUserInfo() {
        App.getServiceInterface().getUserInfo().subscribeOn(Schedulers.io()).subscribe(new Observer<ApiBaseResponse<UserInfo>>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {

            }

            @Override
            public void onNext(@NotNull ApiBaseResponse<UserInfo> response) {
                LogUtil.e("getUserInfo获取用户信息:"+response.getData());
            }

            @Override
            public void onError(@NotNull Throwable e) {
                LogUtil.e("getUserInfo获取用户信息 e:"+e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * get请求
     */
    private void reqGet() {
        App.getServiceInterface().getBaidu("https://www.baidu.com").enqueue(new Callback<ResponseBody>() {
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
