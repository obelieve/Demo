package com.zxy.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler
import android.util.Log

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.zxy.utility.LogUtil
import java.util.*
import javax.security.auth.login.LoginException

/**
 * Created by Admin
 * on 2020/11/3
 */
class LifecycleActivity : AppCompatActivity() {

    var registery: LifecycleRegistry? =null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.e()
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(TestObserver());
        lifecycle.addObserver(object: DefaultLifecycleObserver{
            override fun onCreate(owner: LifecycleOwner) {
                LogUtil.e("DefaultLifecycleObserver-onCreate","onCreate")
            }

            override fun onStart(owner: LifecycleOwner) {
                LogUtil.e("DefaultLifecycleObserver-onStart","onStart")
            }

            override fun onResume(owner: LifecycleOwner) {
                LogUtil.e("DefaultLifecycleObserver-onResume","onResume")
            }

            override fun onPause(owner: LifecycleOwner) {
                LogUtil.e("DefaultLifecycleObserver-onPause","onPause")
            }

            override fun onStop(owner: LifecycleOwner) {
                LogUtil.e("DefaultLifecycleObserver-onStop","onStop")
            }

            override fun onDestroy(owner: LifecycleOwner) {
                LogUtil.e("DefaultLifecycleObserver-onDestroy","onDestroy")
            }
        });
        registery = LifecycleRegistry(this);
        registery!!.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        registery!!.addObserver(TestObserver());
    }



    override fun onStart() {
        super.onStart()
        registery!!.handleLifecycleEvent(Lifecycle.Event.ON_START)
        LogUtil.e()
    }

    override fun onResume() {
        super.onResume()
        registery!!.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        LogUtil.e()
    }

    override fun onPause() {
        registery!!.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        super.onPause()
        LogUtil.e()
    }

    override fun onStop() {
        registery!!.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        super.onStop()
        LogUtil.e()
    }

    override fun onDestroy() {
        registery!!.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        super.onDestroy()
        LogUtil.e()
    }

}


