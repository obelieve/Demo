package com.zxy.demo.http;

import com.zxy.demo.App;
import com.zxy.demo.entity.SquarePostEntity;
import com.zxy.frame.net.BaseApiService;
import com.zxy.frame.net.BaseResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ApiService extends BaseApiService implements ServiceInterface {

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

}
