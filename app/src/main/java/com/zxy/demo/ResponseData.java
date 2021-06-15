package com.zxy.demo;

import java.util.Map;

public class ResponseData {
    private Object args;
    private String data;
    private Object files;
    private Map<String,String> form;
    private Map<String,String> headers;
    private String json;
    private String origin;
    private String url;

    public Object getArg() {
        return args;
    }

    public void setArg(Object arg) {
        args = arg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Object getFile() {
        return files;
    }

    public void setFile(Object file) {
        files = file;
    }

    public Map<String, String> getForm() {
        return form;
    }

    public void setForm(Map<String, String> form) {
        this.form = form;
    }

    public Map<String, String> getHeader() {
        return headers;
    }

    public void setHeader(Map<String, String> header) {
        headers = header;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
