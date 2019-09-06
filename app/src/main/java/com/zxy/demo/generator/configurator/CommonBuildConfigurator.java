package com.zxy.demo.generator.configurator;

/**
 * Created by zxy on 2019/09/05.
 */
public class CommonBuildConfigurator implements BuildConfigurator {
    @Override
    public int[] getNoRouteRGBs() {
        return null;
    }

    @Override
    public int getRgbBias() {
        return 0;
    }

    @Override
    public int getZoomSize() {
        return 20;
    }

    @Override
    public float getRoutePercent() {
        return 0.6f;
    }

    @Override
    public void openRoute(int[][] surface, int x, int y) {
        /**
         * 主要做的是：
         * 如果这个位置(x,y)没有路（surface[x][y] == 1表示路径不可通），就向四个方向进行寻找是否有路径，
         * 找到最佳的路径后，进行开路。
         */
        final int DIVIDE_COUNT = 5;
        int MIN_NEED_COUNT = 2;
        int divide = 0;
        int orientation = 0;
        int[] count = new int[4];//l=0,t=1,r=2,b=3
        if (surface[x][y] == 1) {
            surface[x][y] = 0;
            while (count[orientation] < MIN_NEED_COUNT) {
                count = new int[4];
                divide += DIVIDE_COUNT;
                for (int i = 0; i < divide; i++) {
                    if (existLocation(surface, x - i, y) && surface[x - i][y] == 0) {
                        count[0]++;
                    }
                    if (existLocation(surface, x, y - i) && surface[x][y - i] == 0) {
                        count[1]++;
                    }
                    if (existLocation(surface, x + i, y) && surface[x + i][y] == 0) {
                        count[2]++;
                    }
                    if (existLocation(surface, x, y + i) && surface[x][y + i] == 0) {
                        count[3]++;
                    }
                }
                if (count[1] > count[orientation]) {
                    orientation = 1;
                }
                if (count[2] > count[orientation]) {
                    orientation = 2;
                }
                if (count[3] > count[orientation]) {
                    orientation = 3;
                }
                if (divide == DIVIDE_COUNT * 4) {
                    break;
                }
            }
            for (int i = 0; i < divide; i++) {
                switch (orientation) {
                    case 0:
                        if (existLocation(surface, x - i, y))
                            surface[x - i][y] = 0;
                        break;
                    case 1:
                        if (existLocation(surface, x, y - i))
                            surface[x][y - i] = 0;
                        break;
                    case 2:
                        if (existLocation(surface, x + i, y))
                            surface[x + i][y] = 0;
                        break;
                    case 3:
                        if (existLocation(surface, x, y + i))
                            surface[x][y + i] = 0;
                        break;
                }
            }
        }
    }

    @Override
    public void resetRoute(int[][] surface) {

    }

    private boolean existLocation(int[][] surface, int x, int y) {
        if (x < surface.length && y < surface[0].length) {
            return true;
        }
        return false;
    }

}
