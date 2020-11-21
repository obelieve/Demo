package com.zxy.demo.fragment;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.zxy.demo.databinding.FragmentZwebviewBinding;
import com.zxy.frame.base.ApiBaseFragment;
import com.zxy.frame.utils.ToastUtil;

public class ZWebViewFragment extends ApiBaseFragment<FragmentZwebviewBinding> {


    @Override
    protected void initView() {
        mViewBinding.wvContent.setProgressBar(mViewBinding.pbLoading);
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


    public static class JSInvoke {
        @JavascriptInterface
        public void login() {
            ToastUtil.show("登录");
        }
    }
}
