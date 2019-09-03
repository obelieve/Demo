package com.zxy.demo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;

import com.zxy.demo.algorithm.AStarAlgorithm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by zxy on 2019/08/30.
 */
public class RouteMapUtil {

    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    public static void init(Context context) {
        sContext = context;
    }

    public static InputStream getAssetsFile(String name) {

        InputStream inputStream = null;
        try {
            inputStream = sContext.getAssets().open(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * 获取Bitmap
     *
     * @param in
     * @return
     */
    public static Bitmap getBitmap(InputStream in) {
        return BitmapFactory.decodeStream(in);
    }

    /**
     * 绘制分隔线
     *
     * @param bitmap       原图
     * @param lineDistance 分割线间隔距离 单位px
     * @return
     */
    public static Bitmap drawIntervalLine(Bitmap bitmap, int lineDistance) {

        Bitmap copy = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);  //很重要
        Canvas canvas = new Canvas(copy);  //创建画布
        Paint paint = new Paint();  //画笔
        paint.setStrokeWidth(1);  //设置线宽。单位为像素
        paint.setAntiAlias(true); //抗锯齿
        paint.setColor(Color.RED);  //画笔颜色
        canvas.drawBitmap(bitmap, new Matrix(), paint);  //在画布上画一个和bitmap一模一样的图
        //根据Bitmap大小，画网格线
        //画横线
        for (int i = 0; i < bitmap.getHeight() / lineDistance; i++) {
            canvas.drawLine(0, i * lineDistance, bitmap.getWidth(), i * lineDistance, paint);
        }
        //画竖线
        for (int i = 0; i < bitmap.getWidth() / lineDistance; i++) {
            canvas.drawLine(i * lineDistance, 0, i * lineDistance, bitmap.getHeight(), paint);
        }
        return copy;
    }

    /**
     * Bitmap中的x,y通过缩放比例转为二维数组的x,y
     *
     * @param zoomSize
     * @param bitmapX
     * @param bitmapY
     * @return
     */
    public static int[] changeToArrayXY(int zoomSize, int bitmapX, int bitmapY) {
        int x = bitmapX / zoomSize;
        int y = bitmapY / zoomSize;
        if (bitmapX > zoomSize * x) {
            x++;
        }
        if (bitmapY > zoomSize * y) {
            y++;
        }
        return new int[]{x, y};
    }

    /**
     * 根据bitmap获取路径地图的二维数组
     *
     * @param bitmap      图像
     * @param routeR      可路由像素的r值 0~255
     * @param routeG      可路由像素的g值 0~255
     * @param routeB      可路由像素的b值 0~255
     * @param rgbBias     可路由像素的误差值 0~255
     * @param noRouteRGBs 不可路由像素的rgb值（如果与可路由像素rgb冲突，优先不可路由）
     * @param ratio       缩放尺寸中，可路由像素的比例:0~1.0  (PS:原先20个像素，现在表示一个二维数组值，20像素中可路由像素的比例占多少，二维数组值才能标记为可路由)
     * @param zoomSize    缩放的尺寸,大于1                        (PS：500x500图像,zoomSize=10,那么就是[50][50])
     * @return 路径二维数组 [*][*]=1,表示为不可路由区域
     */
    public static int[][] changeToArray(Bitmap bitmap, int routeR, int routeG, int routeB, int rgbBias, @ColorInt int[] noRouteRGBs, float ratio, int zoomSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int arrayWidth = width / zoomSize;
        if (width > arrayWidth * zoomSize) {
            arrayWidth += 1;
        }
        int arrayHeight = height / zoomSize;
        if (height > arrayHeight * zoomSize) {
            arrayHeight += 1;
        }
        int[][] surface = new int[arrayWidth][arrayHeight];

        for (int aw = 0; aw < arrayWidth; aw++) {
            for (int ah = 0; ah < arrayHeight; ah++) {
                //二维数组值所表示的像素集
                int routeCount = 0;//可路由像素的数量
                for (int w = aw * zoomSize; w < (aw + 1) * zoomSize; w++) {
                    for (int h = ah * zoomSize; h < (ah + 1) * zoomSize; h++) {
                        if (w < width && h < height) {//有可能超过实际坐标
                            int color = bitmap.getPixel(w, h);
                            int colorR = Color.red(color);
                            int colorG = Color.green(color);
                            int colorB = Color.blue(color);
                            if (colorR >= (routeR - rgbBias) && colorR <= (routeR + rgbBias) &&
                                    colorG >= (routeG - rgbBias) && colorG <= (routeG + rgbBias) &&
                                    colorB >= (routeB - rgbBias) && colorB <= (routeB + rgbBias)) {
                                if (noRouteRGBs != null && noRouteRGBs.length > 0) {
                                    for (int l = 0; l < noRouteRGBs.length; l++) {
                                        int noRouteColor = noRouteRGBs[l];
                                        if (!(colorR == Color.red(noRouteColor) && colorG == Color.green(noRouteColor) && colorB == Color.blue(noRouteColor))) {
                                            routeCount++;
                                        }
                                    }
                                } else {
                                    routeCount++;
                                }
                            }
                        }
                    }
                }
                if (routeCount >= zoomSize * zoomSize * ratio) {
                    surface[aw][ah] = 0;
                } else {
                    surface[aw][ah] = 1;
                }
            }
        }
        return surface;
    }

    /**
     * 对非路线区域进行绘制并覆盖到原图上
     *
     * @param bitmap               原图
     * @param surface              Bitmap的二维数组映射
     * @param zoomSize             Bitmap在二维数组映射中,缩放的比例
     * @param noRouteAreaColor     非路线区域填充的颜色
     * @return 覆盖后的Bitmap
     */
    public static Bitmap overlayNoRouteAreaToBitmap(Bitmap bitmap, int[][] surface, int zoomSize, int noRouteAreaColor) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int arrayWidth = surface.length;
        int arrayHeight = surface[0].length;
        Bitmap copy = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(copy);  //创建画布
        Paint paint = new Paint();//画笔
        paint.setStrokeWidth(1);  //设置线宽。单位为像素
        paint.setAntiAlias(true); //抗锯齿
        paint.setColor(noRouteAreaColor);  //画笔颜色
        canvas.drawBitmap(bitmap, new Matrix(), paint);  //在画布上画一个和bitmap一模一样的图
        for (int aw = 0; aw < arrayWidth; aw++) {
            for (int ah = 0; ah < arrayHeight; ah++) {
                for (int w = aw * zoomSize; w < (aw + 1) * zoomSize; w++) {
                    for (int h = ah * zoomSize; h < (ah + 1) * zoomSize; h++) {
                        if (w < width && h < height && surface[aw][ah] == 1) {
                            paint.setColor(noRouteAreaColor);
                            canvas.drawPoint(w, h, paint);
                        }
                    }
                }
                if (aw * zoomSize < width && ah * zoomSize < height && surface[aw][ah] == 1) {
                    paint.setColor(Color.parseColor("#0A4082"));
                    paint.setTextSize(8);
                    canvas.drawText(aw + "", aw * zoomSize + 2, ah * zoomSize + 8, paint);
                    canvas.drawText(ah + "", aw * zoomSize + 2, ah * zoomSize + 8 * 2, paint);
                }
            }
        }
        return copy;
    }

    /**
     * 绘制起点到终点完整路线
     *
     * @param bitmap   原图
     * @param nodes    节点列表
     * @param zoomSize 缩放尺寸
     * @return
     */
    public static Bitmap drawSurfaceWholeRouting(Bitmap bitmap, List<AStarAlgorithm.Node> nodes, int zoomSize) {
        return drawSurfacePath(bitmap, nodes, true, true, zoomSize);
    }

    /**
     * 绘制起点到中间某个节点的路线
     *
     * @param bitmap    原图
     * @param nodes     节点列表
     * @param zoomSize  缩放尺寸
     * @return
     */
    public static Bitmap drawSurfaceStartRouting(Bitmap bitmap, List<AStarAlgorithm.Node> nodes, int zoomSize) {
        return drawSurfacePath(bitmap, nodes, true, false, zoomSize);
    }

    private static Bitmap drawSurfacePath(Bitmap bitmap, List<AStarAlgorithm.Node> nodes, boolean startTag, boolean endTag, int zoomSize) {
        Bitmap copy = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);  //很重要
        Canvas canvas = new Canvas(copy);  //创建画布
        Paint paint = new Paint();  //画笔
        paint.setStrokeWidth(10);  //设置线宽。单位为像素
        paint.setAntiAlias(true); //抗锯齿
        paint.setColor(Color.RED);  //画笔颜色
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawBitmap(bitmap, new Matrix(), paint);  //在画布上画一个和bitmap一模一样的图
        if (nodes.size() > 1) {
            Path path = new Path();
            float startX = (nodes.get(0).X + 0.5f) * zoomSize;
            float startY = (nodes.get(0).Y + 0.5f) * zoomSize;
            float endX = (nodes.get(nodes.size() - 1).X + 0.5f) * zoomSize;
            float endY = (nodes.get(nodes.size() - 1).Y + 0.5f) * zoomSize;
            InputStream startIn = getAssetsFile("start.png");
            InputStream endIn = getAssetsFile("end.png");
            path.moveTo(startX, startY);
            for (int i = 1; i < nodes.size(); i++) {
                AStarAlgorithm.Node node = nodes.get(i);
                float nodeX = (node.X + 0.5f) * zoomSize;
                float nodeY = (node.Y + 0.5f) * zoomSize;
                path.lineTo(nodeX, nodeY);
            }
            canvas.drawPath(path, paint);
            if (startIn != null || endIn != null) {
                if (startTag) {
                    canvas.drawBitmap(zoomBitmap(startIn, 30, 30), startX - 10, startY - 15, paint);
                }
                if (endTag) {
                    canvas.drawBitmap(zoomBitmap(endIn, 30, 30), endX - 10, endY - 15, paint);
                }
            }
        }
        return copy;
    }

    public static Bitmap zoomBitmap(InputStream in, int previewWidth, int previewHeight) {
        BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
// 不将图片读取到内存中，仍然可以通过参数获得它的高宽
        factoryOptions.inJustDecodeBounds = true;
        byte[] bytes = toByteArray(in);
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, factoryOptions);
        int imageWidth = factoryOptions.outWidth;
        int imageHeight = factoryOptions.outHeight;
        // 等比缩小，previewWidth和height是imageView的宽高
        int scaleFactor = Math.max(imageWidth / previewWidth,
                imageHeight / previewHeight);

        // 将图片读取到内存中
        factoryOptions.inJustDecodeBounds = false;
        // 设置等比缩小图
        factoryOptions.inSampleSize = scaleFactor;
        // 样图可以回收内存
        factoryOptions.inPurgeable = true;

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, factoryOptions);
    }

    private static byte[] toByteArray(InputStream input) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (true) {
            try {
                if (-1 == (n = input.read(buffer))) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}