package com.zxy.frame.json;

import java.util.List;

/**
 * Created by zxy on 2018/8/10 09:10.
 */

interface IJson
{

    String parseObject(Object obj);

    <T> T parseJson(String json, Class<T> tClass);

    <T> List<T> parseJSONArray(String json, Class<T> tClass);
}
