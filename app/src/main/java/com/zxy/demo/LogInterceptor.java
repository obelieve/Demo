package com.zxy.demo;

import com.zxy.utility.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LogInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        LogUtil.e(response.body().string()+" "+Thread.currentThread());
        return response.newBuilder().build();
    }
}
