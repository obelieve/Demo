package com.zxy.frame.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.io.IOException;
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
                    callback.onFailure(callback.getIOException());
                    break;
                case ON_RESPONSE:
                    try
                    {
                        callback.onResponse(callback.getResponseString());
                    } catch (Exception e)
                    {
                        callback.onFailure(e);
                    }
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
        requestHttp(callback, request);
    }

    public void postJson(String url, String json, Callback callback)
    {
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, json);
        Request request = new Request.Builder().url(url)
                .post(body).build();
        requestHttp(callback, request);
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
        requestHttp(callback, request);
    }


    /**
     * @param url
     * @param map
     * @param callback
     */
    @Deprecated
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
        requestHttp(callback, request);
    }

    private void requestHttp(Callback callback, Request request)
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
        mOkHttpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                Message msg = sHandler.obtainMessage();
                msg.what = OkHttpUtil.ON_FAILURE;
                msg.obj = mainCallback;
                sHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                Message msg = sHandler.obtainMessage();
                msg.what = OkHttpUtil.ON_RESPONSE;
                msg.obj = mainCallback;
                sHandler.sendMessage(msg);
            }
        });
    }

    /**
     * UI Thread Callback
     */
    public abstract class MainCallback implements Callback
    {
        private IOException mIOException;
        private String mResponseString;

        public IOException getIOException()
        {
            return mIOException;
        }

        public void setIOException(IOException IOException)
        {
            mIOException = IOException;
        }

        public String getResponseString()
        {
            return mResponseString;
        }

        public void setResponseString(String responseString)
        {
            mResponseString = responseString;
        }

        public abstract void onFailure(Exception error);

        public abstract void onResponse(String response);

        @Override
        public void onFailure(Call call, IOException ioexception)
        {
            mIOException = ioexception;
        }

        @Override
        public void onResponse(Call call, Response response)
        {
            try
            {
                mResponseString = response.body().string();
            } catch (IOException e)
            {
                e.printStackTrace();
                onFailure(e);
            }
        }

    }

}
