package com.zxy.demo;

import android.widget.Toast;

import com.obelieve.frame.application.BaseApplication;
import com.obelieve.frame.net.HttpUtil;
import com.obelieve.frame.utils.ToastUtil;

public class App extends BaseApplication {

    private static ServiceInterface sServiceInterface;

    public static ServiceInterface getServiceInterface(){
        return sServiceInterface;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.init(this);
        sServiceInterface = HttpUtil.build().baseUrl(ServiceInterface.BASE_URL).create(ServiceInterface.class);
    }
}
