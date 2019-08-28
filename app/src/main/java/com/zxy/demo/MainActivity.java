package com.zxy.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.utility.LogUtil;

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
                A_start_bitmap();
                //A_start();
            }
        });

    }

    private void A_start_bitmap() {
        Bitmap bitmap = BitmapUtil.getBitmap(AssetsUtil.getFile(MainActivity.this, "f1.jpg"));
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
        mIv.setImageBitmap(BitmapUtil.drawBackground(bitmap, 20));
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
        A_StartAlgorithm algorithm = new A_StartAlgorithm();
        A_StartAlgorithm.Node end = new A_StartAlgorithm.Node(1, 3);
        A_StartAlgorithm.Node start = new A_StartAlgorithm.Node(2, 0);
        algorithm.init(ints, start, end);
        List<A_StartAlgorithm.Node> nodes = algorithm.execute();
        LogUtil.e("结果 nodes：" + nodes);
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

        public static Bitmap getBitmap(InputStream in) {
            return BitmapFactory.decodeStream(in);
        }


        private static Bitmap drawBackground(Bitmap bitmap, int pixInterval) {
            long startTime = System.currentTimeMillis();
            LogUtil.e("当前时间：" + startTime);
            /**
             * 注意多个createBiamap重载函数，必须是可变位图对应的重载才能绘制
             * bitmap: 原图像
             * pixInterval: 网格线的横竖间隔，单位:像素
             */
            Bitmap copy = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);  //很重要
            Canvas canvas = new Canvas(copy);  //创建画布
            Paint paint = new Paint();  //画笔
            paint.setStrokeWidth(30);  //设置线宽。单位为像素
            paint.setAntiAlias(true); //抗锯齿
            paint.setColor(Color.RED);  //画笔颜色
            canvas.drawBitmap(bitmap, new Matrix(), paint);  //在画布上画一个和bitmap一模一样的图
            //根据Bitmap大小，画网格线
            //画横线
//            for (int i = 0; i < bitmap.getHeight() / pixInterval; i++) {
//                canvas.drawLine(0, i * pixInterval, bitmap.getWidth(), i * pixInterval, paint);
//            }
//            //画竖线
//            for (int i = 0; i < bitmap.getWidth() / pixInterval; i++) {
//                canvas.drawLine(i * pixInterval, 0, i * pixInterval, bitmap.getHeight(), paint);
//            }
//            canvas.drawLine(2000, 2000, 4000, 1500, paint);
            drawInsideRoad(bitmap, startTime, canvas, paint);

            return copy;
        }
    }

    /**
     * 绘制除了路之外的区域
     *
     * @param bitmap
     * @param startTime
     * @param canvas
     * @param paint
     */
    private static void drawInsideRoad(Bitmap bitmap, long startTime, Canvas canvas, Paint paint) {
        //耗时44秒
        int[][] surface = new int[bitmap.getWidth()][bitmap.getHeight()];
        A_StartAlgorithm.Node start = new A_StartAlgorithm.Node(3342,2299);
        A_StartAlgorithm.Node end = new A_StartAlgorithm.Node(2380,2389);
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                int r = Color.red(bitmap.getPixel(i, j));
                int g = Color.green(bitmap.getPixel(i, j));
                int b = Color.blue(bitmap.getPixel(i, j));
                //231,240,245 灰色
                final int fR = 231;
                final int fG = 240;
                final int fB = 245;
                final int fDiff = 30;
                surface[i][j] = 0;
                if (!((r >= (fR - fDiff) && r <= (fR + fDiff)) && (g >= (fG - fDiff) && g <= (fG + fDiff)) && (b >= (fB - fDiff) && b <= (fB + fDiff)))) {
//                    canvas.drawPoint(i, j, paint);
                    surface[i][j] = 1;
                } else {
                    if (r == 255 && g == 255 && b == 255) {
//                        canvas.drawPoint(i, j, paint);
                        surface[i][j] = 1;
                    }
                }
            }
        }
        LogUtil.e("当前时间-耗时：" + (System.currentTimeMillis() - startTime));
        A_StartAlgorithm algorithm = new A_StartAlgorithm();
        algorithm.init(surface, start, end);
        List<A_StartAlgorithm.Node> nodes = algorithm.execute();
        LogUtil.e("结果当前时间-耗时：" + (System.currentTimeMillis() - startTime));
        for (int i = 0; i < nodes.size(); i++) {
            A_StartAlgorithm.Node node = nodes.get(i);
            canvas.drawPoint(node.X, node.Y, paint);
        }
        LogUtil.e("绘制当前时间-耗时：" + (System.currentTimeMillis() - startTime));
    }

}
