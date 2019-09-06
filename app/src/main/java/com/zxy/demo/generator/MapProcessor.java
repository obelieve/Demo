package com.zxy.demo.generator;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.zxy.demo.algorithm.AStarAlgorithm;
import com.zxy.demo.generator.model.DrawRouteConfig;
import com.zxy.demo.generator.model.MapData;
import com.zxy.demo.generator.model.MapInfo;
import com.zxy.demo.utils.RouteMapUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by zxy on 2019/09/06.
 */
public class MapProcessor {

    private MapInfo mMapInfo;
    private MapData mMapData;

    public MapProcessor(MapInfo mapInfo) {
        mMapInfo = mapInfo;
    }

    public MapInfo getMapInfo() {
        return mMapInfo;
    }

    public MapData getMapData() {
        return mMapData;
    }

    public void setMapData(MapData mapData) {
        mMapData = mapData;
    }

    /**
     * 生成楼层相关信息的图片
     *
     * @param bitmap   原图
     * @param filePath 保存路径
     */
    public void genOverlayMapFile(Bitmap bitmap, String filePath) {
        if (mMapData == null || mMapData.getSurface() == null) return;
        int[][] surface = mMapData.getSurface();
        int zoomSize = mMapInfo.getZoomSize();
        int noRouteColor = Color.parseColor("#FF3B00");
        bitmap = RouteMapUtil.drawIntervalLine(bitmap, zoomSize);
        bitmap = RouteMapUtil.changeToNewBitmap(surface, zoomSize, bitmap, noRouteColor);
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
    public Bitmap genMapRouteBitmap(Bitmap bitmap, List<AStarAlgorithm.Node> nodes, DrawRouteConfig config) {
        return RouteMapUtil.drawSurfaceWholeRouting(bitmap, nodes, mMapInfo.getZoomSize(), config);
    }

    /**
     * 生成楼层路线节点集合
     *
     * @param startPosition 起点
     * @param endPosition   终点
     * @return 节点集合
     */
    @SuppressWarnings({"unchecked", "WeakerAccess"})
    public List<AStarAlgorithm.Node> genMapRouteNodes(String startPosition, String endPosition) {
        if (mMapData == null || mMapData.getSurface() == null) {
            return Collections.EMPTY_LIST;
        }
        int[][] surface = mMapData.getSurface();
        Map<String, List<Integer>> positions = mMapInfo.getLocationMapping();
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
        return nodes;
    }
}
