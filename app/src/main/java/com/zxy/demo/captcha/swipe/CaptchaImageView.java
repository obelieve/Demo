package com.zxy.demo.captcha.swipe;

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

public class CaptchaImageView extends AppCompatImageView {

    public final int BLOCK_SIZE = 50;
    public final int ACCURACY_VALUE = 10;

    private int mBlockSize;
    private Paint mShadowPaint;
    private Paint mBlockPaint;
    private SwipeCaptchaHelper.EndCallback mEndCallback;
    //init
    private int mFailedCount;
    private Path mBlockShadowPath;
    private Bitmap mBlockBitmap;
    private BlockPosition mBlockShadowPosition;
    private BlockPosition mBlockPosition;
    private SwipeCaptchaHelper.Status mStatus = SwipeCaptchaHelper.Status.INIT;


    public CaptchaImageView(Context context) {
        super(context);
        init();
    }

    public CaptchaImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setEndCallback(SwipeCaptchaHelper.EndCallback callback) {
        mEndCallback = callback;
    }

    private void init() {
        mBlockSize = (int) (getResources().getDisplayMetrics().density * BLOCK_SIZE);
        mBlockPaint = new Paint();
        mShadowPaint = new Paint();
        mShadowPaint.setColor(Color.BLACK);
        mShadowPaint.setAlpha(165);
        mShadowPaint.setStyle(Paint.Style.FILL);
    }

    private void initCaptcha() {
        mFailedCount = 0;
        int offset = mBlockSize;
        int border = ACCURACY_VALUE + 1;
        int x = new Random().nextInt(getWidth() - offset - border) + border;
        int y = new Random().nextInt(getHeight() - offset);
        mBlockShadowPosition = new BlockPosition(x, y);
        mBlockPosition = new BlockPosition(0, mBlockShadowPosition.getY());
        mBlockShadowPath = getBlockShadowPath();
        mBlockShadowPath.offset(mBlockShadowPosition.x, mBlockShadowPosition.y);
        Bitmap lastBlockBitmap = mBlockBitmap;
        mBlockBitmap = getBlockBitmap(mBlockShadowPath);
        if (lastBlockBitmap != null) {//删除上一次滑块Bitmap
            lastBlockBitmap.recycle();
        }
        mStatus = SwipeCaptchaHelper.Status.START;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mStatus == SwipeCaptchaHelper.Status.INIT) {
            initCaptcha();
        }
        canvas.drawPath(mBlockShadowPath, mShadowPaint);
        canvas.drawBitmap(mBlockBitmap, mBlockPosition.x, mBlockPosition.y, mBlockPaint);
    }

    public void move(int progress) {
        if (progress < 0 || progress > 100) return;
        int distance = (int) (progress / (100 * 1.0f) * (getWidth() - mBlockSize));
        mBlockPosition.setX(distance);
        invalidate();
        mStatus = SwipeCaptchaHelper.Status.MOVE;
    }

    public void end() {
        if (Math.abs((mBlockPosition.x - mBlockShadowPosition.x)) <= ACCURACY_VALUE && Math.abs(mBlockPosition.y - mBlockShadowPosition.y) <= ACCURACY_VALUE) {
            mBlockPosition.x = mBlockShadowPosition.x;
            mBlockPosition.y = mBlockShadowPosition.y;
            invalidate();
            if (mEndCallback != null) {
                mEndCallback.onSuccess();
            }
        } else {
            mFailedCount++;
            if (mFailedCount >= SwipeCaptchaHelper.MAX_FAILED_COUNT) {
                if (mEndCallback != null) {
                    mEndCallback.onMaxFailed();
                }
            } else {
                if (mEndCallback != null) {
                    mEndCallback.onFailure();
                }
            }
        }
        mStatus = SwipeCaptchaHelper.Status.END;
    }

    public void resetBlock() {
        mBlockPosition.x = 0;
        invalidate();
    }

    public void reset() {
        initCaptcha();
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


    private Path getBlockShadowPath() {
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

}
