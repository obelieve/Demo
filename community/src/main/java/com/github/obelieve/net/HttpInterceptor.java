package com.github.obelieve.net;


import androidx.annotation.NonNull;

import com.github.obelieve.repository.cache.constant.SystemValue;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by zxy on 2019/08/06.
 */
public class HttpInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("platform","android")
                .addHeader("channel","_2048android")// SystemValue.chl
                .addHeader("version",SystemValue.versionName)
                .addHeader("Authorization","Bearer " + SystemValue.token)
                .addHeader("system", SystemValue.model + ";" + SystemValue.system)
                .addHeader("Accept","application/x.2048.v2+json")
                .addHeader("udidcode",SystemValue.deviceId)
                .build();
        Response response = chain.proceed(request);
        return response;
    }
}
