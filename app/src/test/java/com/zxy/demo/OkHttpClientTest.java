package com.zxy.demo;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;

public class OkHttpClientTest {

    public static void main(String[] args) {
        testPost();
    }

    public static void testPost(){
        OkHttpClient client = new OkHttpClient();
        Request.Builder request = new Request.Builder().post(Util.EMPTY_REQUEST)
                .url("http://api.dev.2048.com/api/get_user_info");
        request.addHeader("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9hcGkuZGV2LjIwNDguY29tXC9cL2FwaVwvcmVnX29yX2xvZyIsImlhdCI6MTU4NDkzMzQyNywiZXhwIjoxNTg1NTM4MjI3LCJuYmYiOjE1ODQ5MzM0MjcsImp0aSI6Iks0cjN3SGJraDhuUXZnUmoiLCJzdWIiOjI0NCwicHJ2IjoiMjNiZDVjODk0OWY2MDBhZGIzOWU3MDFjNDAwODcyZGI3YTU5NzZmNyJ9.iDXdSi6JmV0ehO-PS_eo7Uvc4o8EXUvvhT7bxc3Nb84");
        request.addHeader("udidcode","2dc50da0-113a-4a0a-982f-0378ccfc3534");
        request.addHeader("system","PCT-AL10;0");
        request.addHeader("Accept","application/x.2048.v2+json");
        request.addHeader("channel","_2048android");
        request.addHeader("version","1.6.2");
        request.addHeader("platform","android");
        client.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = null;
                try {
                    json = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final BaseResponse baseResponse = new BaseResponse();
                if (jsonObject.has("code")){
                    baseResponse.setCode(jsonObject.optInt("code"));
                }else if (jsonObject.has("status")){
                    baseResponse.setCode(jsonObject.optInt("status"));
                }
                baseResponse.setMsg(jsonObject.optString("message"));
                baseResponse.setData(jsonObject.optString("data"));
                System.out.println("请求响应response = str:"+baseResponse);
            }
        });
    }

}
