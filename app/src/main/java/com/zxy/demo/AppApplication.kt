package com.zxy.demo

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.obelieve.frame.utils.ToastUtil

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ARouter.init(this)
        ToastUtil.init(this)
    }
}
