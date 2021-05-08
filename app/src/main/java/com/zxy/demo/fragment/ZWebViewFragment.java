package com.zxy.demo.fragment;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.zxy.demo.databinding.FragmentZwebviewBinding;
import com.zxy.frame.base.ApiBaseFragment;
import com.zxy.frame.utils.ToastUtil;
import com.zxy.frame.view.AppWebView;

public class ZWebViewFragment extends ApiBaseFragment<FragmentZwebviewBinding> {


    @Override
    protected void initView() {
        mViewBinding.wvContent.setCallback(new AppWebView.Callback() {
            @Override
            public ProgressBar getProgress() {
                return mViewBinding.pbLoading;
            }

            @Override
            public void onReceivedTitle(String title) {

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
        mViewBinding.wvContent.loadUrl("https://developer.mozilla.org/zh-CN/");
//        wvContent.addJavascriptInterface(new JSInvoke(),"Android");
//        wvContent.setWebContentsDebuggingEnabled(true);
//        wvContent.getSettings().setUserAgentString("Android");
        mViewBinding.wvContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
                    if(mViewBinding.wvContent.canGoBack()){
                        mViewBinding.wvContent.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        mViewBinding.wvContent.release();
        super.onDestroyView();
    }

    public static class JSInvoke {
        @JavascriptInterface
        public void login() {
            ToastUtil.show("登录");
        }
    }
}
