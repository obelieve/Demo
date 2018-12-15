package com.zxy.im.imsdk;

import android.content.Context;

import com.zxy.frame.utility.LogUtil;
import com.zxy.frame.utility.SPUtil;
import com.zxy.im.cache.SPConstant;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by zxy on 2018/12/14 16:11.
 */

public class APPIMContext
{

    private RongIMClient.ConnectCallback mConnectCallback = new IMConnectCallback();
    private RongIMClient.ConnectionStatusListener mConnectionStatusListener = new IMConnectStatusListener();

    private static APPIMContext sAPPIMContext = new APPIMContext();

    private APPIMContext()
    {
    }

    public static APPIMContext getInstance()
    {
        return sAPPIMContext;
    }

    public static void init(Context context)
    {
        RongIM.init(context);
        RongIM.setConnectionStatusListener(sAPPIMContext.mConnectionStatusListener);
    }

    public RongIMClient.ConnectCallback getConnectCallback()
    {
        return mConnectCallback;
    }

    public RongIMClient.ConnectionStatusListener getConnectionStatusListener()
    {
        return mConnectionStatusListener;
    }

    /**
     * 监听连接状态
     */
    public static class IMConnectStatusListener implements RongIMClient.ConnectionStatusListener
    {

        @Override
        public void onChanged(ConnectionStatus connectionStatus)
        {
            LogUtil.e(connectionStatus.getMessage());
        }
    }

    /**
     * 监听Token连接
     */
    public static class IMConnectCallback extends RongIMClient.ConnectCallback
    {

        @Override
        public void onTokenIncorrect()
        {
            LogUtil.e();
            RongIM.getInstance().logout();
        }

        @Override
        public void onSuccess(String s)
        {
            SPUtil.getInstance().getSP().edit()
                    .putString(SPConstant.USER_ID_STR, s).apply();
        }

        @Override
        public void onError(RongIMClient.ErrorCode errorCode)
        {
            LogUtil.e();
        }
    }
}
