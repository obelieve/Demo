package com.zxy.demo.generator.configurator;

/**
 * Created by zxy on 2019/09/06.
 */
public interface DrawConfigurator {

    /**
     * 起点图片文件名
     *
     * @return
     */
    String getStartFileName();

    /**
     * 终点图片文件名
     *
     * @return
     */
    String getEndFileName();

    /**
     * 图片左边坐标偏移值
     *
     * @return
     */
    int getPointLeftOffset();

    /**
     * 图片右边坐标偏移值
     *
     * @return
     */
    int getPointTopOffset();

    /**
     * 路线绘制宽度 px
     *
     * @return
     */
    int getRouteWidth();

    /**
     * 路线绘制颜色
     *
     * @return
     */
    int getRouteColor();

    /**
     * 图片是否缩放
     *
     * @return
     */
    boolean isZoom();

    /**
     * 图片缩放后的宽高
     *
     * @return
     */
    int zoomWH();
}
