package com.zxy.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.Random;

public class SwipeVerificationImageView extends AppCompatImageView {

    public final int BLOCK_SIZE = 50;
    public final int ACCURACY_VALUE = 10;

    private int mBlockSize;
    private Paint mShadowPaint;
    private Paint mBlockPaint;
    private BlockPosition mBlockShadowPosition;
    private BlockPosition mBlockPosition;
    private Callback mCallback;

    public SwipeVerificationImageView(Context context) {
        super(context);
        init();
    }

    public SwipeVerificationImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    private void init() {
        mBlockSize = (int) (getResources().getDisplayMetrics().density * BLOCK_SIZE);
        mBlockPaint = new Paint();
        mShadowPaint = new Paint();
        mShadowPaint.setColor(Color.BLACK);
        mShadowPaint.setAlpha(165);
        mShadowPaint.setStyle(Paint.Style.FILL);
    }

    private void initPosition() {
        mBlockShadowPosition = getShadowPosition();
        mBlockPosition = getBlockPosition(true);
        if (Math.abs(mBlockShadowPosition.x - mBlockPosition.x) <= BLOCK_SIZE) {
            initPosition();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initPosition();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = getBlockPath();
        path.offset(mBlockShadowPosition.x, mBlockShadowPosition.y);
        Bitmap blockBitmap = getBlockBitmap(path);
        canvas.drawPath(path, mShadowPaint);
        canvas.drawBitmap(blockBitmap, mBlockPosition.x, mBlockPosition.y, mBlockPaint);
    }

    public void move(int progress) {
        if (progress < 0 || progress > 100) return;
        int distance = (int) (progress / (100 * 1.0f) * (getWidth() - mBlockSize));
        mBlockPosition.setX(distance);
        invalidate();
    }

    public void up() {
        if (Math.abs((mBlockPosition.x - mBlockShadowPosition.x)) <= ACCURACY_VALUE && Math.abs(mBlockPosition.y - mBlockShadowPosition.y) <= ACCURACY_VALUE) {
            mBlockPosition.x = mBlockShadowPosition.x;
            mBlockPosition.y = mBlockShadowPosition.y;
            invalidate();
            if(mCallback!=null){
                mCallback.onSuccess();
            }
        }else{
            if(mCallback!=null){
                mCallback.onFailure();
            }
        }
    }

    public void reset() {
        initPosition();
        invalidate();
    }

    /**
     * 绘制滑动块 Bitmap
     *
     * @param path
     * @return
     */
    private Bitmap getBlockBitmap(Path path) {
        /**
         * 获取剪切后的图片bitmap（整个bitmap只有滑动块才有渲染）
         */
        Bitmap tempBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.clipPath(path);
        getDrawable().draw(tempCanvas);
        /**
         * 定位这个滑动块的位置，然后只获取滑动块的bitmap
         */
        Bitmap bitmap = Bitmap.createBitmap(tempBitmap, mBlockShadowPosition.x, mBlockShadowPosition.y, mBlockSize, mBlockSize);
        tempBitmap.recycle();
        return bitmap;
    }


    /**
     * 获取滑动块阴影位置（滑动块原有的位置）
     *
     * @return
     */
    private BlockPosition getShadowPosition() {
        int offset = -mBlockSize;
        int x = new Random().nextInt(getWidth() + offset);
        int y = new Random().nextInt(getHeight() + offset);
        return new BlockPosition(x, y);
    }

    /**
     * 获取滑动块的位置
     *
     * @param swipe 是否滑动方式
     * @return
     */
    private BlockPosition getBlockPosition(boolean swipe) {
        if (mBlockShadowPosition == null) {
            return new BlockPosition(0, 0);
        }
        int offset = -mBlockSize;
        int x;
        int y;
        if (swipe) {
            x = 0;
            y = mBlockShadowPosition.getY();
        } else {
            x = new Random().nextInt(getWidth() + offset);
            y = new Random().nextInt(getHeight() + offset);
        }
        return new BlockPosition(x, y);
    }

    private Path getBlockPath() {
        Path path = new Path();
        int gap = mBlockSize / 3;
        path.moveTo(0, gap);
        path.rLineTo(gap, 0);
        path.rLineTo(0, -gap);
        path.rLineTo(gap, 0);
        path.rLineTo(0, gap);
        path.rLineTo(gap, 0);
        path.rLineTo(0, gap * 2);
        path.rLineTo(-mBlockSize, 0);
        path.rLineTo(0, -gap * 2);
        path.close();
        return path;
    }


    public static class BlockPosition {
        private int x;
        private int y;

        public BlockPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    public interface Callback{
        void onSuccess();
        void onFailure();
    }
}
