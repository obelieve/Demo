package com.zxy.demo.utils;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.zxy.demo.generator.model.MapData;
import com.zxy.demo.generator.model.MapInfo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by zxy on 2019/09/06.
 */
public class MapStreamUtil {

    private static Gson sGson = new Gson();

    /**
     * 读取地图映射数据
     *
     * @param in
     * @return
     */
    public static int[][] readMapData(InputStream in) {
        int[][] array = null;
        try {
            ObjectInputStream objectIn = new ObjectInputStream(in);
            array = ((MapData) objectIn.readObject()).getSurface();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return array;
    }

    /**
     * 读取地图信息
     *
     * @param in
     * @return
     */
    public static MapInfo readMapInfo(InputStream in) {
        return sGson.fromJson(new BufferedReader(new InputStreamReader(in)), MapInfo.class);
    }

    /**
     * 写入地图映射数据
     *
     * @param surface
     * @param savePath
     */
    public static void writeMapData(int[][] surface, String savePath) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(savePath));
            MapData mapData = new MapData(surface);
            out.writeObject(mapData);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 写入地图信息
     *
     * @param info
     * @param savePath
     */
    public static void writeMapInfo(MapInfo info, String savePath) {
        String json = sGson.toJson(info, MapInfo.class);
        FileWriter out = null;
        try {
            out = new FileWriter(savePath);
            out.write(json);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 写入bitmap
     *
     * @param bitmap
     * @param savePath
     */
    public static void writeBitmap(Bitmap bitmap, String savePath) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(savePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
