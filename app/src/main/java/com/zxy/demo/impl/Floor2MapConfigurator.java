package com.zxy.demo.impl;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.zxy.demo.generator.configurator.BuildConfigurator;
import com.zxy.demo.generator.configurator.CommonBuildConfigurator;
import com.zxy.demo.generator.configurator.CommonDrawConfigurator;
import com.zxy.demo.generator.configurator.DrawConfigurator;
import com.zxy.demo.generator.configurator.MapConfigurator;
import com.zxy.demo.utils.RouteMapUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxy on 2019/09/05.
 */
public class Floor2MapConfigurator implements MapConfigurator {


    @Override
    public String getUniqueMapName() {
        return "f2";
    }

    @Override
    public Bitmap getOriginalBitmap() {
        return RouteMapUtil.getBitmap(RouteMapUtil.getAssetsFile("f2.jpg"));
    }

    @Override
    public String getRouteColor() {
        return "#DFE9F3";
    }

    @Override
    public Map<String, List<Integer>> getLocation() {
        Map<String, List<Integer>> map = new HashMap<>();
        map.put("扶梯", Arrays.asList(2989, 1547));
        map.put("市场监督管理局", Arrays.asList(1949, 2407));
        map.put("商务局", Arrays.asList(1111, 2620));
        map.put("左边厕所", Arrays.asList(845, 2263));
        map.put("印章刻制", Arrays.asList(1333, 2275));
        map.put("北京CA", Arrays.asList(1473, 2239));
        map.put("福建CA", Arrays.asList(1656, 2195));
        map.put("土地测绘", Arrays.asList(1629, 1927));
        map.put("科技局", Arrays.asList(1573, 1799));
        map.put("教育局", Arrays.asList(1517, 1643));
        map.put("文化旅游局", Arrays.asList(1465, 1503));
        map.put("左边电梯", Arrays.asList(1173, 1439));
        map.put("海洋渔业局", Arrays.asList(1533, 1323));
        map.put("司法局", Arrays.asList(1777, 1271));
        map.put("卫健局", Arrays.asList(2045, 1207));
        map.put("民政局", Arrays.asList(2309, 1155));
        map.put("人社局", Arrays.asList(2533, 1111));
        map.put("医社保综合服务区1", Arrays.asList(3045, 995));
        map.put("右边电梯", Arrays.asList(3433, 843));
        map.put("医社保综合服务区2", Arrays.asList(3749, 1515));
        map.put("右边厕所", Arrays.asList(4469, 1363));
        map.put("公安综合服务区", Arrays.asList(3997, 1883));
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

    @Override
    public DrawConfigurator getDrawConfigurator() {
        return new CommonDrawConfigurator();
    }
}
