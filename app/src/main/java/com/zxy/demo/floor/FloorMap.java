package com.zxy.demo.floor;

import java.io.Serializable;

/**
 * 楼层地图的二维数组映射数据
 * Created by zxy on 2019/08/30.
 */
public class FloorMap implements Serializable {

    private int[][] mSurface;

    public FloorMap(int[][] surface) {
        mSurface = surface;
    }

    public int[][] getSurface() {
        return mSurface;
    }

}