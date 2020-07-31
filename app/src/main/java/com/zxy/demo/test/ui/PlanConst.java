package com.zxy.demo.test.ui;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import com.zxy.demo.R;
import com.zxy.utility.SystemUtil;

/**
 * Created by Admin
 * on 2020/7/29
 */
public class PlanConst {

    public enum ColorValue {
        HOT(Color.parseColor("#F53F3F"), Color.parseColor("#FFF3F3"), R.drawable.bg_fff3f3_stroke_f0f0f0_point5_dp),
        WARM(Color.parseColor("#FF8800"), Color.parseColor("#FFF3E5"), R.drawable.bg_fff3e5_stroke_f0f0f0_point5_dp),
        COLD(Color.parseColor("#0077FF"), Color.parseColor("#EDF5FF"), R.drawable.bg_edf5ff_stroke_f0f0f0_point5_dp),
        DRAGON(Color.parseColor("#0077FF"), Color.parseColor("#EDF5FF"), R.drawable.bg_edf5ff_stroke_f0f0f0_point5_dp),
        TIGER(Color.parseColor("#FF8800"), Color.parseColor("#FFF3E5"), R.drawable.bg_fff3e5_stroke_f0f0f0_point5_dp),
        NO_DRAGON_TIGER(Color.parseColor("#FBFCFB"), Color.parseColor("#FBFCFB"), R.drawable.bg_fbfcfb_stroke_f0f0f0_point5_dp);

        private int textColor;
        private int bgColor;
        private int bgRes;

        ColorValue(int textColor, int bgColor,int bgRes) {
            this.textColor = textColor;
            this.bgColor = bgColor;
            this.bgRes = bgRes;
        }

        public int getTextColor() {
            return textColor;
        }

        public int getBgColor() {
            return bgColor;
        }

        public int getBgRes() {
            return bgRes;
        }
    }

    public static GradientDrawable genGradientDrawable(int color, int radiusDp){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(SystemUtil.dp2px(radiusDp));
        drawable.setColor(color);
        return drawable;
    }
}
