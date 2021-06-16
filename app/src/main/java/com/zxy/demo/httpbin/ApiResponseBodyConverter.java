package com.zxy.demo.httpbin;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class ApiResponseBodyConverter implements Converter<ResponseBody, HttpBinResponse> {

    @Nullable
    @Override
    public HttpBinResponse convert(ResponseBody value) throws IOException {
        String json = value.string();
        HttpBinResponse response = new HttpBinResponse();
        try{
            response.setCode(1);
            response.setMessage("Success");
            response.setData(json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
}
