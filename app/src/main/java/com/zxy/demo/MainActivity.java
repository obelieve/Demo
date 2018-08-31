package com.zxy.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zxy.SystemInfoUtil;
import com.zxy.demo.bind.Bind;
import com.zxy.demo.bind.ViewInject;
import com.zxy.utility.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

/**
 * 1.issue:
 * onPageStarted和onPageFinished会调用多次，由于重定向问题，shouldOverrideUrlLoading会调用多次
 */
public class MainActivity extends AppCompatActivity
{
    @ViewInject(R.id.wv)
    private WebView mWebView;

    ValueCallback<Uri> mValueCallbackAndroid4;
    ValueCallback<Uri[]> mValueCallbackAndroid5;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bind.inject(this);
        initWebViewConfig();
        String url="";
        //url = "http://www.aicchain.co/wap/index.php?";
       // url = "http://www.baidu.com";
        //
        //mWebView.loadUrl(url);

        //js 参数没有var
//        mWebView.loadDataWithBaseURL(null,"<html><script type=\"text/javascript\">   \n" +
//                "function showToast(msg) {       \n" +
//                "    App.showToast(msg);\n" +
//                "     }\n" +
//                "</script><body><h1>biaosda</h1><p>萨空间打开的金卡</p><div><input type=\"button\" value = \"展示Toast\" onClick=\"showToast('好')\"></input></div></body></html>","text/html",null,null);
        mWebView.loadUrl("file:///android_asset/text.html");
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

        // For Android  > 4.1.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
        {
            mValueCallbackAndroid4 = uploadMsg;
            selectFile(MainActivity.this);
        }

        // For Android >5.0
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams)
        {
            mValueCallbackAndroid5 = filePathCallback;
            selectFile(MainActivity.this);
            return true;
        }
    }

    private String mCameraPicturePath;//拍照图片

    public void selectFile(Context context)
    {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .create();

        TextView tvTakePicture = new TextView(context);
        tvTakePicture.setTextSize(30);
        tvTakePicture.setText("拍照");
        TextView tvAlbum = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 30, 0, 0);
        tvAlbum.setLayoutParams(params);
        tvAlbum.setTextSize(30);
        tvAlbum.setText("相册");

        tvTakePicture.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                File file = new File(getExternalCacheDir() + "/" + DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg");
                mCameraPicturePath = file.getPath();
                Intent intent = IntentUtil.takePicture();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, 0);
                dialog.dismiss();
            }
        });
        tvAlbum.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(IntentUtil.selectPicture(), 1);
                dialog.dismiss();
            }
        });
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(tvTakePicture);
        linearLayout.addView(tvAlbum);

        dialog.setView(linearLayout);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 0://拍照
                if (mCameraPicturePath != null && resultCode == Activity.RESULT_OK)
                {
                    File file = new File(mCameraPicturePath);
                    FileOutputStream out = null;

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(mCameraPicturePath, options);
                    if (options.outWidth > SystemInfoUtil.screenWidth(this))
                    {
                        options.inSampleSize = options.outWidth / SystemInfoUtil.screenWidth(this);
                    }
                    options.inJustDecodeBounds = false;

                    Bitmap bitmap = BitmapFactory.decodeFile(mCameraPicturePath, options);
                    try
                    {
                        out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    } catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                        htmlReceiveValue(null);
                        return;
                    } finally
                    {
                        if (bitmap != null)
                        {
                            bitmap.recycle();
                        }
                        if (out != null)
                        {
                            try
                            {
                                out.close();
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        mCameraPicturePath = null;
                    }
                    htmlReceiveValue(Uri.fromFile(file));
                } else
                {
                    htmlReceiveValue(null);
                }
                htmlValueCallbackNull();
                break;
            case 1://相册
                htmlReceiveValue(data!=null?data.getData():null);
                htmlValueCallbackNull();
                break;
        }
    }

    /**
     * 网页接收数据
     *
     * @param uri
     */
    public void htmlReceiveValue(Uri uri)
    {
        if (mValueCallbackAndroid4 != null)
        {
            mValueCallbackAndroid4.onReceiveValue(uri);
        } else if (mValueCallbackAndroid5 != null)
        {
            if(uri==null){
                mValueCallbackAndroid5.onReceiveValue(new Uri[]{});
            }else{
                mValueCallbackAndroid5.onReceiveValue(new Uri[]{uri});
            }
        }
    }

    /**
     * 清空ValueCallback
     */
    public void htmlValueCallbackNull()
    {
        if (mValueCallbackAndroid4 != null)
        {
            mValueCallbackAndroid4 = null;
        } else if (mValueCallbackAndroid5 != null)
        {
            mValueCallbackAndroid5 = null;
        }
    }


    private void initWebViewConfig()
    {
        initWebViewSettings(mWebView.getSettings());
        mWebView.addJavascriptInterface(new JsInvoke(this),"App");//addJavascriptInterface 最小支持API 17
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
        //settings.setUseWideViewPort(true);//使用html viewport提供显示区域
        //settings.setLoadWithOverviewMode(false);//默认自适应内容屏幕
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
        //设置地理位置DBgit
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
