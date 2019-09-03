package com.zxy.demo.floor;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.zxy.demo.algorithm.AStarAlgorithm;
import com.zxy.demo.utils.RouteMapUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by zxy on 2019/08/30.
 */
public class FloorProcessor {

    private FloorInfo mFloorInfo;
    private FloorMap mFloorMap;

    public FloorProcessor(FloorInfo floorInfo) {
        mFloorInfo = floorInfo;
    }

    public FloorInfo getFloorInfo() {
        return mFloorInfo;
    }

    public FloorMap getFloorMap() {
        return mFloorMap;
    }

    public void setFloorMap(FloorMap floorMap) {
        mFloorMap = floorMap;
    }

    /**
     * 楼层二维数组存储到文件
     *
     * @param filePath
     * @param surface
     */
    public static void surfaceSaveToFile(int[][] surface, String filePath) {
        FloorMap mapArray = new FloorMap(surface);
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(filePath)));
            out.writeObject(mapArray);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 从文件中获取楼层二维数组
     * @return
     */
    public static int[][] surfaceFromFile(InputStream in) {
        int[][] array = null;
        try {
            ObjectInputStream objectIn = new ObjectInputStream(in);
            array = ((FloorMap) objectIn.readObject()).getSurface();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return array;
    }

    /**
     * 从Bitmap中，获取楼层二维数组信息
     *
     * @param bitmap 原图
     * @return 二维数组
     */
    public int[][] getFloorSurface(Bitmap bitmap) {
        int r = mFloorInfo.getRoute_r();
        int g = mFloorInfo.getRoute_g();
        int b = mFloorInfo.getRoute_b();
        int bias = mFloorInfo.getRoute_rgb_bias();
        String[] noRouteRGBStrings = mFloorInfo.getNo_route_rgbs();
        int[] noRouteRGBs = null;
        float ratio = mFloorInfo.getRatio();
        int zoomSize = mFloorInfo.getZoom_size();
        List<List<Integer>> noRouteList = mFloorInfo.getNo_route_list();
        if (noRouteRGBStrings != null && noRouteRGBStrings.length > 0) {
            try {
                noRouteRGBs = new int[noRouteRGBStrings.length];
                for (int i = 0; i < noRouteRGBStrings.length; i++) {
                    noRouteRGBs[i] = Color.parseColor(noRouteRGBStrings[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                noRouteRGBs = null;
            }
        }
        int[][] surface = RouteMapUtil.changeToArray(bitmap, r, g, b, bias, noRouteRGBs, ratio, zoomSize);
        if (noRouteList != null && noRouteList.size() > 0) {
            for (int i = 0; i < noRouteList.size(); i++) {
                if (noRouteList.get(i).size() == 2) {
                    int x = noRouteList.get(i).get(0);
                    int y = noRouteList.get(i).get(1);
                    surface[x][y] = 0;
                }
            }
        }
        return surface;
    }


    /**
     * 生成楼层相关信息的图片
     *
     * @param bitmap   原图
     * @param filePath 保存路径
     */
    public void genFloorInfoFile(Bitmap bitmap, String filePath) {
        if (mFloorMap == null || mFloorMap.getSurface() == null) return;
        int[][] surface = mFloorMap.getSurface();
        int zoomSize = mFloorInfo.getZoom_size();
        String noRouteRegionColor = mFloorInfo.getFloor_info_no_route_color();
        int noRouteColor = Color.parseColor("#FF3B00");
        try {
            noRouteColor = Color.parseColor(noRouteRegionColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bitmap = RouteMapUtil.drawIntervalLine(bitmap, zoomSize);
        bitmap = RouteMapUtil.overlayNoRouteAreaToBitmap(bitmap, surface, zoomSize, noRouteColor);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成楼层路线的Bitmap
     *
     * @param bitmap 原图
     * @param nodes  路线的所有节点
     * @return bitmap
     */
    public Bitmap genFloorRouteBitmap(Bitmap bitmap, List<AStarAlgorithm.Node> nodes) {
        return RouteMapUtil.drawSurfaceWholeRouting(bitmap, nodes, mFloorInfo.getZoom_size());
    }

    /**
     * 生成楼层路线的Bitmap
     *
     * @param bitmap        原图
     * @param startPosition 起点
     * @param endPosition   终点
     * @return
     */
    public Bitmap genFloorRouteBitmap(Bitmap bitmap, String startPosition, String endPosition) {
        return RouteMapUtil.drawSurfaceWholeRouting(bitmap, genFloorRouteNodes(startPosition, endPosition), mFloorInfo.getZoom_size());
    }

    /**
     * 生成楼层路线节点集合
     *
     * @param startPosition 起点
     * @param endPosition   终点
     * @return 节点集合
     */
    @SuppressWarnings({"unchecked", "WeakerAccess"})
    public List<AStarAlgorithm.Node> genFloorRouteNodes(String startPosition, String endPosition) {
        if (mFloorMap == null || mFloorMap.getSurface() == null) {
            return Collections.EMPTY_LIST;
        }
        int[][] surface = mFloorMap.getSurface();
        Map<String, List<Integer>> positions = mFloorInfo.getPlace_map();
        List<Integer> start = positions.get(startPosition);
        List<Integer> end = positions.get(endPosition);
        if (positions == null || !(positions.containsKey(startPosition) && positions.containsKey(endPosition))) {
            return Collections.EMPTY_LIST;
        }
        if (start == null || end == null) {
            return Collections.EMPTY_LIST;
        }
        AStarAlgorithm algorithm = new AStarAlgorithm();
        List<AStarAlgorithm.Node> nodes = algorithm.execute(surface, new AStarAlgorithm.Node(start.get(0), start.get(1)), new AStarAlgorithm.Node(end.get(0), end.get(1)));
        ;
        return nodes;
    }
}
