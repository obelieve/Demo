package com.zxy.demo.http;


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
                .addHeader("platform","android")
                .addHeader("channel","2048_android")
                .addHeader("version","1.1.0")
                .addHeader("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9hcGkuZGV2LjIwNDguY29tXC9cL2FwaVwvcmVnX29yX2xvZyIsImlhdCI6MTU4NDU4MTQzNCwiZXhwIjoxNTg1MTg2MjM0LCJuYmYiOjE1ODQ1ODE0MzQsImp0aSI6ImJuYThhck1zWE5hZEVhaHEiLCJzdWIiOjI0NCwicHJ2IjoiMjNiZDVjODk0OWY2MDBhZGIzOWU3MDFjNDAwODcyZGI3YTU5NzZmNyJ9.h1mt3_AD3vurfSwAsE-GPoDf7bApMM0sytxzeFZ6C3o")
                .addHeader("system","XiaoMi MIX2:0")
                .addHeader("Accept","application/x.2048.v2+json")
                .build();
        Response response = chain.proceed(request);
        return response;
    }
}
