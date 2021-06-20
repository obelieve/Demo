package com.zxy.demo;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.obelieve.frame.base.ApiBaseActivity2;
import com.zxy.demo.databinding.ActivityMainBinding;
import kotlinx.coroutines.*

@Route(path = AppPageRouter.MAIN)
public class MainActivity : ApiBaseActivity2<ActivityMainBinding>() {

    override fun initCreateAfterView(savedInstanceState: Bundle?) {
        GlobalScope.launch(Dispatchers.Main) {
            withTimeoutOrNull(1000) {
                delay(500)
            }
            delay(500)
            if(!mActivity.isFinishing&&!mActivity.isDestroyed){
                MainDialog(mActivity)
                    .show()
            }
        }
    }
}
