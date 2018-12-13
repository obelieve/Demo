package com.zxy.im.http;

import com.zxy.frame.http.OkHttpUtil;
import com.zxy.frame.utility.SecretUtil;
import com.zxy.im.R;
import com.zxy.im.application.App;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * Created by zxy on 2018/12/12 15:42.
 */

public class HttpInterface
{

    public static void requestGetToken(String userId, String name, String portraitUri, OkHttpUtil.MainCallback callback)
    {
        final String url = "http://api.cn.ronghub.com/user/getToken.json";
        final String appKey = App.getAppContext().getString(R.string.app_key);
        final String nonce = String.valueOf(Math.random() * 1000000);
        final String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        final String signature = SecretUtil.SHA1("UqyuBg4necCG" + nonce + timestamp);

        FormBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .add("name", name)
                .add("portraitUri", portraitUri)
                .build();
        Request request = new Request.Builder().url(url)
                .addHeader("App-Key", appKey)
                .addHeader("Nonce", nonce)
                .addHeader("Timestamp", timestamp)
                .addHeader("Signature", signature)
                .post(formBody).build();
        OkHttpUtil.getInstance().getOkHttpClient().newCall(request).enqueue(callback);
    }

    public static void requestLogin(String mobile, String password, OkHttpUtil.MainCallback callback)
    {
        final JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("region", "86");
            jsonObject.put("phone", mobile);
            jsonObject.put("password", password);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        OkHttpUtil.getInstance().postJson("http://api.sealtalk.im/user/login",jsonObject.toString(),callback);
    }
}
