package com.zxy.demo.generator.model;

import java.util.List;
import java.util.Map;

/**
 * Created by zxy on 2019/09/05.
 */
public class MapInfo {

    private int width;
    private int height;
    private int zoomSize;
    private Map<String, List<Integer>> locationMapping;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getZoomSize() {
        return zoomSize;
    }

    public void setZoomSize(int zoomSize) {
        this.zoomSize = zoomSize;
    }

    public Map<String, List<Integer>> getLocationMapping() {
        return locationMapping;
    }

    public void setLocationMapping(Map<String, List<Integer>> locationMapping) {
        this.locationMapping = locationMapping;
    }

    @Override
    public String toString() {
        return "MapInfo{" +
                "width=" + width +
                ", height=" + height +
                ", zoomSize=" + zoomSize +
                ", locationMapping=" + locationMapping +
                '}';
    }
}
