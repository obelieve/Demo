package com.zxy.demo.impl;

import android.graphics.Bitmap;
import android.graphics.Color;

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
public class Floor3MapConfigurator implements MapConfigurator {


    @Override
    public String getUniqueMapName() {
        return "f3";
    }

    @Override
    public Bitmap getOriginalBitmap() {
        return RouteMapUtil.getBitmap(RouteMapUtil.getAssetsFile("f3.jpg"));
    }

    @Override
    public String getRouteColor() {
        return "#C0D0E7";
    }

    @Override
    public Map<String, List<Integer>> getLocation() {
        Map<String, List<Integer>> map = new HashMap<>();
        map.put("左边电梯", Arrays.asList(859, 2027));
        map.put("并联审批窗口", Arrays.asList(1235, 2159));
        map.put("自然资源局", Arrays.asList(1943, 2425));
        map.put("林业局", Arrays.asList(2530, 2279));
        map.put("生态环境局", Arrays.asList(3188, 2121));
        map.put("交通局", Arrays.asList(3847,1957));
        map.put("人防办", Arrays.asList(4161, 1855));
        map.put("住建局", Arrays.asList(4041, 1499));
        map.put("右边电梯", Arrays.asList(3919, 1207));
        map.put("中闽水务", Arrays.asList(3575, 1382));
        map.put("国网供电", Arrays.asList(3321, 1440));
        map.put("新奥燃气", Arrays.asList(3087, 1492));
        map.put("安然燃气", Arrays.asList(2827, 1554));
        map.put("发改委", Arrays.asList(2579,1610));

        map.put("水利局", Arrays.asList(2247, 1690));
        map.put("气象局", Arrays.asList(2035, 1734));
        map.put("应急管理", Arrays.asList(1807,1788));
        map.put("取号机", Arrays.asList(1505, 1888));
        map.put("厕所", Arrays.asList(1285, 1908));
        return map;
    }

    @Override
    public BuildConfigurator getBuildConfigurator() {
        return new CommonBuildConfigurator() {
            @Override
            public int[] getNoRouteRGBs() {
                return new int[]{Color.parseColor("#FFFFFF")};
            }

            @Override
            public int getRgbBias() {
                return 50;
            }

            @Override
            public float getRoutePercent() {
                return 0.5f;
            }

            @Override
            public int getZoomSize() {
                return 20;
            }
        };
    }
}
