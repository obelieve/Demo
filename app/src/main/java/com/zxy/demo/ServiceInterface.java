package com.zxy.demo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by zxy on 2019/08/06.
 */
public interface ServiceInterface {
    String BASE_URL = "http://api.test.2048.com/";

    @POST("api/get_tab")
    Call<ResponseBody> get_tab();
}
