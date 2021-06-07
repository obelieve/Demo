package com.zxy.demo

import com.obelieve.frame.net.ApiBaseResponse
import com.zxy.demo.entity.LogConfigEntity
import com.zxy.demo.entity.UserInfo
import io.reactivex.Observable
import okhttp3.Request
import okhttp3.RequestBody

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File
import java.util.*

/**
 * Created by Admin
 * on 2020/8/13
 */
interface ServiceInterface {

    companion object{
        var BASE_URL = "https://sm.ms"//"h*t*t*p://*d*e*v*.o*k*s**p*o*r*t*s*e*r*v*.com/".replace("*","")
        const val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9kZXYub2tzcG9ydHNlcnYuY29tXC9hcGlcL2F1dGhcL21vYmlsZWxvZ2luIiwiaWF0IjoxNjIyNTM3MjIwLCJleHAiOjE2MjMxNDIwMjAsIm5iZiI6MTYyMjUzNzIyMCwianRpIjoiNUFuZUxLZnlHM2NrcmcxdiIsInN1YiI6MTYyLCJwcnYiOiI1NDM5NTU2YWVjYTkzYzVjMmFiMjQ5ZTdjNjg3NDU0MDI0ZWRmNTNiIn0.smowM5fORBlmRY8ss_7iqhfzOLRZWUd3Jav2v0GeAnI"
        const val API_USER_INFO = "/api/user/info"
        const val API_MODIFY_USER_INFO = "/api/user/se*ttings"
        var EXTERNAL_URL = "https://s**l*s*-deb*ug.gol*og*st*or*e.co*m/s*ls/c*onf*ig".replace("*","")
    }

    @Streaming
    @GET
    fun downloadFile(
        @Header("RANGE") downParam: String,
        @Url fileUrl: String
    ): Call<ResponseBody>

    @GET
    fun getBaidu(@Url url: String) : Call<ResponseBody>

    //外部URL使用
    //@Headers(RetrofitUrlManager.DOMAIN_NAME_HEADER +"logConfig")
    @FormUrlEncoded
    @POST
    fun getLogstoreConfig(
        @Url url: String,
        @Field("verid") verid: String,
        @Field("appid") appid: String,
        @Field("sign") sign: String,
    ) : Observable<ApiBaseResponse<LogConfigEntity>>

    /**
     * 修改用户信息
     */
    @POST(API_MODIFY_USER_INFO)
    @FormUrlEncoded
    fun modifyUserInfo(
        @Field("opt") opt: String,
        @Field("email") email: String,
        @Field("nickname") nickname: String,
        @Field("avatar") avatar: String
    ): Observable<ApiBaseResponse<String>>

    /**
     * 获取用户信息
     */
    @POST(API_USER_INFO)
    fun getUserInfo(): Observable<ApiBaseResponse<UserInfo>>
}