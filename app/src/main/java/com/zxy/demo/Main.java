package com.zxy.demo;


import com.obelieve.frame.net.ApiBaseResponse;
import com.zxy.demo.entity.UserInfo;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;

import okhttp3.ResponseBody;

import okio.BufferedSink;

import okio.Okio;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


public class Main {
    public static void main(String[] args) throws Exception {
//        reqGet();
//        reqGetDownload();
        reqPost();
        Thread.sleep(500);
        reqPost("11");
    }

    /**
     * - 1.GET请求
     * 	- 1.带参数
     * 	- 2.字节流下载
     * - 2.POST请求
     * 	- 2.1 不带参数
     * 	- 2.2 带参数
     * 	- 2.3 多部分表格提交 multipart/form-data
     */

    private static ServiceInterface sServiceInterface = new Retrofit.Builder().baseUrl(ServiceInterface.Companion.getBASE_URL()).client(
            new OkHttpClient.Builder().addInterceptor(new HttpInterceptor()).build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ApiConverterFactory.Companion.create())
            .build().create(ServiceInterface.class);

    /**
     * Get请求
     */
    private static void reqGet() {
        sServiceInterface.getBaidu("https://www.baidu.com").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println("reqGet百度 "+response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {

            }
        });
    }

    /**
     * Get请求下载，带有@Streaming
     */
    private static void reqGetDownload(){
        String url = "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2103804686,792857297&fm=26&gp=0.jpg";
        sServiceInterface.downloadFile("Range: bytes=10-",url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    BufferedSink sink = Okio.buffer(Okio.sink(new File("C:\\Users\\Administrator\\Desktop", "testImage" + 1 + ".png")));
                    sink.writeAll(response.body().source());
                    sink.close();
                    System.out.println("req图片 ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {

            }
        });
    }

    /**
     * Post请求
     */
    private static void reqPost(){
        sServiceInterface.getUserInfo().subscribe(new Observer<ApiBaseResponse<UserInfo>>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {

            }

            @Override
            public void onNext(@NotNull ApiBaseResponse<UserInfo> response) {
                System.out.println("获取用户信息"+response.getData());
            }

            @Override
            public void onError(@NotNull Throwable e) {
                System.out.println("获取用户信息err "+e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * Post请求带参数
     * @param nickname
     */
    private static void reqPost(String nickname){
        sServiceInterface.modifyUserInfo("nickname","",nickname,"").subscribe(new Observer<ApiBaseResponse<String>>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {

            }

            @Override
            public void onNext(@NotNull ApiBaseResponse<String> response) {
                System.out.println("修改用户信息:"+response.getData());
            }

            @Override
            public void onError(@NotNull Throwable e) {
                System.out.println("修改用户信息err "+e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

}
