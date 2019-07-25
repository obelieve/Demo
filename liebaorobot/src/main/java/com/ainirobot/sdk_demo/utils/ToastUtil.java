package com.ainirobot.sdk_demo.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * @author Orion
 * @time 2018/9/13
 */
public class ToastUtil {

    private static final String TAG = "ToastUtil";

    public static UIHandler uiHandler;

    private ToastUtil() {
    }

    public static ToastUtil getInstance() {
        return SingleHolder.INSTANCE;
    }

    private static class SingleHolder {
        public static final ToastUtil INSTANCE = new ToastUtil();
    }

    public void init(Context context) {
        if (uiHandler == null) {
            uiHandler = new UIHandler(context);
        }
    }

    private static class UIHandler extends Handler {

        WeakReference<Context> mActivityReference;

        UIHandler(Context activity) {
            mActivityReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Context context = mActivityReference.get();
            if (context != null) {
                switch (msg.what) {
                    case 0:
                        String result = String.format("result:[code:%s, msg:%s]", msg.arg1, String.valueOf(msg.obj));
                        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        String update = String.format("update:[code:%s, msg:%s]", msg.arg1, String.valueOf(msg.obj));
                        Toast.makeText(context, update, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        String error = String.format("error:[code:%s, msg:%s]", msg.arg1, String.valueOf(msg.obj));
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(context, String.valueOf(msg.obj), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void onResult(int code, String msg) {
        Message message = uiHandler.obtainMessage(0);
        message.arg1 = code;
        message.obj = msg;
        uiHandler.sendMessage(message);
    }

    public void onUpdate(int code, String msg) {
        Message message = uiHandler.obtainMessage(1);
        message.arg1 = code;
        message.obj = msg;
        uiHandler.sendMessage(message);
    }

    public void onError(int code, String msg) {
        Message message = uiHandler.obtainMessage(2);
        message.arg1 = code;
        message.obj = msg;
        uiHandler.sendMessage(message);
    }

    public void toast(String msg) {
        Message message = uiHandler.obtainMessage(3);
        message.arg1 = 0;
        message.obj = msg;
        uiHandler.sendMessage(message);
    }

    public void release() {
        if (uiHandler != null) {
            uiHandler.removeCallbacksAndMessages(null);
            uiHandler = null;
        }
    }

}
