package com.zxy.demo

import com.obelieve.frame.net.ApiBaseResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class ApiConverterFactory: Converter.Factory() {
    companion object{
        fun create(): ApiConverterFactory {
            return ApiConverterFactory()
        }
    }

    override fun responseBodyConverter(
        type: Type?,
        annotations: Array<out Annotation>?,
        retrofit: Retrofit?
    ): Converter<ResponseBody, ApiBaseResponse<*>>? {
        return ApiResponseBodyConverter()
    }
}
