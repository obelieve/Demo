package com.zxy.demo.httpbin

import com.zxy.demo.httpbin.ServiceInterface.Companion.BASE_URL
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException

/**
 * 协程
 */
object CMain {
    val sServiceInterface = Retrofit.Builder().baseUrl(BASE_URL).client(
        OkHttpClient.Builder().addInterceptor(object : Interceptor {
            @Volatile
            private var host //="www.baidu.com"
                    : String? = null

            fun setHost(host: String?) {
                this.host = host
            }

            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                var request = chain.request()
                val host = host
                if (host != null) {
                    val newUrl = request.url.newBuilder().host(host).build()
                    request = request.newBuilder().url(newUrl).build()
                }
                return chain.proceed(request)
            }
        }).build()
    )
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(ApiConverterFactory.create())
        .build().create(ServiceInterface::class.java)

    @JvmStatic
    fun main(args: Array<String>){
        runBlocking {
            System.out.println("get ${Thread.currentThread()}")
            val job = GlobalScope.async {
                val r = sServiceInterface.coroutinesGet()
                System.out.println("get ${Thread.currentThread()} ${r.string()}")
            }
            job.await()
        }
    }

}