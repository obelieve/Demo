package com.zxy.demo.fragment;

import android.webkit.JavascriptInterface;

import com.zxy.demo.R;
import com.zxy.frame.base.BaseFragment;
import com.zxy.frame.utils.ToastUtil;
import com.zxy.frame.view.AppWebView;

import butterknife.BindView;

public class ZWebViewFragment extends BaseFragment {

    @BindView(R.id.wv_content)
    AppWebView wvContent;

    @Override
    public int layoutId() {
        return R.layout.fragment_zwebview;
    }

    @Override
    protected void initView() {
        wvContent.loadUrl("https://developer.mozilla.org/zh-CN/");
//        wvContent.addJavascriptInterface(new JSInvoke(),"Android");
//        wvContent.setWebContentsDebuggingEnabled(true);
//        wvContent.getSettings().setUserAgentString("Android");
    }


    public static class JSInvoke{
        @JavascriptInterface
        public void login(){
            ToastUtil.show("登录");
        }
    }
}
