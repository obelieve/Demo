package com.zxy.frame.net;


import com.zxy.frame.net.gson.MGson;


/**
 * 网络返回基类 支持泛型
 */
public class BaseResponse<T>  {
    private int code;
    private String msg;
    private String data;
    private T t;

    public BaseResponse() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData(Class<T> c) {
        if (t == null) {
            t = MGson.newGson().fromJson(data, c);
        }
        return t;
    }

    public String getDataJson() {
        return data;
    }

    public void setData(String json) {
        this.data = json;
    }

//    public boolean isSuccess() {
//        return code == ErrorCode.CODE_OK;
//    }
//
//    public boolean isTokenError() {
//        return code == ErrorCode.CODE_TOKEN_ERROR;
//    }
}
