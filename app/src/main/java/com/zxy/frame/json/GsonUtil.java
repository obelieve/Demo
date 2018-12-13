package com.zxy.frame.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Administrator on 2017/10/11.
 */

class GsonUtil implements IJson
{
    private Gson mGson = new Gson();

    @Override
    public String parseObject(Object obj)
    {
        return mGson.toJson(obj);
    }

    @Override
    public <T> T parseJson(String json, Class<T> tClass)
    {
        return mGson.fromJson(json, tClass);
    }

    @Override
    public <T> List<T> parseJSONArray(String json, Class<T> tClass)
    {
        return mGson.fromJson(json, new TypeToken<List<T>>()
        {
        }.getType());
    }
}
