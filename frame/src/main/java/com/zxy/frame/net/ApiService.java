package com.zxy.frame.net;

import com.zxy.frame.net.download.DownloadInterface;
import com.zxy.frame.net.download.DownloadInterfaceImpl;
import com.zxy.frame.net.gson.MGson;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class ApiService {

    private static DownloadInterfaceImpl sDownloadInterfaceImpl;

    public static void setDownloadInterface(DownloadInterface downloadInterface) {
        sDownloadInterfaceImpl = new DownloadInterfaceImpl(downloadInterface);
    }

    public static Observable<ResponseBody> downloadFile(String downParam, String fileUrl) throws ApiServiceException {
        if (sDownloadInterfaceImpl == null) {
            throw new ApiServiceException("need invoke setDownloadInterface(DownloadInterface)!");
        }
        return sDownloadInterfaceImpl.downloadFile(downParam, fileUrl);
    }

    public static Disposable download(String savePath, String fileUrl, DownloadInterfaceImpl.DownloadCallback callback) {
        if (sDownloadInterfaceImpl == null) {
            throw new ApiServiceException("need invoke setDownloadInterface(DownloadInterface)!");
        }
        return sDownloadInterfaceImpl.download(savePath, fileUrl, callback);
    }

    public static <T> Observable<BaseResponse<T>> wrap(Observable<BaseResponse<T>> observable, Class<T> tClass) {
        return observable.compose(io_main()).compose(handleResult(tClass));
    }

    private static <T> Observable<T> createData(final T data) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(data);
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }

    private static ObservableTransformer io_main() {
        return new ObservableTransformer() {

            @Override
            public ObservableSource apply(@NonNull Observable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    private static <T> ObservableTransformer<BaseResponse<T>, BaseResponse<T>> handleResult(Class<T> tClass) {
        return new ObservableTransformer<BaseResponse<T>, BaseResponse<T>>() {
            @Override
            public Observable<BaseResponse<T>> apply(@NonNull Observable<BaseResponse<T>> upstream) {
                return upstream.flatMap(new Function<BaseResponse<T>, Observable<BaseResponse<T>>>() {
                    @Override
                    public Observable<BaseResponse<T>> apply(@NonNull BaseResponse<T> tBaseResponse) throws Exception {
                        if (tBaseResponse.getCode() == ApiStatusCode.SUCCESS_CODE) {
                            try {
                                T t = MGson.newGson().fromJson(tBaseResponse.getData(), tClass);
                                tBaseResponse.setEntity(t);
                                return createData(tBaseResponse);
                            } catch (Exception e) {
                                ApiServiceException exception = new ApiServiceException(e.getMessage(), ApiStatusCode.JSON_SYNTAX_EXCEPTION_CODE, tBaseResponse.getData());
                                exception.setStackTrace(e.getStackTrace());
                                return Observable.error(exception);
                            }
                        } else {
                            return Observable.error(new ApiServiceException(tBaseResponse.getMsg(), tBaseResponse.getCode(), tBaseResponse.getData()));
                        }
                    }
                });
            }
        };
    }
}
