package com.zxy.demo.floor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 楼层相关信息
 * Created by zxy on 2019/08/30.
 */
public class FloorInfo {

    private int width;
    private int height;
    /**
     * 楼层图
     */
    private String floor_filename;
    /**
     * 二维数组映射数据文件
     */
    private String floor_data_filename;
    /**
     * 包含二维数组值、路由区域的楼层图
     */
    private String floor_info_filename;
    /**
     * 不可路由区域的颜色标识
     */
    private String floor_info_no_route_color;
    /**
     * 可路由区域RGB
     */
    private int route_r;
    private int route_g;
    private int route_b;
    /**
     * 可路由区域的误差值
     */
    private int route_rgb_bias;
    /**
     * 不可路由区域RGB（冲突优先不可路由）
     */
    private String[] no_route_rgbs;
    /**
     * 可路由区域中，可路由像素占比0~1.0
     */
    private float ratio;
    /**
     * 二维数组缩放尺寸
     */
    private int zoom_size;
    /**
     * 不可路由的二维数组坐标集合
     */
    private List<List<Integer>> no_route_list;
    /**
     * 特定位置对应的二维数组坐标集合
     */
    private Map<String, List<Integer>> place_map;

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

    public String getFloor_filename() {
        return floor_filename;
    }

    public void setFloor_filename(String floor_filename) {
        this.floor_filename = floor_filename;
    }

    public String getFloor_data_filename() {
        return floor_data_filename;
    }

    public void setFloor_data_filename(String floor_data_filename) {
        this.floor_data_filename = floor_data_filename;
    }

    public String getFloor_info_filename() {
        return floor_info_filename;
    }

    public void setFloor_info_filename(String floor_info_filename) {
        this.floor_info_filename = floor_info_filename;
    }

    public String getFloor_info_no_route_color() {
        return floor_info_no_route_color;
    }

    public void setFloor_info_no_route_color(String floor_info_no_route_color) {
        this.floor_info_no_route_color = floor_info_no_route_color;
    }

    public int getRoute_r() {
        return route_r;
    }

    public void setRoute_r(int route_r) {
        this.route_r = route_r;
    }

    public int getRoute_g() {
        return route_g;
    }

    public void setRoute_g(int route_g) {
        this.route_g = route_g;
    }

    public int getRoute_b() {
        return route_b;
    }

    public void setRoute_b(int route_b) {
        this.route_b = route_b;
    }

    public int getRoute_rgb_bias() {
        return route_rgb_bias;
    }

    public void setRoute_rgb_bias(int route_rgb_bias) {
        this.route_rgb_bias = route_rgb_bias;
    }

    public String[] getNo_route_rgbs() {
        return no_route_rgbs;
    }

    public void setNo_route_rgbs(String[] no_route_rgbs) {
        this.no_route_rgbs = no_route_rgbs;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public int getZoom_size() {
        return zoom_size;
    }

    public void setZoom_size(int zoom_size) {
        this.zoom_size = zoom_size;
    }

    public List<List<Integer>> getNo_route_list() {
        return no_route_list;
    }

    public void setNo_route_list(List<List<Integer>> no_route_list) {
        this.no_route_list = no_route_list;
    }

    public Map<String, List<Integer>> getPlace_map() {
        return place_map;
    }

    public void setPlace_map(Map<String, List<Integer>> place_map) {
        this.place_map = place_map;
    }

    @Override
    public String toString() {
        return "FloorInfo{" +
                "floor_filename='" + floor_filename + '\'' +
                ", floor_data_filename='" + floor_data_filename + '\'' +
                ", floor_info_filename='" + floor_info_filename + '\'' +
                ", floor_info_no_route_color='" + floor_info_no_route_color + '\'' +
                ", route_r=" + route_r +
                ", route_g=" + route_g +
                ", route_b=" + route_b +
                ", route_rgb_bias=" + route_rgb_bias +
                ", no_route_rgbs=" + Arrays.toString(no_route_rgbs) +
                ", ratio=" + ratio +
                ", zoom_size=" + zoom_size +
                ", no_route_list=" + no_route_list +
                ", place_map=" + place_map +
                '}';
    }
}
