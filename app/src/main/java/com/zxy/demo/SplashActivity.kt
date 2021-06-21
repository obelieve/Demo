package com.zxy.demo

import android.annotation.SuppressLint
import android.os.Bundle
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.obelieve.frame.base.ApiBaseActivity2
import com.obelieve.frame.utils.log.LogUtil
import com.zxy.demo.databinding.ActivitySplashBinding
import kotlinx.coroutines.*

class SplashActivity : ApiBaseActivity2<ActivitySplashBinding>() {

    override fun initCreateAfterView(savedInstanceState: Bundle?) {
        LogUtil.e(TAG,"SplashActivity#initCreateAfterView ${time(System.currentTimeMillis())}")
        GlobalScope.launch(Dispatchers.Main) {
            withTimeoutOrNull(2000) {
                delay(1000)
            }
            LogUtil.e(TAG,"协程中执行开始startMain() ${time(System.currentTimeMillis())}")
            startMain()
        }
    }

    @SuppressLint("WrongConstant")
    private fun startMain() {
        val jumpExtra = intent.getStringExtra("jump")
        ARouter.getInstance()
            .build(AppPageRouter.MAIN).withString("jump", jumpExtra)
            .navigation(this, object : NavCallback() {
                override fun onFound(postcard: Postcard) {
                    LogUtil.e(TAG,"ARouter跳转MAIN #onFound ${time(System.currentTimeMillis())}")
                }

                override fun onLost(postcard: Postcard) {
                    LogUtil.e(TAG,"ARouter跳转MAIN #onLost ${time(System.currentTimeMillis())}")
                }

                override fun onArrival(postcard: Postcard) {
                    LogUtil.e(TAG,"ARouter跳转MAIN #onArrival，调用跳转SPLASH_AD ${time(System.currentTimeMillis())}")
                    ARouter.getInstance()
                        .build(AppPageRouter.SPLASH_AD)
                        .navigation()
                }

                override fun onInterrupt(postcard: Postcard) {
                    LogUtil.e(TAG,"ARouter跳转MAIN #onInterrupt ${time(System.currentTimeMillis())}")
                }
            })
    }
}
