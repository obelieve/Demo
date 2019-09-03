package com.zxy.demo.utils.generator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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
        bitmap = RouteMapUtil.drawIntervalLine(bitmap, 10);
        bitmap = RouteMapUtil.changeToNewBitmap(surface, 10, bitmap, Color.parseColor("#713A54"));
        saveNewBitmapToFile(RouteMapUtil.getContext(), bitmap);
//        saveDataToFile(surface);
//        saveJsonToFile();
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
        floorInfo.setFloor_info_no_route_color("#713A54");
        floorInfo.setNo_route_rgbs(new String[]{"#FFFFFF"});
        floorInfo.setRoute_r(255);
        floorInfo.setRoute_g(255);
        floorInfo.setRoute_b(255);
        floorInfo.setRoute_rgb_bias(0);
        floorInfo.setRatio(0.9f);
        floorInfo.setZoom_size(10);
        List<List<Integer>> noRouteList = new ArrayList<>();
        noRouteList.add(Arrays.asList(91, 122));
        noRouteList.add(Arrays.asList(92, 122));
        noRouteList.add(Arrays.asList(91, 123));
        noRouteList.add(Arrays.asList(92, 123));
        noRouteList.add(Arrays.asList(139, 108));
        noRouteList.add(Arrays.asList(41, 112));
        noRouteList.add(Arrays.asList(42, 112));
        noRouteList.add(Arrays.asList(41, 113));
        noRouteList.add(Arrays.asList(83, 109));
        noRouteList.add(Arrays.asList(81, 100));
        noRouteList.add(Arrays.asList(82, 100));
        noRouteList.add(Arrays.asList(83, 100));
        noRouteList.add(Arrays.asList(91, 82));
        noRouteList.add(Arrays.asList(91, 83));
        noRouteList.add(Arrays.asList(166, 50));
        noRouteList.add(Arrays.asList(193, 82));
        noRouteList.add(Arrays.asList(193, 83));
        noRouteList.add(Arrays.asList(181, 99));
        noRouteList.add(Arrays.asList(181, 100));
        noRouteList.add(Arrays.asList(215, 70));
        noRouteList.add(Arrays.asList(223, 68));
        noRouteList.add(Arrays.asList(223, 69));
        noRouteList.add(Arrays.asList(59, 70));
        noRouteList.add(Arrays.asList(69, 128));
        floorInfo.setNo_route_list(noRouteList);

        Map<String, List<Integer>> map = new HashMap<>();
        map.put("拍照区", Arrays.asList(166, 50));
        map.put("中国建设银行", Arrays.asList(91, 123));
        map.put("母婴室", Arrays.asList(215, 70));
        map.put("便民服务区", Arrays.asList(83, 109));
        map.put("不动产登记综合服务区1", Arrays.asList(83, 100));
        map.put("住房公积金综合服务区", Arrays.asList(193, 83));
        map.put("前台", Arrays.asList(139, 108));
        map.put("不动产登记综合服务区2", Arrays.asList(91, 82));
        map.put("不动产登记综合服务区3", Arrays.asList(181, 100));
        map.put("左边厕所", Arrays.asList(41, 112));
        map.put("电梯1", Arrays.asList(59, 70));
        map.put("入口", Arrays.asList(156, 127));
        map.put("自助服务区", Arrays.asList(69, 128));
        map.put("右边厕所", Arrays.asList(223, 68));
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
