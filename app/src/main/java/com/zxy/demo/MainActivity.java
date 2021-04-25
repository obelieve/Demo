package com.zxy.demo;

import android.content.Intent;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.zxy.utility.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * issue:一个页面跳转到WebView页(WebViewActivity)，然后返回时ImageView的icon会出现灰色阴影。
 * 解决：WebView的硬件加速默认是开启的，需要关闭才可以。
 * 或iv控件在WebViewActivity#onDestroy()回调结束后，调用invalidate()刷新。
 * 或WebViewActivity单独开启一个进程，但耗时较长。
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView mTv;
    ImageView iv;
    NestedScrollView nsl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        iv = findViewById(R.id.iv);
        nsl = findViewById(R.id.nsl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e("LayerType:MainActivity "+iv.getLayerType());
//        iv.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                LogUtil.e("LayerType:invalidate "+iv.getLayerType());
//                iv.invalidate();
//            }
//        },400);
    }

    @OnClick(R.id.tv)
    public void onViewClicked() {
        Intent intent = new Intent(this,WebViewActivity.class);
        startActivity(intent);
    }
}
