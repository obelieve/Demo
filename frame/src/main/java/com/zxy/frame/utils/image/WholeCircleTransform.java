package com.zxy.frame.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.zxy.frame.utils.info.SystemInfoUtil;

import java.security.MessageDigest;

/**
 * 图片进行缩放到一个圆的区域进行显示 不进行裁翦
 * Created by Admin
 * on 2020/6/30
 */
public class WholeCircleTransform extends BitmapTransformation {

    private Context mContext;
    private int mSize;
    private int mRadius;
    private final String ID = "com.zxy.frame.utils.image.WholeCircleTransform";
    private final byte[] ID_BYTES = ID.getBytes(CHARSET);

    /**
     * @param size 显示的尺寸
     */
    public WholeCircleTransform(Context context, int size) {
        mContext = context;
        mSize = size;
        mRadius = size / 2;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        int mSize = SystemInfoUtil.dp2px(mContext, 65);
        int mRadius = mSize / 2;
        float scale = mSize / (float) Math.sqrt(Math.pow(toTransform.getWidth(), 2) + Math.pow(toTransform.getHeight(), 2));
        int width = (int) (scale * toTransform.getWidth());
        int height = (int) (scale * toTransform.getHeight());
        float marginL = (mSize - width) / 2;
        float marginT = (mSize - height) / 2;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap image = Bitmap.createScaledBitmap(toTransform, width, height, true);
        final Bitmap result = Bitmap.createBitmap(mSize, mSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawCircle(mRadius, mRadius, mRadius, paint);
        canvas.drawBitmap(image, marginL, marginT, paint);
        return result;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
