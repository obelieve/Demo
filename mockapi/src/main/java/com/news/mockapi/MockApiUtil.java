package com.news.mockapi;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Admin
 * on 2020/7/6
 */
public class MockApiUtil {
    private static final String TAG = MockApiUtil.class.getSimpleName();
    private static Context sContext;
    private static final String FILE_SUFFIX = ".json";
    private static StringBuilder SB = new StringBuilder();
    private static String ALL_URL;
    private static String sApiPrefix;

    public static void init(Context context, String apiPrefix, String fileDir, String fileSuffix) {
        sContext = context;
        sApiPrefix = apiPrefix;
        try {
            String[] paths = sContext.getAssets().list(fileDir);
            for (String path : paths) {
                SB.append(apiPrefix + path.replace(fileSuffix, "") + ",");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Assets目录下，文件查找错误：" + e.getMessage());
        }
        ALL_URL = SB.toString();
        Log.e(TAG, "ALL_URL：" + ALL_URL);
    }

    /**
     * 获取URL的api标识
     *
     * @param url
     * @return 不为空时，存在模拟数据。null时，不存在
     */
    public static String getMockApiDataTag(String url) {
        if (!TextUtils.isEmpty(url)) {
            int start = url.indexOf(sApiPrefix);
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
