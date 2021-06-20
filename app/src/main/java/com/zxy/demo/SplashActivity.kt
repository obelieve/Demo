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
        GlobalScope.launch(Dispatchers.Main) {
            withTimeoutOrNull(2000) {
                delay(1000)
            }
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
                    LogUtil.e("MAIN onFound")
                }

                override fun onLost(postcard: Postcard) {
                    LogUtil.e("MAIN onLost")
                }

                override fun onArrival(postcard: Postcard) {
                    LogUtil.e("MAIN onArrival 跳转广告页")
                    ARouter.getInstance()
                        .build(AppPageRouter.SPLASH_AD)
                        .navigation()
                }

                override fun onInterrupt(postcard: Postcard) {
                    LogUtil.e("MAIN onInterrupt 跳转广告页")
                }
            })
    }
}
