package com.zxy.mall.http;

import com.zxy.frame.net.BaseApiService;
import com.zxy.frame.net.BaseResponse;
import com.zxy.frame.net.download.DownloadInterface;
import com.zxy.frame.net.download.DownloadInterfaceImpl;
import com.zxy.mall.App;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ApiService extends BaseApiService implements ServiceInterface, DownloadInterface {

    private static ApiService mApiService = new ApiService();
    private ServiceInterface mServiceInterface;
    private DownloadInterfaceImpl mDownloadInterface;

    public static ApiService getInstance() {
        return mApiService;
    }

    private ApiService() {
        mServiceInterface = App.getServiceInterface();
        mDownloadInterface = new DownloadInterfaceImpl(this);
    }

    @Override
    public Observable<ResponseBody> downloadFile(String downParam, String fileUrl) {
        return mServiceInterface.downloadFile(downParam,fileUrl);
    }

    public Disposable download(String savePath, String fileUrl, DownloadInterfaceImpl.DownloadCallback callback) {
        return mDownloadInterface.download(savePath, fileUrl, callback);
    }

}
