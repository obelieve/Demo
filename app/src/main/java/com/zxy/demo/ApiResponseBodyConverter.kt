package com.zxy.demo

import com.google.gson.JsonObject
import com.obelieve.frame.net.ApiBaseResponse
import okhttp3.ResponseBody
import retrofit2.Converter

class ApiResponseBodyConverter:Converter<ResponseBody,ApiBaseResponse<*>> {




    override fun convert(value: ResponseBody?): ApiBaseResponse<*>? {
        val response:ApiBaseResponse<*> = ApiBaseResponse<Any>()
        val obj:JsonObject=JsonObject()
        if(obj.has("msg")){
            response.msg = obj.get("msg").asString
        }
        if(obj.has("code")){
            response.code = obj.get("code").asInt
        }
        if(obj.has("data")){
            response.data = obj.get("data").asString
        }
        return response
    }
}
