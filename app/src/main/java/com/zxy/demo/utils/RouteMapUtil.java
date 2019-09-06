package com.zxy.demo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.zxy.demo.algorithm.AStarAlgorithm;
import com.zxy.demo.generator.MapProcessor;
import com.zxy.demo.generator.model.DrawRouteConfig;
import com.zxy.demo.generator.model.MapData;
import com.zxy.demo.generator.model.MapInfo;
import com.zxy.demo.impl.FloorMap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zxy on 2019/08/30.
 */
public class RouteMapUtil {

    /**
     * 需要分段显示路线时，所需的间隔时间
     */
    private static final int ROUTE_DELAY_TIME = 500;
    private static Context sContext;

    public static Context getContext() {
        if (sContext == null) {
            throw new IllegalStateException("Need to initialize RouteMapUtil#init(Context) !");
        }
        return sContext;
    }

    public static void init(Context context) {
        sContext = context;
    }

    public static Observable<Bitmap> Observable(@NonNull FloorMap map, final String startPosition, final String endPosition) {
        String assetsImagePath;
        String assetsJsonPath;
        String assetsDataPath;
        switch (map) {
            case FLOOR_1:
            default:
                assetsImagePath = "f1.jpg";
                assetsJsonPath = "f1.json";
                assetsDataPath = "f1.data";
                break;
            case FLOOR_2:
                assetsImagePath = "f2.jpg";
                assetsJsonPath = "f2.json";
                assetsDataPath = "f2.data";
                break;
            case FLOOR_3:
                assetsImagePath = "f3.jpg";
                assetsJsonPath = "f3.json";
                assetsDataPath = "f3.data";
                break;
        }
        return Observable(assetsImagePath, assetsJsonPath, assetsDataPath, startPosition, endPosition);
    }

