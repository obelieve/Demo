package com.news.mockapi;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

/**
 * Created by Admin
 * on 2020/7/6
 */
public class MockApiUtil {
    private static Context sContext;
    private static final String FILE_SUFFIX = ".json";
    private static StringBuilder SB = new StringBuilder();
    private static String ALL_URL;

    static {
        Field[] fieldArray = MockApi.class.getDeclaredFields();
        for (Field f : fieldArray) {
            try {
                SB.append(f.get(null) + ",");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        ALL_URL = SB.toString();
    }

    public static void init(Context context) {
        sContext = context;
    }

    /**
     * 获取URL的api标识
     *
     * @param url
     * @return 不为空时，存在模拟数据。null时，不存在
     */
    public static String getMockApiDataTag(String url) {
        if (!TextUtils.isEmpty(url)) {
            int start = url.indexOf(MockApi.API_PREFIX_TAG);
            if (start != -1) {
                String tag = url.substring(start);
                if (MockApiUtil.ALL_URL.contains(tag)) {
                    return tag;
                }
            }
        }
        return null;
    }

    /**
     * 获取模拟数据
     *
     * @param name
     * @return
     */
    public static String getData(String name) {
        InputStream inputStream = null;
        try {
            inputStream = sContext.getAssets().open(name + FILE_SUFFIX);
            return getString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
