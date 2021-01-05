package com.zxy.mockapi;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
    private static final String FILE_SUFFIX = ".txt";
    private static String sFileDir;
    private static String sBaseUrl;
    private static boolean sWriteOrRead;

    public static void init(String fileDir, String baseUrl, boolean writeOrRead) {
        sFileDir = fileDir;
        sBaseUrl = baseUrl;
        sWriteOrRead = writeOrRead;
    }

    public static boolean isWriteOrRead() {
        return sWriteOrRead;
    }

    /**
     * 是否有数据
     *
     * @param url
     * @return
     */
    public static Boolean existData(String url) {
        String tag = getMockApiDataTag(url);
        if (TextUtils.isEmpty(tag)) {
            return false;
        }
        return new File(sFileDir, tag).exists();
    }


    public static Boolean putData(String url, String data) {
        String tag = getMockApiDataTag(url);
        boolean isSuccess = false;
        if (TextUtils.isEmpty(tag)) {
            return false;
        }
        File file = new File(sFileDir, tag);
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(data.getBytes());
            out.flush();
            out.close();
            isSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * 获取模拟数据
     *
     * @param url
     * @return
     */
    public static String getData(String url) {
        String tag = getMockApiDataTag(url);
        if (TextUtils.isEmpty(tag)) {
            return null;
        }
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(new File(sFileDir, tag));
            return getString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取URL的api标识
     *
     * @param url
     * @return 不为空时，存在模拟数据。null时，不存在
     */
    private static String getMockApiDataTag(String url) {
        if (!TextUtils.isEmpty(url)) {
            url = url.replace(sBaseUrl, "");
            int start = url.indexOf("/");
            if (start != -1) {
                String tag = url.substring(start);
                tag = tag.replace("/", "$");
                return tag + FILE_SUFFIX;
            }
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
