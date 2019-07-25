package com.ainirobot.sdk_demo.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ainirobot.sdk_demo.R;
import com.ainirobot.sdk_demo.model.BaseFragment;

/**
 * @author Orion
 * @time 2019/3/30
 */
public class DocFragment extends BaseFragment {

    private static final String TAG = "DocFragment";

    private WebView mWebView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mContainer = inflater.inflate(R.layout.layout_doc, container, false);
        mWebView = mContainer.findViewById(R.id.webview_doc);
        initWebview();
        return mContainer;
    }

    private void initWebview() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);// 不支持缩放
        settings.setUseWideViewPort(true);// 将图片调整到适合webview大小
        settings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
//        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//支持缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//支持缓存
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);// 设置允许访问文件数据
        settings.setDatabaseEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.setInitialScale(1);
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.setHorizontalScrollBarEnabled(false);//水平不显示
        mWebView.setVerticalScrollBarEnabled(false); //垂直不显示
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity())
                        .setTitle("信息").setMessage(message)
                        .setPositiveButton("OK", new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        });
                alert.setCancelable(false);
                alert.create();
                alert.show();
                return true;
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                Log.d(TAG, "onReceivedSslError: " + error);
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(TAG, "onPageStarted: " + url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                Log.i(TAG, "shouldOverrideUrlLoading: " + url);
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, "onPageFinished: " + url);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.loadUrl("https://note.youdao.com/ynoteshare1/index.html?id=9029108f9528c2865b9fc62ca8e887ce&type=note");
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
