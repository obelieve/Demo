package com.zxy.demo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity
{
    public static final String URL = "http://testo2onew.fanwe.net/wap/biz.php?ctl=shop_verify";
    WebView mWV;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWV = (WebView) findViewById(R.id.wv);
        mWV.getSettings().setSupportZoom(true);//支持缩放
        mWV.getSettings().setBuiltInZoomControls(true);//支持缩放按钮 (只有页面宽度大于屏幕宽度后才能正常显示)

        mWV.getSettings().setJavaScriptEnabled(true);//JS
        mWV.getSettings().setUseWideViewPort(true);//viewPort

        mWV.setWebChromeClient(new WebChromeClient());//辅助WebView处理JavaScript的对话框，网站图标，网站title，加载进度等
        mWV.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
            {

                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        //UserAgent 和网页显示有关系
        String us = "Mozilla/5.0 (Linux; Android 7.0; FRD-AL10 Build/HUAWEIFRD-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36 fanwe_app_sdk sdk_type/android sdk_version_name/1.0.0 sdk_version/2018031901 sdk_guid/860596034937017 screen_width/1080 screen_height/1794";
        mWV.getSettings().setUserAgentString(us);
        mWV.loadUrl(URL);
    }

    @Override
    public void onBackPressed()
    {
        if (mWV.canGoBack())
        {
            mWV.goBack();
        } else
        {
            super.onBackPressed();
        }
    }
}
