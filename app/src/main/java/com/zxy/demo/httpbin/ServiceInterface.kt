package com.zxy.demo.httpbin

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

/**
 * Created by Admin
 * on 2020/8/13
 */
interface ServiceInterface {

    companion object {
        var BASE_URL = "http://www.httpbin.org/"
    }

    @Streaming
    @GET
    fun downloadFile(
        @Header("RANGE") downParam: String,
        @Url fileUrl: String
    ): Call<ResponseBody>

    @GET("/get")
    fun get(@Query("name")name:String,@Query("content")content: String): Call<HttpBinResponse>

    @GET("/get")
    fun get(@QueryName name:String): Call<ResponseBody>

    @POST("/post")
    fun post(): Call<ResponseBody>

    @POST("/post")
    @FormUrlEncoded
    fun post(@Field("name") name:String,@Field("content") content:String): Call<ResponseBody>

    @POST("/post")
    @Multipart
    fun post(@Part("va") va:RequestBody,@Part("name\"; filename=\"name.png")name: File, @Part("name2\"; filename=\"name2.png")name2: File): Call<ResponseBody>

    @PATCH("/patch")
    @FormUrlEncoded
    fun patch(@Field("name") name:String,@Field("content") content:String): Call<ResponseBody>

    @PUT("/put")
    @FormUrlEncoded
    fun put(@Field("name") name:String,@Field("content") content:String): Call<ResponseBody>

    @DELETE("/delete")
    fun delete(): Call<ResponseBody>
}
