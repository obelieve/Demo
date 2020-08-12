package com.zxy.frame.net;

public class ApiServiceException extends RuntimeException {

    private int code;
    private String data;

    public ApiServiceException(String message) {
        super(message);
    }

    public ApiServiceException(String message, int code, String data) {
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
