package com.zxy.demo.generator.model;

import java.io.Serializable;

/**
 * 楼层地图的二维数组映射数据
 * Created by zxy on 2019/09/06.
 */
public class MapData implements Serializable {

    private int[][] mSurface;

    public MapData(int[][] surface) {
        mSurface = surface;
    }

    public int[][] getSurface() {
        return mSurface;
    }

}