    public static Observable<Bitmap> Observable(final String assetsImagePath, final String assetsJsonPath, final String assetsDataPath, final String startPosition, final String endPosition) {

        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                try {
                    Bitmap bitmap = RouteMapUtil.getBitmap(RouteMapUtil.getAssetsFile(assetsImagePath));
                    emitter.onNext(bitmap);
                    if (TextUtils.isEmpty(startPosition) || TextUtils.isEmpty(endPosition) || startPosition.equals(endPosition)) {
                        emitter.onComplete();
                        return;
                    }
                    MapInfo mapInfo = MapStreamUtil.readMapInfo(RouteMapUtil.getAssetsFile(assetsJsonPath));
                    MapProcessor mapProcessor = new MapProcessor(mapInfo);
                    MapData mapData = new MapData(MapStreamUtil.readMapData(RouteMapUtil.getAssetsFile(assetsDataPath)));
                    mapProcessor.setMapData(mapData);
                    List<AStarAlgorithm.Node> list = mapProcessor.genMapRouteNodes(startPosition, endPosition);

                    if (list.size() >= 2) {
                        int startX = list.get(0).X;
                        int startY = list.get(0).Y;
                        int endX = list.get(list.size() - 1).X;
                        int endY = list.get(list.size() - 1).Y;
                        int distance = Math.abs(endY - startY) + Math.abs(endX - startX);
                        final int DEF_DISTANCE = (mapInfo.getWidth() + mapInfo.getHeight()) / mapInfo.getZoomSize() / 10;//设置默认每秒显示的距离
                        if (DEF_DISTANCE == 0 || distance < 2 * DEF_DISTANCE || list.size() == 2) {//直接显示起始点路线
                            Bitmap tempBitmap = mapProcessor.genMapRouteBitmap(bitmap, list, mapInfo.getConfig());
                            bitmap.recycle();
                            bitmap = tempBitmap;
                            emitter.onNext(bitmap);
                        } else {//间隔一段时间，显示一段路线
                            int split = distance / DEF_DISTANCE;
                            int increase = list.size() / split;
                            int curIndex = 0;
                            RouteTrace routeTrace = RouteTrace.START;
                            Bitmap tempBitmap = null;
                            while (curIndex < list.size()) {
                                switch (routeTrace) {
                                    case START:
                                        curIndex += increase;
                                        tempBitmap = RouteMapUtil.drawSurfaceStartRouting(bitmap, list.subList(0, curIndex), mapInfo.getZoomSize(), mapInfo.getConfig());
                                        bitmap.recycle();
                                        bitmap = tempBitmap;
                                        emitter.onNext(bitmap);
                                        if (curIndex + increase >= list.size()) {
                                            routeTrace = RouteTrace.WHOLE;
                                        } else {
                                            routeTrace = RouteTrace.START;
                                        }
                                        Thread.sleep(ROUTE_DELAY_TIME);
                                        break;
                                    case WHOLE:
                                        curIndex += increase;
                                        tempBitmap = RouteMapUtil.drawSurfaceWholeRouting(bitmap, list, mapInfo.getZoomSize(), mapInfo.getConfig());
                                        bitmap.recycle();
                                        bitmap = tempBitmap;
                                        emitter.onNext(bitmap);
                                        break;
                                }
                            }
                        }
                    }
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                    e.printStackTrace();
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public static InputStream getAssetsFile(String name) {

        InputStream inputStream = null;
        try {
            inputStream = getContext().getAssets().open(name);
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
     * @param bitmap
     * @param splitPixel 分割线间隔的像素数
     * @return
     */
    public static Bitmap drawIntervalLine(Bitmap bitmap, int splitPixel) {

        /**
         * 注意多个createBitmap重载函数，必须是可变位图对应的重载才能绘制
         * bitmap: 原图像
         * pixInterval: 网格线的横竖间隔，单位:像素
         */
        Bitmap copy = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);  //很重要
        Canvas canvas = new Canvas(copy);  //创建画布
        Paint paint = new Paint();  //画笔
        paint.setStrokeWidth(1);  //设置线宽。单位为像素
        paint.setAntiAlias(true); //抗锯齿
        paint.setColor(Color.RED);  //画笔颜色
        canvas.drawBitmap(bitmap, new Matrix(), paint);  //在画布上画一个和bitmap一模一样的图
        //根据Bitmap大小，画网格线
        //画横线
        for (int i = 0; i < bitmap.getHeight() / splitPixel; i++) {
            canvas.drawLine(0, i * splitPixel, bitmap.getWidth(), i * splitPixel, paint);
        }
        //画竖线
        for (int i = 0; i < bitmap.getWidth() / splitPixel; i++) {
            canvas.drawLine(i * splitPixel, 0, i * splitPixel, bitmap.getHeight(), paint);
        }
        return copy;
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
    public static int[][] changeToArray(Bitmap bitmap, int routeR, int routeG, int routeB,
                                        int rgbBias, @ColorInt int[] noRouteRGBs, float ratio, int zoomSize) {
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
     * 二维数组标记为1值作为参照，对Bitmap进行填充相应颜色和显示二维数组位置值(黑色字体)
     *
     * @param surface   Bitmap转为二维数组的值
     * @param zoomSize  #surface缩放的比例
     * @param bitmap    原图的Bitmap
     * @param drawColor 填充的颜色
     * @return 填充后的Bitmap
     */
    public static Bitmap changeToNewBitmap(int[][] surface, int zoomSize, Bitmap bitmap,
                                           int drawColor) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int arrayWidth = surface.length;
        int arrayHeight = surface[0].length;
        Bitmap copy = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);  //很重要
        Canvas canvas = new Canvas(copy);  //创建画布
        Paint paint = new Paint();//画笔
        paint.setStrokeWidth(1);  //设置线宽。单位为像素
        paint.setAntiAlias(true); //抗锯齿
        paint.setColor(drawColor);  //画笔颜色
        canvas.drawBitmap(bitmap, new Matrix(), paint);  //在画布上画一个和bitmap一模一样的图
        for (int aw = 0; aw < arrayWidth; aw++) {
            for (int ah = 0; ah < arrayHeight; ah++) {
                for (int w = aw * zoomSize; w < (aw + 1) * zoomSize; w++) {
                    for (int h = ah * zoomSize; h < (ah + 1) * zoomSize; h++) {
                        if (w < width && h < height && surface[aw][ah] == 1) {
                            paint.setColor(drawColor);
                            canvas.drawPoint(w, h, paint);
                        }
                    }
                }
                if (aw * zoomSize < width && ah * zoomSize < height && surface[aw][ah] == 1) {
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(8);
                    canvas.drawText(aw + "", aw * zoomSize + 2, ah * zoomSize + 8, paint);
                    canvas.drawText(ah + "", aw * zoomSize + 2, ah * zoomSize + 16, paint);
                }
            }
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

    public static Bitmap drawSurfaceStartRouting(Bitmap bitmap, List<AStarAlgorithm.Node> nodes, int zoomSize, DrawRouteConfig config) {
        return drawSurfacePath(bitmap, nodes, true, false, zoomSize, config);
    }

    public static Bitmap drawSurfaceWholeRouting(Bitmap bitmap, List<AStarAlgorithm.Node> nodes, int zoomSize, DrawRouteConfig config) {
        return drawSurfacePath(bitmap, nodes, true, true, zoomSize, config);
    }

    /**
     * 绘制节点区域
     *
     * @param bitmap
     * @param nodes
     * @param zoomSize
     * @param paintWidth
     * @param paintColor
     * @param paintStyle
     * @return
     */
    public static Bitmap drawSurfaceNodes(Bitmap bitmap, List<AStarAlgorithm.Node> nodes, int zoomSize, int paintWidth, int paintColor, Paint.Style paintStyle) {
        Bitmap copy = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(copy);  //创建画布
        Paint paint = new Paint();  //画笔
        paint.setStrokeWidth(paintWidth);  //设置线宽。单位为像素
        paint.setAntiAlias(true); //抗锯齿
        paint.setColor(paintColor);  //画笔颜色
        paint.setStyle(paintStyle);
        canvas.drawBitmap(bitmap, new Matrix(), paint);  //在画布上画一个和bitmap一模一样的图
        Paint textPaint = new Paint();
        textPaint.setStrokeWidth(1);  //设置线宽。单位为像素
        textPaint.setAntiAlias(true); //抗锯齿
        textPaint.setTextSize(8);
        textPaint.setColor(Color.BLACK);  //画笔颜色
        textPaint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < nodes.size(); i++) {
            AStarAlgorithm.Node node = nodes.get(i);
            Rect rect = new Rect();
            rect.set(node.X * zoomSize, node.Y * zoomSize, (node.X + 1) * zoomSize, (node.Y + 1) * zoomSize);
            canvas.drawRect(rect, paint);
            String ff = node.getF() + "";
            if (ff.length() > 2) {
                canvas.drawText(ff.substring(0, 2), node.X * zoomSize + 8, node.Y * zoomSize + 8, textPaint);
                canvas.drawText(ff.substring(2), node.X * zoomSize + 8, node.Y * zoomSize + 16, textPaint);
            } else {
                canvas.drawText(node.getF() + "", node.X * zoomSize + 8, node.Y * zoomSize + 8, textPaint);
            }
        }
        return copy;
    }

    /**
     * 绘制节点的路线
     *
     * @param bitmap
     * @param nodes
     * @param startTag
     * @param endTag
     * @param zoomSize
     * @param config
     * @return
     */
    private static Bitmap drawSurfacePath(Bitmap bitmap, List<AStarAlgorithm.Node> nodes,
                                          boolean startTag, boolean endTag, int zoomSize, DrawRouteConfig config) {
        Bitmap copy = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(copy);  //创建画布
        Paint paint = new Paint();  //画笔
        paint.setStrokeWidth(config.getRouteWidth());  //设置线宽。单位为像素
        paint.setAntiAlias(true); //抗锯齿
        paint.setColor(config.getRouteColor());  //画笔颜色
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawBitmap(bitmap, new Matrix(), paint);  //在画布上画一个和bitmap一模一样的图
        if (nodes.size() > 1) {
            Path path = new Path();
            float startX = (nodes.get(0).X + 0.5f) * zoomSize;
            float startY = (nodes.get(0).Y + 0.5f) * zoomSize;
            float endX = (nodes.get(nodes.size() - 1).X + 0.5f) * zoomSize;
            float endY = (nodes.get(nodes.size() - 1).Y + 0.5f) * zoomSize;
            path.moveTo(startX, startY);
            InputStream startIn = getAssetsFile(config.getStartFileName());
            InputStream endIn = getAssetsFile(config.getEndFileName());
            for (int i = 1; i < nodes.size(); i++) {
                AStarAlgorithm.Node node = nodes.get(i);
                float nodeX = (node.X + 0.5f) * zoomSize;
                float nodeY = (node.Y + 0.5f) * zoomSize;
                path.lineTo(nodeX, nodeY);
            }
            canvas.drawPath(path, paint);
            if (startIn != null && endIn != null) {
                if (startTag) {
                    Bitmap startBitmap = config.isZoom() ? zoomBitmap(startIn, config.getZoomWH(), config.getZoomWH()) : getBitmap(startIn);
                    canvas.drawBitmap(startBitmap, startX - config.getPointLeftOffset(), startY - config.getPointTopOffset(), paint);
                }
                if (endTag) {
                    Bitmap endBitmap = config.isZoom() ? zoomBitmap(endIn, config.getZoomWH(), config.getZoomWH()) : getBitmap(endIn);
                    canvas.drawBitmap(endBitmap, endX - config.getPointLeftOffset(), endY - config.getPointTopOffset(), paint);
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

    /**
     * 路线轨迹枚举
     */
    private enum RouteTrace {
        /**
         * 完整路线
         */
        WHOLE,
        /**
         * 从起点到中间某个点的路线
         */
        START
    }

}