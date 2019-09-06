package com.zxy.demo.generator.model;

import com.zxy.demo.generator.configurator.DrawConfigurator;

/**
 * 绘制路线配置
 * Created by zxy on 2019/09/06.
 */
public class DrawRouteConfig {

    private String startFileName;
    private String endFileName;
    private int pointLeftOffset;
    private int pointTopOffset;
    private int routeWidth;
    private int routeColor;
    private boolean zoom;
    private int zoomWH;

    public DrawRouteConfig() {
    }

    public DrawRouteConfig(DrawConfigurator configurator) {
        startFileName = configurator.getStartFileName();
        endFileName = configurator.getEndFileName();
        pointLeftOffset = configurator.getPointLeftOffset();
        pointTopOffset = configurator.getPointTopOffset();
        routeWidth = configurator.getRouteWidth();
        routeColor = configurator.getRouteColor();
        zoom = configurator.isZoom();
        zoomWH = configurator.zoomWH();
    }

    public String getStartFileName() {
        return startFileName;
    }

    public void setStartFileName(String startFileName) {
        this.startFileName = startFileName;
    }

    public String getEndFileName() {
        return endFileName;
    }

    public void setEndFileName(String endFileName) {
        this.endFileName = endFileName;
    }

    public int getPointLeftOffset() {
        return pointLeftOffset;
    }

    public void setPointLeftOffset(int pointLeftOffset) {
        this.pointLeftOffset = pointLeftOffset;
    }

    public int getPointTopOffset() {
        return pointTopOffset;
    }

    public void setPointTopOffset(int pointTopOffset) {
        this.pointTopOffset = pointTopOffset;
    }

    public int getRouteWidth() {
        return routeWidth;
    }

    public void setRouteWidth(int routeWidth) {
        this.routeWidth = routeWidth;
    }

    public int getRouteColor() {
        return routeColor;
    }

    public void setRouteColor(int routeColor) {
        this.routeColor = routeColor;
    }

    public boolean isZoom() {
        return zoom;
    }

    public void setZoom(boolean zoom) {
        this.zoom = zoom;
    }

    public int getZoomWH() {
        return zoomWH;
    }

    public void setZoomWH(int zoomWH) {
        this.zoomWH = zoomWH;
    }

    @Override
    public String toString() {
        return "DrawRouteConfig{" +
                "startFileName='" + startFileName + '\'' +
                ", endFileName='" + endFileName + '\'' +
                ", pointLeftOffset=" + pointLeftOffset +
                ", pointTopOffset=" + pointTopOffset +
                ", routeWidth=" + routeWidth +
                ", routeColor=" + routeColor +
                ", zoom=" + zoom +
                ", zoomWH=" + zoomWH +
                '}';
    }
}