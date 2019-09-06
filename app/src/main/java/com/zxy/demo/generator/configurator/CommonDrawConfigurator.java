package com.zxy.demo.generator.configurator;

import android.graphics.Color;

/**
 * Created by zxy on 2019/09/06.
 */
public class CommonDrawConfigurator implements DrawConfigurator {
    @Override
    public String getStartFileName() {
        return "start.png";
    }

    @Override
    public String getEndFileName() {
        return "end.png";
    }

    @Override
    public int getPointLeftOffset() {
        return 10;
    }

    @Override
    public int getPointTopOffset() {
        return 15;
    }

    @Override
    public int getRouteWidth() {
        return 10;
    }

    @Override
    public int getRouteColor() {
        return Color.RED;
    }

    @Override
    public boolean isZoom() {
        return true;
    }

    @Override
    public int zoomWH() {
        return 30;
    }
}
