package com.zxy.im.model;

/**
 * Created by zxy on 2018/12/14 10:46.
 */

public class BaseModel
{
    private int errcode;
    private String errmsg;

    public int getErrcode()
    {
        return errcode;
    }

    public void setErrcode(int errcode)
    {
        this.errcode = errcode;
    }

    public String getErrmsg()
    {
        return errmsg;
    }

    public void setErrmsg(String errmsg)
    {
        this.errmsg = errmsg;
    }
}
