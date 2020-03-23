package com.zxy.demo;

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
//        if (t == null) {
//            t = MGson.newGson().fromJson(data, c);
//        }
        return t;
    }

    public String getDataJson() {
        return data;
    }

    public void setData(String json) {
        this.data = json;
    }

    public boolean isSuccess() {
        return code == ErrorCode.CODE_OK;
    }

    public boolean isTokenError() {
        return code == ErrorCode.CODE_TOKEN_ERROR;
    }

    public static class ErrorCode{

        public static final int CODE_OK = 200;
        public static final int CODE_TOKEN_ERROR = 201;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                ", t=" + t +
                '}';
    }
}

