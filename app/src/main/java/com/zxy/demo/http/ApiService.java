package com.zxy.demo.http;

import com.zxy.demo.App;
import com.zxy.demo.entity.SquarePostEntity;
import com.zxy.demo.entity.VersionUpdateEntity;
import com.zxy.frame.net.BaseApiService;
import com.zxy.frame.net.BaseResponse;
import com.zxy.frame.net.download.DownloadInterface;
import com.zxy.frame.net.download.DownloadInterfaceImpl;

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

    @Override
    public Call<ResponseBody> get_tab() {
        return mServiceInterface.get_tab();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<BaseResponse<SquarePostEntity>> square_post(int page) {
        return mServiceInterface.square_post(page).compose(io_main()).compose(handleResult());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable<BaseResponse<VersionUpdateEntity>> version_check() {
        return mServiceInterface.version_check().compose(io_main()).compose(handleResult());
    }
}
