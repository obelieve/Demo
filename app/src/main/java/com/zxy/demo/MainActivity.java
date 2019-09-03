package com.zxy.demo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zxy.demo.ui.RouteImageView;
import com.zxy.demo.utils.generator.Floor1GeneratorImpl;

public class MainActivity extends Activity {

    private TextView mTv;
    private RouteImageView mIv;

    public void log(String msg) {
        Log.e(MainActivity.class.getSimpleName(), msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTv = findViewById(R.id.tv);
        mIv = findViewById(R.id.iv);
        mTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRoute();
            }
        });
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mTv.performClick();
            }
        });
    }

    private void clickRoute() {
        new Floor1GeneratorImpl().saveToFile();
        //routePlanning1("入口","前台");
    }

    public void routePlanning1(String startPosition, String endPosition) {
        long startTime = System.currentTimeMillis();
        mIv.setRouteImageBitmap("f1.jpg","f1.json","f1.data",startPosition,endPosition);
        log("显示-耗时：" + (System.currentTimeMillis() - startTime));
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
}
