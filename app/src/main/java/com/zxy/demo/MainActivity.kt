package com.zxy.demo;

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.obelieve.frame.base.ApiBaseActivity2
import com.obelieve.frame.utils.log.LogUtil
import com.zxy.demo.databinding.ActivityMainBinding
import kotlinx.coroutines.*

@Route(path = AppPageRouter.MAIN)
class MainActivity : ApiBaseActivity2<ActivityMainBinding>() {
    var isResume:Boolean = false
    var needShowMainDialog:Boolean = false
    override fun initCreateAfterView(savedInstanceState: Bundle?) {
        LogUtil.e(TAG,"MainActivity#initCreateAfterView ${time(System.currentTimeMillis())}")
        GlobalScope.launch(Dispatchers.Main) {
            withTimeoutOrNull(1000) {
                delay(500)
            }
            delay(500)
            if(!mActivity.isFinishing&&!mActivity.isDestroyed){
                LogUtil.e(TAG,"协程调用，弹出MainDialog ${time(System.currentTimeMillis())}")
                if(isResume){
                    needShowMainDialog = false
                    MainDialog(mActivity)
                        .show()
                }else{
                    needShowMainDialog = true
                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        LogUtil.e(TAG,"MainActivity#onRestart ${time(System.currentTimeMillis())}")
    }

    override fun onStart() {
        super.onStart()
        LogUtil.e(TAG,"MainActivity#onStart ${time(System.currentTimeMillis())}")
    }

    override fun onResume() {
        super.onResume()
        isResume = true
        if(needShowMainDialog){
            MainDialog(mActivity)
                .show()
        }
        LogUtil.e(TAG,"MainActivity#onResume ${time(System.currentTimeMillis())}")
    }

    override fun onPause() {
        super.onPause()
        isResume = false
        LogUtil.e(TAG,"MainActivity#onPause ${time(System.currentTimeMillis())}")
    }

    override fun onStop() {
        super.onStop()
        LogUtil.e(TAG,"MainActivity#onStop ${time(System.currentTimeMillis())}")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.e(TAG,"MainActivity#onDestroy ${time(System.currentTimeMillis())}")
    }
}
