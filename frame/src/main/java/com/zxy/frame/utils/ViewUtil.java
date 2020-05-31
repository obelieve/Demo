package com.zxy.frame.utils;

import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.ViewCompat;

import com.zxy.utility.SystemUtil;

/**
 * Created by Admin
 * on 2020/5/29
 */
public class ViewUtil {

    public static void insetStatusBar(View view) {
        SystemUtil.init(view.getContext().getApplicationContext());
        if (view.getLayoutParams() != null) {
            view.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            view.getLayoutParams().height = SystemUtil.getStatusBarHeight();
        }
    }

    /**
     * 设置View阴影背景
     *
     * @param view
     * @param drawable
     */
    public static void setShadowBackground(View view, ShadowDrawable drawable) {
        /**
         * #setShadowLayer(..)需要关闭硬件加速，CardView设置app:cardCornerRadius 需要开启硬件加速
         */
        ViewCompat.setBackground(view, drawable);
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }
}
