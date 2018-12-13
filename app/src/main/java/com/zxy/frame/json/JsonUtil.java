package com.zxy.frame.json;

import java.util.List;

/**
 * Created by zxy on 2018/8/10 09:07.
 */

public class JsonUtil
{
    private static IJson sJson = new GsonUtil();

    public static void init(JsonType type)
    {
        switch (type)
        {
            case GSON:
                sJson = new GsonUtil();
                break;
        }
    }

    public static <T> T parseJson(String json, Class<T> tClass)
    {
        return sJson.parseJson(json, tClass);
    }

    public static <T> List<T> parseJSONArray(String json, Class<T> tClass)
    {
        return sJson.parseJSONArray(json, tClass);
    }


    public static String parseObject(Object obj)
    {
        return sJson.parseObject(obj);
    }

    public enum JsonType
    {
        GSON
    }
}
