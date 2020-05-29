package com.zxy.frame.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 设置控件背景阴影均匀分布效果
 * Created by Admin
 * on 2020/5/29
 */
public class ShadowDrawable extends Drawable {

    private static Builder sBuilder = new Builder();

    private ShadowDrawable() {
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        sBuilder.mRectF = new RectF(left + sBuilder.mShadowRadius, top + sBuilder.mShadowRadius, right - sBuilder.mShadowRadius, bottom - sBuilder.mShadowRadius);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawRoundRect(sBuilder.mRectF, sBuilder.mRadius, sBuilder.mRadius, sBuilder.mShadowPaint);
        canvas.drawRoundRect(sBuilder.mRectF, sBuilder.mRadius, sBuilder.mRadius, sBuilder.mBgPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        sBuilder.mShadowPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        sBuilder.mShadowPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public static Builder gen() {
        return sBuilder;
    }

    public static class Builder {
        private static final int RADIUS = 10;
        private static final String SHADOW_COLOR = "#99999999";

        private RectF mRectF;
        private int mRadius;
        private int mShadowRadius;

        private int mBgColor;
        private int mShadowColor;

        private Paint mShadowPaint;
        private Paint mBgPaint;

        private Builder() {

        }

        public Builder radius(int radiusDp) {
            sBuilder.mRadius = radiusDp;
            return sBuilder;
        }

        public Builder shadowRadius(int radiusDp) {
            sBuilder.mShadowRadius = radiusDp;
            return sBuilder;
        }

        public Builder shadowColor(int shadowColor) {
            sBuilder.mShadowColor = shadowColor;
            return sBuilder;
        }

        public Builder backgroundColor(int bgColor) {
            sBuilder.mBgColor = bgColor;
            return sBuilder;
        }

        public ShadowDrawable build(Context context) {
            init(context);
            return new ShadowDrawable();
        }

        private void init(Context context) {
            float density = context.getResources().getDisplayMetrics().density;
            mShadowColor = mShadowColor != 0 ? mShadowColor : Color.parseColor(SHADOW_COLOR);
            mBgColor = mBgColor != 0 ? mBgColor : Color.WHITE;
            mRadius = (int) (mRadius * density);
            mShadowRadius = mShadowRadius != 0 ? (int) (mShadowRadius * density) : (int) (RADIUS * density);
            mShadowPaint = new Paint();
            mShadowPaint.setAntiAlias(true);
            mShadowPaint.setColor(Color.TRANSPARENT);//防止mBgPaint绘制覆盖时，圆角出现黑边
            mBgPaint = new Paint();
            mShadowPaint.setShadowLayer(mShadowRadius, 0, 0, mShadowColor);
            mBgPaint.setAntiAlias(true);
            mBgPaint.setColor(mBgColor);
        }
    }

}