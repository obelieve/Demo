package com.zxy.demo.http;

import com.zxy.utility.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LogInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        String data = response.body().string();
        response.body().close();
        String start = " \n\n =================================START HTTP/HTTPS==============================\n";
        String requestStr = String.format("request: \nmethod: %s\n headers:\n%s body:%s length=%s\n", request.method(), request.headers(), request.body().contentType(), request.body().contentLength());
        String responseStr = String.format("\n response: \n %s", data);
        String end = " \n\n ==================================END HTTP/HTTPS===============================\n";
        LogUtil.i(start + requestStr + responseStr + end);
        return response.newBuilder().body(ResponseBody.create(data, MediaType.parse("application/json"))).build();
    }
}
