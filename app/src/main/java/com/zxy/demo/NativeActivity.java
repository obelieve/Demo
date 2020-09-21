package com.zxy.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.qihoo360.replugin.RePlugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Created by Admin
 * on 2020/9/21
 */
public class NativeActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);
        FrameLayout fl = findViewById(R.id.fl_content);
        Plugin.requestNativeAD(this,fl);
    }


    public static class Plugin{
        public static void requestNativeAD(final Activity activity, final FrameLayout frameLayout) {
            try {
//                ClassLoader classLoader = DexClassLoader.getSystemClassLoader().getParent();
                ClassLoader classLoader = RePlugin.fetchClassLoader("plugin");
                Class<?> methodClass = classLoader.loadClass("com.news.project.ADManager");//ADManager.class
                /**
                 * 被代理的接口
                 */
                Class<?> callBackClass = classLoader.loadClass("com.qq.e.ads.nativ.NativeADUnifiedListener");//NativeADUnifiedListener.class;
                /**
                 * callBackClass : 被代理的接口的Class
                 * proxListener : 被代理接口的实例
                 */
                Method load = methodClass.getDeclaredMethod("requestNativeUnifiedAD", new Class[]{Activity.class, callBackClass});
                Log.e("ClassLoader","callBackClass.getClassLoader()="+callBackClass.getClassLoader());
                Object proxyListener = Proxy.newProxyInstance(callBackClass.getClassLoader(), new Class[]{callBackClass}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //判断是什么方法被调用了嘛
                        String methodName = method.getName();
                        if ("onADLoaded".equals(methodName)) {
                            List<Object> objects = (List<Object>) args[0];
                            if (objects != null && objects.size() > 0) {
                                bindNativeUnifiedADView(activity, frameLayout, objects.get(0));
                            }
                        }
                        return null;
                    }
                });
                load.invoke(null, activity, proxyListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private static void bindNativeUnifiedADView(Activity activity, FrameLayout frameLayout, Object object) {
            try {
//                ClassLoader classLoader = DexClassLoader.getSystemClassLoader();
                ClassLoader classLoader = RePlugin.fetchClassLoader("plugin");
                Class<?> methodClass = classLoader.loadClass("com.news.project.ADManager");//ADManager.class;
                Method method = methodClass.getMethod("bindNativeUnifiedADView",Activity.class,FrameLayout.class,Object.class);
                method.invoke(methodClass, activity, frameLayout, object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
