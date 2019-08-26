package com.zxy.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private TextView mTv;
    private ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTv = findViewById(R.id.tv);
        mIv = findViewById(R.id.iv);
        Bitmap bitmap = BitmapUtil.getBitmap(AssetsUtil.getFile(this, "f1.jpg"));
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
            /**
             * 注意多个createBiamap重载函数，必须是可变位图对应的重载才能绘制
             * bitmap: 原图像
             * pixInterval: 网格线的横竖间隔，单位:像素
             */
            Bitmap copy = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);  //很重要
            Canvas canvas = new Canvas(copy);  //创建画布
            Paint paint = new Paint();  //画笔
            paint.setStrokeWidth(50);  //设置线宽。单位为像素
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
            canvas.drawLine(2000, 2000, 4000, 1500, paint);

            return copy;
        }
    }
}
