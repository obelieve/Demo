package com.zxy.demo.generator;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import com.zxy.demo.algorithm.AStarAlgorithm;
import com.zxy.demo.generator.configurator.MapConfigurator;
import com.zxy.demo.generator.model.MapInfo;
import com.zxy.demo.utils.MapStreamUtil;
import com.zxy.demo.utils.RouteLog;
import com.zxy.demo.utils.RouteMapUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地图生成器
 * Created by zxy on 2019/09/05.
 */
public class MapGenerator {
    
    private static final String TAG = MapGenerator.class.getSimpleName();
    
    private static final String MAP_DATA_FILE_SUFFIX = ".data";
    private static final String MAP_INFO_FILE_SUFFIX = ".json";
    private static final String MAP_BITMAP_FILE_SUFFIX = "_bitmap.jpg";
    private static final String MAP_ROUTE_FILE_SUFFIX = "_route.jpg";
    private static final String MAP_BITMAP_FILE_NO_ROUTE_COLOR = "#FF3B00";
    private static final String MAP_BITMAP_FILE_LOCATION_COLOR = "#000000";

    public static void create(MapConfigurator configurator) {
        long startTime = System.currentTimeMillis();
        int routeRGB = Color.parseColor(configurator.getRouteColor());
        int r = Color.red(routeRGB);
        int g = Color.green(routeRGB);
        int b = Color.blue(routeRGB);
        Bitmap bitmap = configurator.getOriginalBitmap();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int rgbBias = configurator.getBuildConfigurator().getRgbBias();
        int[] noRouteRGBs = configurator.getBuildConfigurator().getNoRouteRGBs();
        float ratio = configurator.getBuildConfigurator().getRoutePercent();
        int zoomSize = configurator.getBuildConfigurator().getZoomSize();
        int[][] surface = RouteMapUtil.changeToArray(bitmap, r, g, b, rgbBias, noRouteRGBs, ratio, zoomSize);
        Map<String, List<Integer>> mapping = new HashMap<>();
        List<AStarAlgorithm.Node> nodes = new ArrayList<>();
        for (String key : configurator.getLocation().keySet()) {
            List<Integer> value = configurator.getLocation().get(key);
            if (value == null)
                continue;
            int[] location = RouteMapUtil.changeToArrayXY(zoomSize, value.get(0), value.get(1));
            nodes.add(new AStarAlgorithm.Node(location[0], location[1]));
            mapping.put(key, Arrays.asList(location[0], location[1]));
            configurator.getBuildConfigurator().openRoute(surface, location[0], location[1]);
        }
        configurator.getBuildConfigurator().resetRoute(surface);
        //生成.data文件，地图映射文件
        MapStreamUtil.writeMapData(surface, RouteMapUtil.getContext().getExternalFilesDir(null) + File.separator + configurator.getUniqueMapName() + MAP_DATA_FILE_SUFFIX);
        MapInfo mapInfo = new MapInfo();
        mapInfo.setWidth(width);
        mapInfo.setHeight(height);
        mapInfo.setLocationMapping(mapping);
        mapInfo.setZoomSize(zoomSize);
        //生成.json文件，地图信息文件
        MapStreamUtil.writeMapInfo(mapInfo, RouteMapUtil.getContext().getExternalFilesDir(null) + File.separator + configurator.getUniqueMapName() + MAP_INFO_FILE_SUFFIX);
        RouteLog.log(TAG, "生成.json文件，地图信息文件:" + (System.currentTimeMillis() - startTime));
        Bitmap originalBitmap = bitmap;//原有的Bitmap
        bitmap = RouteMapUtil.drawIntervalLine(bitmap, zoomSize);
        bitmap = RouteMapUtil.changeToNewBitmap(surface, zoomSize, bitmap, Color.parseColor(MAP_BITMAP_FILE_NO_ROUTE_COLOR));
        bitmap = RouteMapUtil.drawSurfaceNodes(bitmap, nodes, zoomSize, 1, Color.parseColor(MAP_BITMAP_FILE_LOCATION_COLOR), Paint.Style.STROKE);
        //生成_bitmap.jpg文件，地图显示具体路径等信息的图片
        MapStreamUtil.writeBitmap(bitmap, RouteMapUtil.getContext().getExternalFilesDir(null) + File.separator + configurator.getUniqueMapName() + MAP_BITMAP_FILE_SUFFIX);
        RouteLog.log(TAG, "生成_bitmap.jpg文件，地图显示具体路径等信息的图片:" + (System.currentTimeMillis() - startTime));
//        bitmap = originalBitmap;
//        AStarAlgorithm algorithm = new AStarAlgorithm();
//        List<List<AStarAlgorithm.Node>> lists = new ArrayList<>();
//        for (List<Integer> start : mapInfo.getLocationMapping().values()) {
//            for (List<Integer> end : mapInfo.getLocationMapping().values()) {
//                lists.add(algorithm.execute(surface, new AStarAlgorithm.Node(start.get(0), start.get(1)), new AStarAlgorithm.Node(end.get(0), end.get(1))));
//            }
//        }
//        RouteLog.log(TAG, "完成节点搜索:" + (System.currentTimeMillis() - startTime));
//        for (List<AStarAlgorithm.Node> nodeList : lists) {
//            bitmap = RouteMapUtil.drawSurfaceWholeRouting(bitmap, nodeList, zoomSize);
//        }
        //生成_route.jpg文件，地图显示全部路径的图片
        //MapStreamUtil.writeBitmap(bitmap, RouteMapUtil.getContext().getExternalFilesDir(null) + File.separator + configurator.getUniqueMapName() + MAP_ROUTE_FILE_SUFFIX);
        //RouteLog.log(TAG, "生成_route.jpg文件，地图显示全部路径的图片:" + (System.currentTimeMillis() - startTime));
    }
}
