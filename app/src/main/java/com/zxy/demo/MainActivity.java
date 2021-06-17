package com.zxy.demo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.obelieve.frame.base.ApiBaseActivity2;
import com.obelieve.frame.utils.log.LogUtil;
import com.zxy.demo.databinding.ActivityMainBinding;
import com.zxy.demo.nativead.NativeTemplateStyle;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends ApiBaseActivity2<ActivityMainBinding> {

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull @NotNull InitializationStatus initializationStatus) {
                LogUtil.e("初始化状态："+initializationStatus.getAdapterStatusMap());
            }
        });
        mViewBinding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AdPageActivity.class));
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mViewBinding.adView.loadAd(adRequest);
        mViewBinding.adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                LogUtil.e("onAdClosed");
            }

            @Override
            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                LogUtil.e("onAdFailedToLoad："+loadAdError.getMessage());
            }

            @Override
            public void onAdOpened() {
                LogUtil.e("onAdOpened");
            }

            @Override
            public void onAdLoaded() {
                LogUtil.e("onAdLoaded");
            }

            @Override
            public void onAdClicked() {
                LogUtil.e("onAdClicked");
            }

            @Override
            public void onAdImpression() {
                LogUtil.e("onAdImpression");
            }
        });

        //原生广告
        AdLoader adLoader = new AdLoader.Builder(mActivity, "ca-app-pub-3940256099942544/2247696110")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull @NotNull NativeAd nativeAd) {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(new ColorDrawable(Color.WHITE)).build();
                        mViewBinding.myTemplate.setStyles(styles);
                        mViewBinding.myTemplate.setNativeAd(nativeAd);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                    }

                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
        mViewBinding.btnNativeAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,UnifiedNativeAdActivity.class));
            }
        });
    }


}
