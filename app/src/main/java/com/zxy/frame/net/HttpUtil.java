package com.zxy.frame.net;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by zxy on 2019/08/06.
 */
public class HttpUtil {

    private static final int READ_TIMEOUT_SECONDS = 30;

    private static final int WRITE_TIMEOUT_SECONDS = 30;

    private static HttpUtil sHttpUtil = new HttpUtil();

    private Retrofit mRetrofit;
    private OkHttpClient mOkHttpClient;

    private String mBaseUrl;
    private List<Interceptor> mInterceptors;
    private List<Interceptor> mNetInterceptors;
    private List<Converter.Factory> mConverterFactorys;

    public static HttpUtil build() {
        return sHttpUtil;
    }

    public HttpUtil baseUrl(String baseUrl) {
        sHttpUtil.mBaseUrl = baseUrl;
        return sHttpUtil;
    }

    public HttpUtil addInterceptor(Interceptor interceptor) {
        if (sHttpUtil.mInterceptors == null) {
            sHttpUtil.mInterceptors = new ArrayList<>();
        }
        sHttpUtil.mInterceptors.add(interceptor);
        return sHttpUtil;
    }

    public HttpUtil addNetInterceptor(Interceptor interceptor) {
        if (sHttpUtil.mNetInterceptors == null) {
            sHttpUtil.mNetInterceptors = new ArrayList<>();
        }
        sHttpUtil.mNetInterceptors.add(interceptor);
        return sHttpUtil;
    }

    public HttpUtil addConverterFactory(Converter.Factory factory) {
        if (sHttpUtil.mConverterFactorys == null) {
            sHttpUtil.mConverterFactorys = new ArrayList<>();
        }
        sHttpUtil.mConverterFactorys.add(factory);
        return sHttpUtil;
    }

    public <T> T create(Class<T> clazz) {
        if (mRetrofit == null) {
            Retrofit.Builder builder = new Retrofit.Builder().baseUrl(mBaseUrl);
            if (mConverterFactorys != null) {
                for (Converter.Factory factory : mConverterFactorys) {
                    builder.addConverterFactory(factory);
                }
            }
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                    .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (mInterceptors != null) {
                for (Interceptor in : mInterceptors) {
                    clientBuilder.addInterceptor(in);
                }
            }
            if (mNetInterceptors != null) {
                for (Interceptor in : mNetInterceptors) {
                    clientBuilder.addInterceptor(in);
                }
            }
            mRetrofit = builder.client(clientBuilder.build()).build();
        }
        return sHttpUtil.mRetrofit.create(clazz);
    }

}
