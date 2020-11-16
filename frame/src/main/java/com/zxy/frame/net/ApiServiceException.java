package com.zxy.frame.net;

public class ApiServiceException extends RuntimeException {

    private int code;
    private String data;
    private int toast;
    private int window;
    private boolean isProcessed;

    public ApiServiceException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    public ApiServiceException(Throwable cause, int code, String msg) {
        super(msg, cause);
        this.code = code;
    }

    public ApiServiceException(String msg, int code, String data, int toast, int window) {
        super(msg);
        this.code = code;
        this.data = data;
        this.toast = toast;
        this.window = window;
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

    public int getToast() {
        return toast;
    }

    public void setToast(int toast) {
        this.toast = toast;
    }

    public int getWindow() {
        return window;
    }

    public void setWindow(int window) {
        this.window = window;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }
}
