package com.zxy.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.utility.LogUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTv;
    private ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTv = findViewById(R.id.tv);
        mIv = findViewById(R.id.iv);
        mTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long startTime = System.currentTimeMillis();
                /**
                 *
                 *                 //231,240,245 灰色【标记为可行走的路线】
                 *                 final int fR = 231;
                 *                 final int fG = 240;
                 *                 final int fB = 245;
                 *                 final int fDiff = 30;
                 */
                startTime = System.currentTimeMillis();
                Bitmap bitmap = BitmapUtil.getBitmap(AssetsUtil.getFile(MainActivity.this, "f1.jpg"));
                LogUtil.e("解析Bitmap-耗时：" + (System.currentTimeMillis() - startTime));
                startTime = System.currentTimeMillis();
                final int ZOOM_SIZE = 10;
                int[][] surface = BitmapUtil.changeToArray(bitmap, 231, 240, 245, 30, new int[]{Color.parseColor("#ffffff")}, 0.6f, ZOOM_SIZE);
                LogUtil.e("转为2维数组-耗时：" + (System.currentTimeMillis() - startTime));
                //3138,2509
                //1926,1663
                int[] start = BitmapUtil.changeToArrayXY(ZOOM_SIZE, 3138, 2509);
                int[] end = BitmapUtil.changeToArrayXY(ZOOM_SIZE, 3679, 1527);
                AStarAlgorithm algorithm = new AStarAlgorithm();
                LogUtil.e("surface[start[0]][start[1]]:"+surface[start[0]][start[1]]);
                LogUtil.e("surface[end[0]][end[1]]:"+surface[end[0]][end[1]]);
                startTime = System.currentTimeMillis();
                List<AStarAlgorithm.Node> list = algorithm.execute(surface, new AStarAlgorithm.Node(start[0], start[1]), new AStarAlgorithm.Node(end[0], end[1]));
                LogUtil.e("节点搜索-耗时：" + (System.currentTimeMillis() - startTime));
                startTime = System.currentTimeMillis();
                Bitmap bitmap1 = BitmapUtil.drawSurfaceRouting(bitmap, list, ZOOM_SIZE);
                try {
                    outputToFile(bitmap1);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                mIv.setImageBitmap(bitmap1);
                LogUtil.e("显示-耗时：" + (System.currentTimeMillis() - startTime));
            }
        });

    }

    private void A_start_bitmap(Bitmap bitmap) throws FileNotFoundException {

        getBitmapInfo(bitmap);
        long startTime = System.currentTimeMillis();
        LogUtil.e("当前时间：" + startTime);

//        Bitmap bitmap1 = BitmapUtil.drawIntervalLine(bitmap, ZOOM_SIZE);
//        outputToFile(bitmap1);
//        mIv.setImageBitmap(bitmap1);
    }


    /**
     * 输出到文件
     *
     * @param bitmap
     * @throws FileNotFoundException
     */
    public void outputToFile(Bitmap bitmap) throws FileNotFoundException {
        FileOutputStream out = new FileOutputStream(getExternalFilesDir(null) + "/a.jpg");
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
    }

    /**
     * 获取图像信息，显示
     *
     * @param bitmap
     */
    public void getBitmapInfo(Bitmap bitmap) {
        SpannableString sp = new SpannableString("abcdefghijklmnopqrstuvwxyz\n");
        sp.setSpan(new AbsoluteSizeSpan(20, true), 0, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(bitmap.getPixel(3287, 2367)), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(bitmap.getPixel(2969, 2268)), 6, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(bitmap.getPixel(2910, 2619)), 13, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTv.setText(sp);
        int b = Color.blue(bitmap.getPixel(3287, 2367));
        int r = Color.red(bitmap.getPixel(3287, 2367));
        int g = Color.green(bitmap.getPixel(3287, 2367));
        mTv.append("(3287,2367) r:" + Integer.toHexString(r) + " g:" + Integer.toHexString(g) + " b:" + Integer.toHexString(b) + "\n");
        mTv.append("        h:" + bitmap.getHeight() + " w:" + bitmap.getWidth() + " count=" + bitmap.getByteCount());
    }

    private static void A_start() {
        /**
         * 000
         * 010
         * 000
         */
        int[][] ints = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ints[i][j] = 0;
            }
        }
        ints[2][1] = 1;
        ints[2][2] = 1;
        ints[1][2] = 1;
        AStarAlgorithm algorithm = new AStarAlgorithm();
        AStarAlgorithm.Node end = new AStarAlgorithm.Node(1, 3);
        AStarAlgorithm.Node start = new AStarAlgorithm.Node(2, 0);
        List<AStarAlgorithm.Node> nodes = algorithm.execute(ints, start, end);
        LogUtil.e("结果 nodes：" + nodes);
//        Bitmap bitmap1 = BitmapUtil.changeToNewBitmap(surface, ZOOM_SIZE, bitmap, Color.parseColor("#7B4343"));
//        try {
//            outputToFile(bitmap1);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }


    public static class AssetsUtil {

        public static InputStream getFile(Context context, String name) {

            InputStream inputStream = null;
            try {
                inputStream = context.getAssets().open(name);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return inputStream;
        }

    }

    public static class BitmapUtil {

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
         * 二维数组标记为1值作为参照，对Bitmap进行填充相应颜色
         *
         * @param surface   Bitmap转为二维数组的值
         * @param zoomSize  #surface缩放的比例
         * @param bitmap    原图的Bitmap
         * @param drawColor 填充的颜色
         * @return 填充后的Bitmap
         */
        public static Bitmap changeToNewBitmap(int[][] surface, int zoomSize, Bitmap bitmap, int drawColor) {
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
                                canvas.drawPoint(w, h, paint);
                            }
                        }
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

        public static Bitmap drawSurfaceRouting(Bitmap bitmap, List<AStarAlgorithm.Node> nodes, int zoomSize) {
            Bitmap copy = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);  //很重要
            Canvas canvas = new Canvas(copy);  //创建画布
            Paint paint = new Paint();  //画笔
            paint.setStrokeWidth(1);  //设置线宽。单位为像素
            paint.setAntiAlias(true); //抗锯齿
            paint.setColor(Color.RED);  //画笔颜色
            canvas.drawBitmap(bitmap, new Matrix(), paint);  //在画布上画一个和bitmap一模一样的图
            for (int i = 0; i < nodes.size(); i++) {
                AStarAlgorithm.Node node = nodes.get(i);
                Rect rect = new Rect(node.X * zoomSize, node.Y * zoomSize, (node.X + 1) * zoomSize, (node.Y + 1) * zoomSize);
                canvas.drawRect(rect, paint);
            }
            return copy;
        }
    }
}
