package com.zxy.frame.net;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class BaseApiService {

    private static final int SUCCESS_CODE = 200;

    protected <T> Observable<T> createData(final T data) {
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


    protected ObservableTransformer io_main() {
        return new ObservableTransformer() {

            @Override
            public ObservableSource apply(@NonNull Observable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    protected <T> ObservableTransformer<BaseResponse<T>, BaseResponse<T>> handleResult() {
        return new ObservableTransformer<BaseResponse<T>, BaseResponse<T>>() {
            @Override
            public Observable<BaseResponse<T>> apply(@NonNull Observable<BaseResponse<T>> upstream) {
                return upstream.flatMap(new Function<BaseResponse<T>, Observable<BaseResponse<T>>>() {
                    @Override
                    public Observable<BaseResponse<T>> apply(@NonNull BaseResponse<T> tBaseResponse) throws Exception {
                        if (tBaseResponse.getCode() == SUCCESS_CODE) {
                            return createData(tBaseResponse);
                        } else {
                            return Observable.error(new BaseApiServiceException(tBaseResponse.getMsg(), tBaseResponse.getCode(), tBaseResponse.getDataJson()));
                        }
                    }
                });
            }
        };
    }
}
