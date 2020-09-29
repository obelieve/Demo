package com.github.obelieve;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.github.obelieve.community.BuildConfig;
import com.github.obelieve.net.HttpInterceptor;
import com.github.obelieve.net.ServiceInterface;
import com.github.obelieve.repository.CacheRepository;
import com.github.obelieve.repository.cache.constant.SystemValue;
import com.github.obelieve.utils.ActivityUtil;
import com.github.obelieve.utils.others.CustomSmartRefreshHeader;
import com.github.obelieve.utils.others.TestImageLoader;
import com.news.mockapi.MockApiInterceptor;
import com.previewlibrary.ZoomMediaLoader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zxy.frame.application.BaseApplication;
import com.zxy.frame.dialog.CommonDialog;
import com.zxy.frame.net.ApiErrorCode;
import com.zxy.frame.net.ApiService;
import com.zxy.frame.net.ApiServiceException;
import com.zxy.frame.net.ApiServiceExceptionHandle;
import com.zxy.frame.net.HttpUtil;
import com.zxy.frame.net.convert.ApiCustomGsonConverterFactory;
import com.zxy.frame.net.download.DownloadInterface;
import com.zxy.frame.utils.LogInterceptor;
import com.zxy.frame.utils.SPUtil;
import com.zxy.frame.utils.ToastUtil;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by Admin
 * on 2020/8/27
 */
public class App extends BaseApplication {

    private static ServiceInterface mServiceInterface;
    private List<Activity> mActivities = new LinkedList<>();

    public static ServiceInterface getServiceInterface() {
        return mServiceInterface;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SystemValue.init(this);
        CacheRepository.getInstance().initUserEntity();
        SPUtil.init(this,"App");
        ToastUtil.init(this);
        ZoomMediaLoader.getInstance().init(new TestImageLoader());
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new CustomSmartRefreshHeader(context);
            }
        });
        initNet();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                mActivities.add(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                mActivities.remove(activity);
            }
        });
    }

    public void finishActivity() {
        finishActivity(1);
    }

    public void finishActivity(int pop) {
        for (int i = mActivities.size() - 1; i >= 0; i--) {
            if (pop > 0) {
                mActivities.get(i).finish();
                pop--;
            } else {
                return;
            }
        }
    }

    private void initNet() {
        ApiServiceExceptionHandle.setApiExtendRespondThrowableListener(new ApiServiceExceptionHandle.ApiExtendRespondThrowableListener() {
            @Override
            public void defHandleException(Activity activity, ApiServiceException ex, int window, int toast) {
                if (window == 1 | window == 202) {
                    switch (ex.code) {
                        case ApiErrorCode.CODE_TOKEN_ERROR:
                            if (activity != null && !activity.isFinishing()) {
                                ActivityUtil.gotoLoginDialogActivity(activity);
                                ex.isProcessed = true;
                            }
                            break;
                        case ApiErrorCode.CODE_NOBINGD_PHONE:
                            if (activity != null && !activity.isFinishing()) {
                                new CommonDialog(activity)
                                        .setContent(ex.message)
                                        .setNegativeButton("再看看", null)
                                        .setPositiveButton("去绑定", dialog1 -> {
                                            dialog1.dismiss();
//                                            LoginTypeActivity.startSettingsBindPhone(activity); //TODO 跳转到 绑定手机页
                                        })
                                        .show();
                            }
                            ex.isProcessed = true;
                            break;
                        case ApiErrorCode.CODE_ACCOUNT_BAN:
                            if (activity != null && !activity.isFinishing()) {
//                                new AccountBanDialog(activity)
//                                        .setBanTitle(ex.message)
//                                        .show();
                            }
                            ex.isProcessed = true;
                            break;
                    }
                } else if (toast == 1) {
                    ToastUtil.show(ex.message);
                    ex.isProcessed = true;
                }
            }
        });

        HttpUtil httpUtil = HttpUtil.build().baseUrl(ServiceInterface.BASE_URL)
                .addInterceptor(new HttpInterceptor());
        if (BuildConfig.DEBUG) {
            httpUtil.addInterceptor(new LogInterceptor());
        }
        if (BuildConfig.IS_MOCK_API) {
            httpUtil.addInterceptor(new MockApiInterceptor(App.getContext(),"api/","api",".json"));
        }
        mServiceInterface = httpUtil
                .addConverterFactory(ApiCustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .create(ServiceInterface.class);
        ApiService.setDownloadInterface(new DownloadInterface() {
            @Override
            public Observable<ResponseBody> downloadFile(String downParam, String fileUrl) {
                return mServiceInterface.downloadFile(downParam, fileUrl);
            }
        });
    }
}
