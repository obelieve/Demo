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
    public static final String URL = "https://www.baidu.com";
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
