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
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.demo.generator.MapGenerator;
import com.zxy.demo.impl.Floor1MapConfigurator;
import com.zxy.demo.utils.RouteMapUtil;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class MainActivity extends Activity {

    private TextView mTv;
    private ImageView mIv;

    private Disposable mDisposable;

    public void log(String msg) {
        Log.e(MainActivity.class.getSimpleName(), msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RouteMapUtil.init(getApplicationContext());
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
        String start, end;
        start = "抽屉";
        end = "冰箱位";
        routePlanning1(start, end);
//        gen();
    }

    private void gen() {
        final long startTime = System.currentTimeMillis();
        MapGenerator.create(new Floor1MapConfigurator());
        log("显示-耗时：" + (System.currentTimeMillis() - startTime));
    }

    public void routePlanning1(String startPosition, String endPosition) {
        final long startTime = System.currentTimeMillis();
        mDisposable = RouteMapUtil.Observable("f1.jpg", "f1.json", "f1.data", startPosition, endPosition).subscribe(new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap bitmap) throws Exception {
                mIv.setImageBitmap(bitmap);
                log("显示-耗时：" + (System.currentTimeMillis() - startTime));
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
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
