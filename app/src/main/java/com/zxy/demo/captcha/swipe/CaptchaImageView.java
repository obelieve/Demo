package com.zxy.demo.captcha.swipe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.Random;

public class CaptchaImageView extends AppCompatImageView {

    private final int BLOCK_SIZE = 50;
    private final int DRAW_SUCCESS_AREA_SIZE = 100;
    private final int ACCURACY_VALUE = 10;

    private int mBlockSize;
    private int mDrawSuccessAreaSize;
    private Paint mShadowPaint;
    private Paint mBlockPaint;
    private Paint mDrawSuccessPaint;
    private SwipeCaptchaHelper.EndCallback mEndCallback;
    //init
    private int mWidth;
    private int mHeight;
    private int mFailedCount;
    private Path mBlockShadowPath;
    private Bitmap mBlockBitmap;
    private BlockPosition mBlockShadowPosition;
    private BlockPosition mBlockPosition;
    //动画绘制参数
    private boolean mDrawBlock = true;
    private boolean mDrawSuccess = false;
    private float mDrawSuccessOffset;
    private ValueAnimator mFailureAnimator;
    private ValueAnimator mSuccessAnimator;

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
        mDrawSuccessAreaSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DRAW_SUCCESS_AREA_SIZE, getContext().getResources().getDisplayMetrics());
        mBlockPaint = new Paint();
        mShadowPaint = new Paint();
        mShadowPaint.setColor(Color.BLACK);
        mShadowPaint.setAlpha(165);
        mShadowPaint.setStyle(Paint.Style.FILL);
        mDrawSuccessPaint = new Paint();
        mDrawSuccessPaint.setColor(Color.WHITE);
        mDrawSuccessPaint.setAlpha(165);
        mDrawSuccessPaint.setStyle(Paint.Style.FILL);
    }

    private void initCaptcha() {
        mFailedCount = 0;
        mWidth = getWidth();
        mHeight = getHeight();
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
        mFailureAnimator = ValueAnimator.ofFloat(0, 1);
        mFailureAnimator.setDuration(100).setRepeatCount(4);
        mFailureAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mFailureAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mDrawBlock = true;
                captchaFailure();
            }
        });
        mFailureAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (value < 0.5) {
                    mDrawBlock = false;
                } else {
                    mDrawBlock = true;
                }
                invalidate();
            }
        });
        mSuccessAnimator = ValueAnimator.ofFloat(mWidth + mDrawSuccessAreaSize, 0);
        mSuccessAnimator.setDuration(500);
        mSuccessAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mDrawSuccess = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mDrawSuccess = false;
                captchaSuccess();
            }
        });
        mSuccessAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mDrawSuccessOffset = value;
                invalidate();
            }
        });
        mStatus = SwipeCaptchaHelper.Status.START;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mStatus == SwipeCaptchaHelper.Status.INIT) {
            initCaptcha();
        }
        canvas.drawPath(mBlockShadowPath, mShadowPaint);
        if (mDrawBlock) {
            canvas.drawBitmap(mBlockBitmap, mBlockPosition.x, mBlockPosition.y, mBlockPaint);
        }
        if (mDrawSuccess) {
            Path path = getDrawSuccessPath(mDrawSuccessAreaSize, mHeight);
            path.offset(mDrawSuccessOffset, 0);
            canvas.drawPath(path, mDrawSuccessPaint);
        }
    }

    public void move(int progress) {
        if (progress < 0 || progress > 100) return;
        int distance = (int) (progress / (100 * 1.0f) * (getWidth() - mBlockSize));
        mBlockPosition.setX(distance);
        invalidate();
        mStatus = SwipeCaptchaHelper.Status.MOVE;
    }

    public void end() {
        if (mStatus != SwipeCaptchaHelper.Status.MOVE) return;
        if (Math.abs((mBlockPosition.x - mBlockShadowPosition.x)) <= ACCURACY_VALUE && Math.abs(mBlockPosition.y - mBlockShadowPosition.y) <= ACCURACY_VALUE) {
            mBlockPosition.x = mBlockShadowPosition.x;
            mBlockPosition.y = mBlockShadowPosition.y;
            invalidate();
            mSuccessAnimator.start();
        } else {
            mFailedCount++;
            mFailureAnimator.start();
        }
        mStatus = SwipeCaptchaHelper.Status.END;
    }

    private void captchaSuccess() {
        if (mEndCallback != null) {
            mEndCallback.onSuccess();
        }
    }

    private void captchaFailure() {
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

    private Path getDrawSuccessPath(int size, int height) {
        Path path = new Path();
        path.moveTo(size, 0);
        path.rLineTo(size / 2, height);
        path.rLineTo(-size, 0);
        path.rLineTo(-size / 2, -height);
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
