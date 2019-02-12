package com.zxy.im.database;

import android.net.Uri;

/**
 * Created by zxy on 2019/2/12 16:18.
 */

public class UserInfo
{
    private String id;
    private String name;
    private Uri imgUri;

    public UserInfo(String id, String name, Uri imgUri)
    {
        this.id = id;
        this.name = name;
        this.imgUri = imgUri;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Uri getImgUri()
    {
        return imgUri;
    }

    public void setImgUri(Uri imgUri)
    {
        this.imgUri = imgUri;
    }

    @Override
    public String toString()
    {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imgUri=" + imgUri.getPath() +
                '}';
    }
}
