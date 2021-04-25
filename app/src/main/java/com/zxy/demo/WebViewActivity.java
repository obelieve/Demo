package com.zxy.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.zxy.utility.LogUtil;

import java.lang.ref.WeakReference;


public class WebViewActivity extends Activity {

    LinearLayout ll;
    WebView wv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ll = findViewById(R.id.ll);
        wv = findViewById(R.id.wv);
        wv.setLayerType(View.LAYER_TYPE_SOFTWARE,null);//Vivo V1816A手机 android 9.0：不开启时，出现切换WebView再返回ImageView显示有灰色阴影
        LogUtil.e("LayerType:" + wv.getLayerType());
        wv.loadUrl("http://www.baidu.com");

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断是否可以返回操作  
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            onBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {

        LogUtil.e("LayerType:Destroy " + wv.getLayerType());
        wv.clearHistory();
        wv.removeAllViews();
        wv.destroy();
        super.onDestroy();

    }


    private void onBack() {
        if (wv.canGoBack()) {
            wv.goBack();
        } else {
            finish();
        }
    }

}
