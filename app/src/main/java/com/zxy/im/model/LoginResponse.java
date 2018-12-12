package com.zxy.im.model;

public class LoginResponse
{
    private int code;
    private Result result;

    public void setCode(int code)
    {
        this.code = code;
    }

    public void setResult(Result result)
    {
        this.result = result;
    }

    public int getCode()
    {
        return code;
    }

    public Result getResult()
    {
        return result;
    }

    public static class Result
    {
        private String id;
        private String token;

        public void setId(String id)
        {
            this.id = id;
        }

        public void setToken(String token)
        {
            this.token = token;
        }

        public String getId()
        {
            return id;
        }

        public String getToken()
        {
            return token;
        }
    }
}
