package com.zxy.im.imsdk;

import android.content.Context;

import com.zxy.frame.utility.LogUtil;
import com.zxy.frame.utility.SPUtil;
import com.zxy.im.cache.SPConstant;

import java.util.List;

import io.rong.imkit.DefExtensionModule;
import io.rong.imkit.DefTextMessageProvider;
import io.rong.imkit.DefVoiceMessageProvider;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
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
        initRongConversation();
    }
    /**
     * 自定义会话页面配置
     */
    private static void initRongConversation()
    {
        //RongIM.registerMessageType(TextMessage.class);
        //RongIM.getInstance().registerMessageTemplate()
        RongIM.registerMessageTemplate(new DefTextMessageProvider());
        RongIM.registerMessageTemplate(new DefVoiceMessageProvider());
        //会话+号 插件
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null)
        {
            for (IExtensionModule module : moduleList)
            {
                if (module instanceof DefaultExtensionModule)
                {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null)
            {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new DefExtensionModule());
            }
        }
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
