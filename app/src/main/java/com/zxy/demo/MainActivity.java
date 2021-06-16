package com.zxy.demo;

import android.os.Bundle;

import com.obelieve.frame.base.ApiBaseActivity2;
import com.zxy.demo.databinding.ActivityMainBinding;


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
    }
}
