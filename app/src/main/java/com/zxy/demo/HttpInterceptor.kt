package com.zxy.demo

import android.content.Context
import com.zxy.demo.ServiceInterface.Companion.token
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 * Created by Admin
 * on 2020/11/13
 */
class HttpInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if ("POST" == request.method) {
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .addHeader("oks-deviceid", "bf661c9459f47d09")
                .addHeader("oks-platform","1")
                .addHeader("oks-channel", "oks-channel")
                .addHeader("oks-version", "1.4.0")
                .addHeader("oks-versioncode", "10")
                .addHeader("oks-timezone", TimeZone.getDefault().id)
                .addHeader("oks-locale", "en")
                .addHeader("oks-country-code", "TH")
                .build()
        }
        return chain.proceed(request)
    }
}