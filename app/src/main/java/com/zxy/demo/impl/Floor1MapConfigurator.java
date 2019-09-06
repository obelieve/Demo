package com.zxy.demo.impl;

import android.graphics.Bitmap;

import com.zxy.demo.generator.configurator.BuildConfigurator;
import com.zxy.demo.generator.configurator.CommonBuildConfigurator;
import com.zxy.demo.generator.configurator.MapConfigurator;
import com.zxy.demo.utils.RouteMapUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxy on 2019/09/05.
 */
public class Floor1MapConfigurator implements MapConfigurator {


    @Override
    public String getUniqueMapName() {
        return "f1";
    }

    @Override
    public Bitmap getOriginalBitmap() {
        return RouteMapUtil.getBitmap(RouteMapUtil.getAssetsFile("f1.jpg"));
    }

    @Override
    public String getRouteColor() {
        return "#FFFFFF";
    }

    @Override
    public Map<String, List<Integer>> getLocation() {
        Map<String, List<Integer>> map = new HashMap<>();
        map.put("抽屉", Arrays.asList(164, 179));
        map.put("消毒柜", Arrays.asList(353, 180));
        map.put("推拉门", Arrays.asList(506, 275));
        map.put("冰箱位", Arrays.asList(517, 161));
        map.put("上柜", Arrays.asList(421, 29));
        return map;
    }

    @Override
    public BuildConfigurator getBuildConfigurator() {
        return new CommonBuildConfigurator() {

            @Override
            public int getRgbBias() {
                return 10;
            }

            @Override
            public float getRoutePercent() {
                return 0.8f;
            }

            @Override
            public int getZoomSize() {
                return 5;
            }
        };
    }
}
