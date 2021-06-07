package com.zxy.demo;


import java.io.File;
import java.io.IOException;



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
    public static void main(String[] args) {
        reqGet();
        reqDownload();
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
            new OkHttpClient.Builder().build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(ServiceInterface.class);

    /**
     * get请求
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

    private static void reqDownload(){
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
}
