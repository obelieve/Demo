package com.zxy.mall.http;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by zxy on 2019/08/06.
 */
public class HttpInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
//                .addHeader("platform","android")
//                .addHeader("channel","_2048android")
//                .addHeader("version","1.6.2")
//                .addHeader("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9hcGkuZGV2LjIwNDguY29tXC9hcGlcL3JlZnJlc2giLCJpYXQiOjE1ODU2NTEwNzksImV4cCI6MTU4NjMxMTI2MywibmJmIjoxNTg1NzA2NDYzLCJqdGkiOiJmR1RnZTkycGphUExiU2pKIiwic3ViIjo5MzY3LCJwcnYiOiIyM2JkNWM4OTQ5ZjYwMGFkYjM5ZTcwMWM0MDA4NzJkYjdhNTk3NmY3In0.q3_DWF7qu2GFU-e4Dtd96lbc279asX1GJGWY_E_KzJw")
//                .addHeader("system","XiaoMi MIX2:0")
//                .addHeader("Accept","application/x.2048.v2+json")
//                .addHeader("udidcode","24157e7e-f9f6-460f-a299-fe39ced9fe40")
                .build();
        Response response = chain.proceed(request);
        return response;
    }
}
