package com.zxy.demo

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.obelieve.frame.utils.ToastUtil
import com.obelieve.frame.utils.log.LogUtil
import java.text.SimpleDateFormat

const val TAG = "App"
val simpleDateFormat:SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSS")
fun time(timeMills:Long):String{
    return simpleDateFormat.format(timeMills)
}
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LogUtil.e(TAG,"AppApplication#onCreate ${time(System.currentTimeMillis())}")
        ARouter.init(this)
        ToastUtil.init(this)
    }
}
