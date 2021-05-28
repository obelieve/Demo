package com.zxy.demo

import com.obelieve.frame.net.ApiBaseResponse
import io.reactivex.Observable

import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by Admin
 * on 2020/8/13
 */
interface ServiceInterface {

    companion object{
        const val BASE_URL = "https://www.baidu.com"
        const val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC90ZXN0Lm9rc3BvcnRzZXJ2LmNvbVwvYXBpXC9hdXRoXC9tb2JpbGVsb2dpbiIsImlhdCI6MTYyMjE2ODE5MywiZXhwIjoxNjIyNzcyOTkzLCJuYmYiOjE2MjIxNjgxOTMsImp0aSI6ImV6NVp1b25Id3BDZTdXaVMiLCJzdWIiOjI4NCwicHJ2IjoiNTQzOTU1NmFlY2E5M2M1YzJhYjI0OWU3YzY4NzQ1NDAyNGVkZjUzYiJ9.wy_NlC907IxEDbSuq0B83M0e8aDcXcOhPSP0qQ_nN_g"
        /**
         * 钻石充值商品列表
         */
        const val API_DIAMOND_GOOD_LIST = "/api/diamond/goods_list"

        /**
         * 发送支付凭证至服务端
         */
        const val API_PAYMENT_REPORT = "/api/payment/report"
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
    @POST(API_DIAMOND_GOOD_LIST)
    fun getDiamondGoodList() : Observable<ApiBaseResponse<DiamondGoodEntity>>

    @POST(API_PAYMENT_REPORT)
    @FormUrlEncoded
    fun sendPaymentReport( @Field("report_data") verid: String) : Observable<ApiBaseResponse<String>>
}