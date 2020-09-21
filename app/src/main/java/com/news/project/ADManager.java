package com.news.project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.news.project.plugin.R;
import com.qq.e.ads.nativ.NativeADUnifiedListener;
import com.qq.e.ads.nativ.NativeUnifiedAD;
import com.qq.e.ads.nativ.NativeUnifiedADData;
import com.qq.e.ads.nativ.widget.NativeAdContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Admin
 * on 2020/9/21
 */
public class ADManager {

    /**
     * 请求获取原生自渲染 数据
     *
     * @param activity
     * @param listener
     */
    public static void requestNativeUnifiedAD(Activity activity, NativeADUnifiedListener listener) {
        NativeUnifiedAD nativeAD = new NativeUnifiedAD(activity, Constants.APPID,
                PositionId.INFORMATION_POS_ID[new Random().nextInt(PositionId.INFORMATION_POS_ID.length - 1)], listener);
        nativeAD.loadData(PositionId.INFORMATION_POS_ID.length);
    }

    /**
     * 绑定原生广告视图
     *
     * @param activity
     * @param frameLayout
     * @param obj
     */
    public static void bindNativeUnifiedADView(Activity activity, final FrameLayout frameLayout, final Object obj) {
        final NativeUnifiedADData data = (NativeUnifiedADData)obj;
        TextView tvTitle = null;
        ImageView ivImage = null;
        ImageView imageCloseAd = null;
        LinearLayout layoutContent = null;
        NativeAdContainer nativeAdContainer = null;
        if (frameLayout.getChildCount() == 0) {
            View inflate = LayoutInflater.from(activity).inflate(R.layout.viewholder_native_ad_container, null);
            tvTitle = inflate.findViewById(R.id.tv_title);
            ivImage = inflate.findViewById(R.id.iv_image);
            imageCloseAd = inflate.findViewById(R.id.image_close_ad);
            layoutContent = inflate.findViewById(R.id.layout_content);
            nativeAdContainer = inflate.findViewById(R.id.native_ad_container);
            //添加广告视图
            frameLayout.addView(inflate);
        }
        if (tvTitle != null && ivImage != null && imageCloseAd != null && layoutContent != null && nativeAdContainer != null) {
            frameLayout.setVisibility(View.VISIBLE);
            //新建要绑定点击事件的viewList
            List<View> viewList = new ArrayList<>();
            viewList.add(layoutContent);
            //传入点击事件
            data.bindAdToView(activity, nativeAdContainer, null, viewList);
            tvTitle.setText(data.getDesc());
            Glide.with(activity).load(data.getImgUrl()).placeholder(R.drawable.failed_to_load_1).into(ivImage);
            //关闭广告
            imageCloseAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    frameLayout.removeAllViews();
                    data.destroy();
                }
            });
        }
    }
}
