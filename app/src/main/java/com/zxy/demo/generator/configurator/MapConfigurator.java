package com.zxy.demo.generator.configurator;

import android.graphics.Bitmap;

import java.util.List;
import java.util.Map;

/**
 * 地图配置器
 * Created by zxy on 2019/09/03.
 */
public interface MapConfigurator {

    /**
     * 地图唯一名称
     *
     * @return
     */
    String getUniqueMapName();

    /**
     * 获取原文件输入流
     *
     * @return
     */
    Bitmap getOriginalBitmap();

    /**
     * 获取可路由区域的色值
     *
     * @return
     */
    String getRouteColor();

    /**
     * 获取地点和关联的像素点
     *
     * @return
     */
    Map<String, List<Integer>> getLocation();

    /**
     * 构建配置器
     *
     * @return
     */
    BuildConfigurator getBuildConfigurator();
}
