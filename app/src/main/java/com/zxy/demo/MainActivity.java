package com.zxy.demo;

import android.os.Bundle;

import com.obelieve.frame.base.ApiBaseActivity2;
import com.obelieve.frame.utils.log.LogUtil;
import com.zxy.demo.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
//        reqGet();
//        reqGet("name","file");
//        reqGet("ho");
//        reqPost();
//        reqPost("obelieve","内容");
//        reqPatch("obelieve","内容");
//        reqDelete();
    }


    /**
     * get请求
     */
    private void reqGet() {
        App.getServiceInterface().getUrl("https://www.baidu.com").enqueue(new Callback<ResponseBody>() {
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

    private void reqGet(String name,String value){
        Map<String,String> map = new HashMap<>();
        map.put(name,value);
        App.getServiceInterface().get(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s = response.body().string();
                    LogUtil.e("请求="+call.request().url().toString()+" 响应数据:"+s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void reqGet(String name){
        App.getServiceInterface().get(name).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s = response.body().string();
                    LogUtil.e("请求="+call.request().url().toString()+" 响应数据:"+s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void reqPost(){
        App.getServiceInterface().post().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s = response.body().string();
                    LogUtil.e("请求="+call.request().url().toString()+" 响应数据:"+s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void reqPost(String name,String content){
        App.getServiceInterface().post(name,content).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s = response.body().string();
                    LogUtil.e("请求="+call.request().url().toString()+" 响应数据:"+s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void reqPatch(String name,String content){
        App.getServiceInterface().patch(name,content).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s = response.body().string();
                    LogUtil.e("请求="+call.request().url().toString()+" "+call.request().method()+" 响应数据:"+s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void reqDelete(){
        App.getServiceInterface().delete().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s = response.body().string();
                    LogUtil.e("请求="+call.request().url().toString()+" "+call.request().method()+" 响应数据:"+s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}
