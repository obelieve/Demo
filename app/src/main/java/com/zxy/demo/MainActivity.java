package com.zxy.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

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
//        url = "http://www.baidu.com";
        //
//        mWebView.loadUrl(url);

        //js 参数没有var
//        mWebView.loadDataWithBaseURL(null,"<html><script type=\"text/javascript\">   \n" +
//                "function showToast(msg) {       \n" +
//                "    App.showToast(msg);\n" +
//                "     }\n" +
//                "</script><body><h1>biaosda</h1><p>萨空间打开的金卡</p><div><input type=\"button\" value = \"展示Toast\" onClick=\"showToast('好')\"></input></div></body></html>","text/html",null,null);
        mWebView.loadUrl("file:///android_asset/text.html");
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
