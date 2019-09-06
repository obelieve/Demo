package com.zxy.demo.generator.model;

import com.zxy.demo.generator.configurator.DrawConfigurator;

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
    private DrawRouteConfig config;

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

    public DrawRouteConfig getConfig() {
        return config;
    }

    public void setConfig(DrawRouteConfig config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return "MapInfo{" +
                "width=" + width +
                ", height=" + height +
                ", zoomSize=" + zoomSize +
                ", locationMapping=" + locationMapping +
                ", config=" + config +
                '}';
    }


}
