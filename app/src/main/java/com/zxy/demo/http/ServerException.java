package com.zxy.demo.http;

public class ServerException extends Exception {

    private int code;
    private String data;

    public ServerException(String message, int code, String data) {
        super(message);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
