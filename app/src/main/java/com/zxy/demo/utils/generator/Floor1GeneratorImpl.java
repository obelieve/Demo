package com.zxy.demo.utils.generator;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.zxy.demo.floor.FloorInfo;
import com.zxy.demo.floor.FloorMap;
import com.zxy.demo.utils.RouteMapUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxy on 2019/09/02.
 */
public class Floor1GeneratorImpl implements FloorGenerator {


    @Override
    public void saveToFile() {
        long start = System.currentTimeMillis();
        Bitmap bitmap = RouteMapUtil.getBitmap(RouteMapUtil.getAssetsFile("f1.jpg"));
        //231,240,245
        int[][] surface = RouteMapUtil.changeToArray(bitmap, 255, 255, 255, 0, null, 0.9f, 10);
        surface[36][19] = 0;//消毒柜
        surface[15][19] = 0;//抽屉
        surface[43][25] = 0;
        surface[43][26] = 0;//推拉门
        surface[42][26] = 0;
        surface[50][17] = 0;//冰箱
//        bitmap = RouteMapUtil.drawIntervalLine(bitmap, 10);
//        bitmap = RouteMapUtil.overlayNoRouteAreaToBitmap(bitmap, surface, 10, Color.parseColor("#f2c3c3c3"));
//        saveNewBitmapToFile(RouteMapUtil.getContext(), bitmap);
        saveDataToFile(surface);
        saveJsonToFile();
        Log.e("TTT", "生成:" + (System.currentTimeMillis() - start));
    }

    /**
     * _info.jpg文件生成
     *
     * @param context
     * @param bitmap
     * @throws FileNotFoundException
     */
    @Override
    public void saveNewBitmapToFile(Context context, Bitmap bitmap) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(context.getExternalFilesDir(null) + "/f1_info.jpg");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * .data 文件生成
     *
     * @param surface
     */
    @Override
    public void saveDataToFile(int[][] surface) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(RouteMapUtil.getContext().getExternalFilesDir(null) + "/f1.data"));
            FloorMap floorMap = new FloorMap(surface);
            out.writeObject(floorMap);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * .json文件生成
     */
    @Override
    public void saveJsonToFile() {
        FloorInfo floorInfo = new FloorInfo();
        floorInfo.setWidth(600);
        floorInfo.setHeight(334);
        floorInfo.setFloor_filename("f1.jpg");
        floorInfo.setFloor_data_filename("f1.data");
        floorInfo.setFloor_info_filename("f1_info.jpg");
        floorInfo.setFloor_info_no_route_color("#f2c3c3c3");
        floorInfo.setNo_route_rgbs(null);
        floorInfo.setRoute_r(255);
        floorInfo.setRoute_g(255);
        floorInfo.setRoute_b(255);
        floorInfo.setRoute_rgb_bias(0);
        floorInfo.setRatio(0.9f);
        floorInfo.setZoom_size(10);
        List<List<Integer>> noRouteList = new ArrayList<>();

        noRouteList.add(Arrays.asList(36, 19));//消毒柜
        noRouteList.add(Arrays.asList(15, 19));//抽屉
        noRouteList.add(Arrays.asList(43, 25));//推拉门
        noRouteList.add(Arrays.asList(43, 26));//推拉门 *
        noRouteList.add(Arrays.asList(42, 26));//推拉门
        noRouteList.add(Arrays.asList(50, 17));//冰箱
        floorInfo.setNo_route_list(noRouteList);

        Map<String, List<Integer>> map = new HashMap<>();
        map.put("消毒柜", Arrays.asList(36, 19));
        map.put("抽屉", Arrays.asList(15, 19));
        map.put("推拉门", Arrays.asList(43, 26));
        map.put("冰箱位", Arrays.asList(50, 17));
        floorInfo.setPlace_map(map);
        Gson gson = new Gson();
        String json = gson.toJson(floorInfo, FloorInfo.class);
        try {
            FileWriter out = new FileWriter(RouteMapUtil.getContext().getExternalFilesDir(null) + "/f1.json");
            out.write(json);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
