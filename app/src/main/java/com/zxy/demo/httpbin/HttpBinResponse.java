package com.zxy.demo.httpbin;

import java.util.Map;

public class HttpBinResponse {

    private String message;
    private int code;
    private Entity entity;
    private String data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static class Entity{
        private Object args;
        private String data;
        private Map<String,String> files;
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

        public Map<String,String> getFile() {
            return files;
        }

        public void setFile(Map<String,String> file) {
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

        @Override
        public String toString() {
            return "Data{" +
                    "args=" + args +
                    ", data='" + data + '\'' +
                    ", files=" + files +
                    ", form=" + form +
                    ", headers=" + headers +
                    ", json='" + json + '\'' +
                    ", origin='" + origin + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
