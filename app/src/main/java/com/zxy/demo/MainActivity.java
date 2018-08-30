package com.zxy.demo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zxy.demo.bind.Bind;
import com.zxy.demo.bind.ViewInject;
import com.zxy.utility.LogUtil;

import java.io.File;

/**
 * 1.issue:
 * onPageStarted和onPageFinished会调用多次，由于重定向问题，shouldOverrideUrlLoading会调用多次
 */
public class MainActivity extends AppCompatActivity
{
    @ViewInject(R.id.wv)
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bind.inject(this);
        initWebViewConfig();
        String url="";
        //url = "http://www.aicchain.co/wap/index.php?";
        url = "http://www.baidu.com";
        //
        mWebView.loadUrl(url);
        //mWebView.loadDataWithBaseURL(null,"<html><body><h1>biaosda</h1><p>萨空间打开的金卡</p></body></html>","text/html",null,null);
    }

    public class MyWebViewClient extends WebViewClient
    {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {//url重定向
            LogUtil.e();
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {//页面开始时
            super.onPageStarted(view, url, favicon);
            LogUtil.e();
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {//页面完成时
            super.onPageFinished(view, url);
            LogUtil.e();
        }

    }

    public class MyWebChromeClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress)
        {
            super.onProgressChanged(view, newProgress);
            LogUtil.e(""+newProgress);
        }


    }

    private void initWebViewConfig()
    {
        initWebViewSettings(mWebView.getSettings());
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
    }

    private void initWebViewSettings(WebSettings settings)
    {
        File databaseDir = new File(getCacheDir().getAbsolutePath() + "/WebViewCache");
        if (!databaseDir.exists())
        {
            databaseDir.mkdir();
        }
        //viewport 设置
        settings.setUseWideViewPort(true);//使用html viewport提供显示区域
        settings.setLoadWithOverviewMode(true);//默认自适应内容屏幕
        //设置缩放
        settings.setSupportZoom(true);//是否支持zoom
        settings.setBuiltInZoomControls(true);//手势放大/缩小 控制
        settings.setDisplayZoomControls(false);//显示放大/缩小 按钮
        //Dom Storage
        settings.setDomStorageEnabled(true);//开启Dom Storage API 功能
        //Cache Mode
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//缓存模式 LOAD_DEFAULT（默认是有缓存数据并且不过期就使用缓存，否则从网络中拉取）
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//重新布局

        //密码保存
        settings.setSavePassword(false);//不保存密码
        //设置DB
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath(databaseDir.getAbsolutePath());
        //设置地理位置DB
        settings.setGeolocationEnabled(true);
        settings.setGeolocationDatabasePath(databaseDir.getAbsolutePath());
        //设置APPCache
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(databaseDir.getAbsolutePath());
        settings.setAppCacheMaxSize(1024*1024*8);
        //设置UserAgent 请求头加入这个值，用于不同的设备显示不同内容
        String us = "";
        settings.setUserAgentString(us);
        //设置js
        settings.setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed()
    {
        if (mWebView.canGoBack())
        {
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }
}
