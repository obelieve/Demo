package com.zxy.frame.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.zxy.frame.json.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.zxy.frame.utility.CheckUtil.checkNotNull;

public class OkHttpUtil
{
    private static final int ON_FAILURE = 1;
    private static final int ON_RESPONSE = 2;

    private static final int CONNECT_TIMEOUT = 10 * 1000;
    private static final int READ_TIMEOUT = 30 * 1000;
    private static final int WRITE_TIMEOUT = 30 * 1000;

    private static final MediaType FILE_MEDIA_TYPE = MediaType.parse("application/octet-stream");
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpUtil sOkHttpUtil;
    private static OkHttpClient.Builder sBuilder;
    private OkHttpClient mOkHttpClient;

    private static Handler sHandler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            MainCallback callback;
            callback = (MainCallback) msg.obj;
            switch (msg.what)
            {
                case ON_FAILURE:
                    callback.onFailure(callback.mException);
                    callback.onFinish();
                    break;
                case ON_RESPONSE:
                    callback.onResponse(callback.mModel);
                    callback.onFinish();
                    break;
                default:
                    break;
            }
        }
    };

    private OkHttpUtil()
    {
        if (sBuilder != null)
        {
            mOkHttpClient = sBuilder.build();
        } else
        {
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                    .build();
        }
    }

    /***
     * 初始化配置
     * @return
     */
    public static OkHttpClient.Builder init()
    {
        sBuilder = new OkHttpClient.Builder();
        return sBuilder;
    }

    public static OkHttpUtil getInstance()
    {
        if (sOkHttpUtil == null)
        {
            synchronized (OkHttpUtil.class)
            {
                if (sOkHttpUtil == null)
                {
                    sOkHttpUtil = new OkHttpUtil();
                }
            }
        }
        return sOkHttpUtil;
    }

    /**
     * @return
     * @throws if OkHttpClient object ==null,throw new NullPointerException
     */
    public OkHttpClient getOkHttpClient()
    {
        checkNotNull(mOkHttpClient);
        return mOkHttpClient;
    }

    public void get(String url, Callback callback)
    {
        Request request = new Request.Builder().url(url)
                .get().build();
        requestHttp(request, callback);
    }

    public void postJson(String url, String json, Callback callback)
    {
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, json);
        Request request = new Request.Builder().url(url)
                .post(body).build();
        requestHttp(request, callback);
    }

    public void postFile(String url, Callback callback, File file)
    {
        RequestBody fileBody = RequestBody.create(FILE_MEDIA_TYPE, file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        requestHttp(request, callback);
    }


    /**
     * @param url
     * @param map
     * @param callback
     */
    public void postForm(String url, Map<String, Object> map,
                         Callback callback)
    {
        post(url, map, null, callback);
    }

    public void post(String url, Map<String, Object> paramMap, Map<String, File> fileMap, Callback callback)
    {
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        bodyBuilder.setType(MultipartBody.FORM);
        if (paramMap != null)
        {
            for (Entry<String, Object> entry : paramMap.entrySet())
            {
                bodyBuilder.addFormDataPart(entry.getKey() == null ? "" : entry.getKey(), entry.getValue() == null ? "" : entry.getValue().toString());
            }
        }
        if (fileMap != null)
        {
            for (Entry<String, File> entry : fileMap.entrySet())
            {
                RequestBody fileBody = RequestBody.create(FILE_MEDIA_TYPE, entry.getValue());
                bodyBuilder.addFormDataPart("file", entry.getValue().getName(), fileBody);
            }
        }
        Request request = new Request.Builder().url(url)
                .post(bodyBuilder.build()).build();
        requestHttp(request, callback);
    }

    public void requestHttp(Request request, Callback callback)
    {
        if (callback instanceof MainCallback)
        {
            mainThreadHandle((MainCallback) callback, request);
        } else
        {
            mOkHttpClient.newCall(request).enqueue(callback);
        }
    }

    private void mainThreadHandle(MainCallback callback, Request request)
    {
        final MainCallback mainCallback = callback;
        callback.onStart();
        mOkHttpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                mainCallback.onFailure(call, e);
                Message msg = sHandler.obtainMessage();
                msg.what = OkHttpUtil.ON_FAILURE;
                msg.obj = mainCallback;
                sHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response)
            {
                int what = 0;
                try
                {
                    mainCallback.onResponse(call, response);
                    what = OkHttpUtil.ON_RESPONSE;
                } catch (Exception e)
                {
                    mainCallback.mException = new Exception(e);
                    what = OkHttpUtil.ON_FAILURE;
                } finally
                {
                    Message msg = sHandler.obtainMessage();
                    msg.what = what;
                    msg.obj = mainCallback;
                    sHandler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * UI Thread Callback
     */
    public static abstract class MainCallback<T> implements Callback
    {
        private T mModel;
        private Exception mException;
        private String mResponseString;

        public T getModel()
        {
            return mModel;
        }

        public Exception getException()
        {
            return mException;
        }

        public String getResponseString()
        {
            return mResponseString;
        }

        public void onStart()
        {

        }

        public abstract void onFailure(Exception error);

        public abstract void onResponse(T model);

        public void onFinish()
        {

        }

        @Override
        public void onFailure(Call call, IOException ioexception)
        {
            mException = ioexception;
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException
        {
            mResponseString = response.body().string();
            mModel = JsonUtil.parseJson(mResponseString, getModelClass());
        }

        protected Class<T> getModelClass()
        {
            final ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
            final Type[] types = parameterizedType.getActualTypeArguments();
            if (types != null && types.length > 0)
            {
                return (Class<T>) types[0];
            } else
            {
                throw new RuntimeException("generic type not found");
            }
        }
    }

}
