package com.zxy.demo

import android.app.Activity

import com.obelieve.frame.application.BaseApplication
import com.obelieve.frame.net.ApiErrorCode
import com.obelieve.frame.net.ApiServiceException
import com.obelieve.frame.net.ApiServiceExceptionHandle
import com.obelieve.frame.net.HttpUtil
import com.obelieve.frame.net.convert.ApiCustomGsonConverterFactory
import com.obelieve.frame.utils.ToastUtil
import com.obelieve.frame.utils.log.LogInterceptor
import com.zxy.demo.ServiceInterface.Companion.BASE_URL
import io.reactivex.plugins.RxJavaPlugins
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

        val httpUtil: HttpUtil = HttpUtil.build().baseUrl(BASE_URL)
            .addInterceptor(HttpInterceptor())
        if (BuildConfig.DEBUG) {
            httpUtil.addInterceptor(LogInterceptor())
        }

        sServiceInterface = httpUtil
            .addConverterFactory(ApiCustomGsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .create(ServiceInterface::class.java)

        ApiServiceExceptionHandle.setApiExtendRespondThrowableListener(object :
            ApiServiceExceptionHandle.ApiExtendRespondThrowableListener {
            fun preProcessException(ex: ApiServiceException?) {
                if (ex?.code == ApiErrorCode.CODE_TOKEN_ERROR) {
                    ex.toast = 1
                } else {
                    ex?.toast = 1
                }
            }

            override fun defHandleException(
                activity: Activity?,
                ex: ApiServiceException?,
                window: Int,
                toast: Int
            ) {
            }

        })
    }

}