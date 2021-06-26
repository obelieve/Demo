package com.zxy.demo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface ServiceInterface {

    String BASE_URL = "http://192.168.2.102:1090";

    @GET("/")
    Call<ResponseBody> get(@Query("name")String name, @Query("value")String value);
}
