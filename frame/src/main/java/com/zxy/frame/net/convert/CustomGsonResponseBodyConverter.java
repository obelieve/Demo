package com.zxy.frame.net.convert;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.zxy.frame.net.BaseResponse;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by TQ on 2018/5/30.
 */

final class CustomGsonResponseBodyConverter implements Converter<ResponseBody, BaseResponse> {
    private final Gson gson;
    private final TypeAdapter adapter;

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<BaseResponse> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override public BaseResponse convert(ResponseBody value) throws IOException {
        try {
            String json = value.string();
//            if (!json.contains("code")){
//                Log.i("caicai",json);
//                //TODO 尝试解密
//                byte[] res  = Base64.getDecoder().decode(json.getBytes());
//                String desc = new String(DESedeHelper.des3DecodeCBC(res,"BROWSER2017&91FiveBoys@#"), "utf-8");
//                json = desc;
//                Log.i("caicai",desc);
//            }
            JSONObject jsonObject = new JSONObject(json);
            BaseResponse baseResponse = new BaseResponse();
            if (jsonObject.has("code")){
                baseResponse.setCode(jsonObject.optInt("code"));
            }else if (jsonObject.has("status")){
                baseResponse.setCode(jsonObject.optInt("status"));
            }
            baseResponse.setMsg(jsonObject.optString("message"));
            baseResponse.setData(jsonObject.optString("data"));
            return baseResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
