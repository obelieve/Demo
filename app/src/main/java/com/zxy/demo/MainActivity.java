package com.zxy.demo;

import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.news.project.Constant;
import com.news.project.WXPayBean;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv)
    public void onViewClicked() {
        String json = "{\"appid\":\"wx1e6b33f8d61a7cf5\",\"partnerid\":\"1572382431\",\"prepayid\":\"wx111558255600934787c734ee1445207700\",\"timestamp\":\"1578729505\",\"noncestr\":\"JLafAhNYkXZWSdwU\",\"package\":\"Sign=WXPay\",\"sign\":\"C80C395986E18F952E1E7DBE6BC2F945\"}";
        WXPayBean bean = new Gson().fromJson(json,WXPayBean.class);
        IWXAPI api = WXAPIFactory.createWXAPI(this, Constant.WX_APPID,false);
        PayReq request = new PayReq();
        request.appId = bean.getAppid();
        request.partnerId = bean.getPartnerid();
        request.prepayId = bean.getPrepayid();
        request.timeStamp = bean.getTimestamp();
        request.nonceStr = bean.getNoncestr();
        request.packageValue = bean.getPackageX();
        request.sign = bean.getSign();
        api.sendReq(request);
    }
}
