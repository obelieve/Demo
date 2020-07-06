package com.news.mockapi;

import android.text.TextUtils;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by zxy
 * on 2020/4/26
 */
public class MockApiInterceptor implements Interceptor {

    private static final String REQUEST_FORMAT = "REQUEST: \n%s\nmethod: %s\nheaders:\n%s \nrequestBody-length = %s\nrequestBody-contentType:%s\n";
    private static final String RESPONSE_FORMAT = "\nRESPONSE: \nprotocol: %s code: %s message: %s\nheaders:\n%s\nbody:\n%s";
    private static final int MAX_CONTENT_LENGTH = 8 * 1024;

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        RequestBody requestBody = request.body();
        ResponseBody responseBody = response.body();
        String start = " \n\n =================================START HTTP/HTTPS==============================\n";
        String requestStr = String.format(REQUEST_FORMAT,
                request.url().toString(),
                request.method(),
                request.headers(),
                requestBody != null ? requestBody.contentLength() : null,
                requestBody != null ? requestBody.contentType() : null);
        String rBody = null;
        String tag = MockApiUtil.getMockApiDataTag(request.url().toString());
        if (responseBody != null) {
            if (responseBody.contentLength() < MAX_CONTENT_LENGTH) {
                response.protocol();
                response.code();
                response.message();
                if (TextUtils.isEmpty(tag)) {
                    rBody = responseBody.string();
                }else{
                    rBody = MockApiUtil.getData(tag);
                }
                responseBody.close();
                responseBody = ResponseBody.create(rBody, responseBody.contentType());
            } else {
                rBody = "body >= 8KB  contentType:" + responseBody.contentType();
            }
        }
        if(!TextUtils.isEmpty(tag)){
            rBody = "\n【模拟数据】:\n"+rBody+"\n";
        }
        String responseStr = String.format(RESPONSE_FORMAT,
                response.protocol(),
                response.code(),
                response.message(),
                response.headers(),
                rBody);
        String end = " \n\n ==================================END HTTP/HTTPS===============================\n";
        Log.i(MockApiInterceptor.class.getSimpleName(), start + requestStr + responseStr + end);
        return response.newBuilder().body(responseBody).build();
    }



}
