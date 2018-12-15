package com.zxy.im.http;

import android.text.TextUtils;

import com.zxy.frame.http.OkHttpUtil;
import com.zxy.frame.utility.SecretUtil;
import com.zxy.frame.utility.UContext;
import com.zxy.im.R;
import com.zxy.im.model.BaseModel;
import com.zxy.im.model.GetTokenResponse;
import com.zxy.im.model.InitModel;
import com.zxy.im.model.LoginModel;
import com.zxy.im.model.LoginResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * Created by zxy on 2018/12/12 15:42.
 */

public class HttpInterface
{
    private static final String SERVER_URL = "http://api.im.fanwe.cn";

    public static void requestGetToken(String userId, String name, String portraitUri, OkHttpUtil.MainCallback<GetTokenResponse> callback)
    {
        final String url = "http://api.cn.ronghub.com/user/getToken.json";
        final String appKey = UContext.getContext().getString(R.string.app_key);
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
        OkHttpUtil.getInstance().requestHttp(request, callback);
    }

    public static void requestLogin(String mobile, String password, OkHttpUtil.MainCallback<LoginResponse> callback)
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

    public static void requestInit(OkHttpUtil.MainCallback<InitModel> callback)
    {
        OkHttpUtil.getInstance().get(SERVER_URL + "/system/get_config", callback);
    }

    /**
     * 账号登录
     */
    public static void requestLoginIM(String mobile, String password, OkHttpUtil.MainCallback<LoginModel> callback)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("password", password);
        OkHttpUtil.getInstance().postForm(SERVER_URL + "/login", map, callback);
    }

    /**
     * 发送验证码
     *
     * @param access_token 【非注册时使用】账户安全操作的access_token，当此值为空时，需要用户登录并且从登录用户信息中获取发送的手机号码信息
     * @param mobile       【注册，登录， 找回密码时使用】手机号码
     * @param country_code 【注册，登录， 找回密码时使用】手机号码国家区号，默认为0086
     * @param scene        【可选】应用场景，如：login, register, forget等，等于register表示注册
     */
    public static void requestSendSMS(String access_token, String mobile, String country_code, String scene, OkHttpUtil.MainCallback<BaseModel> callback)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);
        if (!TextUtils.isEmpty(access_token))
            map.put("access_token", access_token);
        if (!TextUtils.isEmpty(country_code))
            map.put("country_code", country_code);
        if (!TextUtils.isEmpty(scene))
            map.put("scene", scene);
        OkHttpUtil.getInstance().postForm(SERVER_URL + "/send_sms_verify", map, callback);
    }
}
