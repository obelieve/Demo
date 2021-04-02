package com.zxy.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zxy.demo.bind.Bind;
import com.zxy.demo.bind.ViewInject;

/**
 * 1.issue:
 * onPageStarted和onPageFinished会调用多次，由于重定向问题，shouldOverrideUrlLoading会调用多次
 */
public class MainActivity extends AppCompatActivity
{
    @ViewInject(R.id.wv)
    private AppWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bind.inject(this);
        mWebView.addJavascriptInterface(new JsInvoke(this),"App");
        String url="";
        //url = "http://www.aicchain.co/wap/index.php?";
        url = "https://sportlive.oksportserv.com/live/4dfa2a1da044f2fd.m3u8?txSecret=cf9c186dedb98283c7f1b4724cb4701e&txTime=60666db0";
        //
        mWebView.loadUrl(url);

        //js 参数没有var
//        mWebView.loadDataWithBaseURL(null,"<html><script type=\"text/javascript\">   \n" +
//                "function showToast(msg) {       \n" +
//                "    App.showToast(msg);\n" +
//                "     }\n" +
//                "</script><body><h1>biaosda</h1><p>萨空间打开的金卡</p><div><input type=\"button\" value = \"展示Toast\" onClick=\"showToast('好')\"></input></div></body></html>","text/html",null,null);
//        mWebView.loadUrl("file:///android_asset/text.html");
//        mWebView.loadDataWithBaseURL(null,"<a href=\"https://www.baidu.com\" target=\"_blank\">yyy</a>","text/html",null,null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        mWebView.onActivityResult(requestCode,resultCode,data);
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

    class JsInvoke
    {
        private Context mContext;

        public JsInvoke(Context con)
        {
            this.mContext = con;
        }

        @JavascriptInterface
        public void showToast(String msg)
        {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
