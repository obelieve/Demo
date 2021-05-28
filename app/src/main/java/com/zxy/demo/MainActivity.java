package com.zxy.demo;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.obelieve.frame.base.ApiBaseActivity;
import com.obelieve.frame.base.ApiBaseActivity2;
import com.obelieve.frame.utils.ToastUtil;
import com.obelieve.frame.utils.log.LogUtil;
import com.zxy.demo.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * GooglePay支付流程：
 * 1.购买列表项点击商品
 * 2.点击购买，Google回调付款令牌
 * 3.把付款令牌，传给后端
 * 4.后端处理交易发送给付款服务提供商（后端通过接口响应结果）
 * 5.** 需要和Google Console配置的包名和签名一致 **
 * 6.** 改变BASE_URL **
 */
public class MainActivity extends ApiBaseActivity<ActivityMainBinding,MainViewModel> {

    private PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                    && purchases != null) {//成功购买后，购买交易
                for (Purchase purchase : purchases) {
                    handlePurchase(purchase);
                }
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
            } else {
                // Handle any other error codes.
            }
        }
    };

    private BillingClient billingClient;
    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        billingClient = BillingClient.newBuilder(this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    LogUtil.e("支付","连接成功 "+billingResult.getDebugMessage()+" "+billingResult.getResponseCode());
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                LogUtil.e("支付","连接失败");
            }
        });
        mViewBinding.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.fetchDiamondGoodInfo(false);
            }
        });
    }

    @Override
    protected void initViewModel() {
        mViewModel.getMDiamondGoodInfoEntity().observe(this, new Observer<DiamondGoodEntity>() {
            @Override
            public void onChanged(DiamondGoodEntity entity) {
                LogUtil.e("获取数据 entity="+entity);
                String iapId = entity.getDiamond_goods_list().get(0).getIap_id();
                List<String> skuList = new ArrayList<>();
                skuList.add(iapId);
                SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                billingClient.querySkuDetailsAsync(params.build(),
                        new SkuDetailsResponseListener() {
                            @Override
                            public void onSkuDetailsResponse(BillingResult billingResult,
                                                             List<SkuDetails> skuDetailsList) {
                                if(skuDetailsList!=null&&skuDetailsList.size()>0) {
                                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                            .setSkuDetails(skuDetailsList.get(0))
                                            .build();
                                    //启动购买流程，点击购买后回调onPurchasesUpdated(..)
                                    int responseCode = billingClient.launchBillingFlow(MainActivity.this, billingFlowParams).getResponseCode();
                                }else{
                                    ToastUtil.show("支付失败："+billingResult.getDebugMessage()+" "+billingResult.getResponseCode());
                                }
                            }
                        });
            }
        });
        mViewModel.getMPaySuccessLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                ToastUtil.show("支付结果："+aBoolean);
            }
        });
    }

    private void handlePurchase(Purchase purchase) {
        // Purchase retrieved from BillingClient#queryPurchases or your PurchasesUpdatedListener.
        // Verify the purchase.
        // Ensure entitlement was not already granted for this purchaseToken.
        // Grant entitlement to the user.
        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

        ConsumeResponseListener listener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // Handle the success of the consume operation.
                    //成功购买令牌来消耗商品，再授予用户权利
                    mViewModel.sendPayToken(purchase.getOriginalJson());
                }
            }
        };

        billingClient.consumeAsync(consumeParams, listener);
    }
}
