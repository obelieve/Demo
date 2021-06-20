package com.zxy.demo

import android.os.Bundle
import android.os.CountDownTimer
import com.alibaba.android.arouter.facade.annotation.Route
import com.obelieve.frame.base.ApiBaseActivity2
import com.zxy.demo.databinding.ActivitySplashAdBinding

@Route(path = AppPageRouter.SPLASH_AD)
class SplashADActivity : ApiBaseActivity2<ActivitySplashAdBinding>() {

    var mCountDownTimer: CountDownTimer? = null

    override fun initCreateAfterView(savedInstanceState: Bundle?) {

        mViewBinding.tvSkip.text = getSkipText(3000)
        mViewBinding.tvSkip.setOnClickListener {
            finish()
        }
        mCountDownTimer =
            object : CountDownTimer(3000, 1000) {
                override fun onFinish() {
                    finish()
                }

                override fun onTick(millisUntilFinished: Long) {
                    mViewBinding.tvSkip.text = getSkipText(millisUntilFinished)
                }

            }
        mCountDownTimer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCountDownTimer?.cancel()
        mCountDownTimer = null
    }

    private fun getSkipText(delayTime: Long): String {
        return String.format("SKIP %s", "${delayTime / 1000}")
    }

}
