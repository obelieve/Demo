package com.zxy.demo.http;

import com.zxy.demo.App;
import com.zxy.demo.entity.SquarePostEntity;
import com.zxy.frame.net.BaseResponse;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ApiService implements ServiceInterface {

    private static ApiService mApiService =new ApiService();

    private ServiceInterface mServiceInterface;

    public static ApiService getInstance(){
        return mApiService;
    }

    private ApiService(){
        mServiceInterface = App.getServiceInterface();
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

    private <T> ObservableTransformer<BaseResponse<T>, BaseResponse<T>> handleResult() {
        return new ObservableTransformer<BaseResponse<T>, BaseResponse<T>>() {
            @Override
            public Observable<BaseResponse<T>> apply(@NonNull Observable<BaseResponse<T>> upstream) {
                return upstream.flatMap(new Function<BaseResponse<T>, Observable<BaseResponse<T>>>() {
                    @Override
                    public Observable<BaseResponse<T>> apply(@NonNull BaseResponse<T> tBaseResponse) throws Exception {
                        if (tBaseResponse.getCode()==ErrorCode.SUCCESS) {
                            return createData(tBaseResponse);
                        } else {
                            return Observable.error(new ServerException(tBaseResponse.getMsg(),tBaseResponse.getCode(),tBaseResponse.getDataJson()));
                        }
                    }
                }).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    private <T> Observable<T> createData(final T data) {
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
}
