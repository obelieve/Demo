package com.zxy.demo;


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
                .addHeader("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9hcGkudGVzdC4yMDQ4LmNvbVwvYXBpXC9yZWZyZXNoIiwiaWF0IjoxNTczMDE4NzE5LCJleHAiOjE1NzM2MjY3ODcsIm5iZiI6MTU3MzAyMTk4NywianRpIjoiSThrdEZIWGJlWEQxaUhsZyIsInN1YiI6MjQ0LCJwcnYiOiIyM2JkNWM4OTQ5ZjYwMGFkYjM5ZTcwMWM0MDA4NzJkYjdhNTk3NmY3In0.GgTpWFg1ay8RJD13Dj55UuoSjj8C_pdza27Fs0ZFVnI")
                .addHeader("system","XiaoMi MIX2:0")
                .addHeader("Accept","application/x.2048.v2+json")
                .build();
        Response response = chain.proceed(request);
        return response;
    }
}
