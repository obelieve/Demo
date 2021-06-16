package com.zxy.demo

import com.obelieve.frame.application.BaseApplication
import com.obelieve.frame.utils.ToastUtil
import com.obelieve.frame.utils.log.LogInterceptor
import com.zxy.demo.httpbin.ServiceInterface
import com.zxy.demo.httpbin.ServiceInterface.Companion.BASE_URL
import io.reactivex.plugins.RxJavaPlugins
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class App : BaseApplication() {


    companion object{
        private lateinit var sServiceInterface: ServiceInterface
        fun getContext(): BaseApplication {
            return BaseApplication.getContext()
        }

        @JvmStatic
        fun getServiceInterface(): ServiceInterface {
            return sServiceInterface
        }
    }
    override fun onCreate() {
        super.onCreate()
        ToastUtil.init(this)
        RxJavaPlugins.setErrorHandler {}

        sServiceInterface = Retrofit.Builder().baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().addInterceptor(LogInterceptor()).build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(ServiceInterface::class.java)
    }

}
