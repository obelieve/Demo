package com.zxy.demo.generator.configurator;

import android.support.annotation.ColorInt;

/**
 * 地图构建配置
 * Created by zxy on 2019/09/05.
 */
public interface BuildConfigurator {

    /**
     * 不是路径的RGB色值
     *
     * @return
     */
    @ColorInt
    int[] getNoRouteRGBs();

    /**
     * 获取路径RGB的偏移值
     *
     * @return
     */
    int getRgbBias();

    /**
     * 获取地图缩放的倍数
     *
     * @return
     */
    int getZoomSize();

    /**
     * 缩放地图后，每个节点中，原先路径所占比例。0~1.0
     *
     * @return
     */
    float getRoutePercent();

    /**
     * 对(x,y)的节点进行铺设路径操作
     *
     * @param surface
     * @param x
     * @param y
     */
    void openRoute(int[][] surface, int x, int y);

    /**
     * 对路径区域重新设置
     *
     * @param surface
     */
    void resetRoute(int[][] surface);
}
