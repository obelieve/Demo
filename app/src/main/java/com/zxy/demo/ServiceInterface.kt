package com.zxy.demo

import com.obelieve.frame.net.ApiBaseResponse
import io.reactivex.Observable

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Admin
 * on 2020/8/13
 */
interface ServiceInterface {

    companion object{
        const val BASE_URL = "https://www.baidu.com"
        const val token = ""
    }

    @Streaming
    @GET
    fun downloadFile(
        @Header("RANGE") downParam: String,
        @Url fileUrl: String
    ): Observable<ResponseBody>


    /**
     * 获取钻石购买页数据
     */
    @GET
    fun getBaidu(@Url fileUrl: String) : Call<ResponseBody>
}