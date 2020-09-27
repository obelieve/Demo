package com.zxy.frame.net;

public class ApiServiceException extends RuntimeException {

    public int code;
    public String message;
    public String data;
    public int toast;
    public int window;
    public boolean isProcessed;

    public ApiServiceException(String message, int code) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ApiServiceException(Throwable cause, int code, String message) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public ApiServiceException(String message, int code, String data, int toast, int window) {
        super(message);
        this.message = message;
        this.code = code;
        this.data = data;
        this.toast = toast;
        this.window = window;
    }
}
