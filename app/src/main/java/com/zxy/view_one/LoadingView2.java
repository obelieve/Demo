package com.zxy.view_one;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.zxy.demo.R;

/**
 * Created by zxy on 2018/11/30 10:18.
 */

public class LoadingView2 extends View
{
    private int mCurrentIndex;
    private Paint mDefPaint, mPointPaint;

    private int mDefRadius;
    private int mPointRadius;
    private int mLoadingContentHeight;

    public LoadingView2(Context context)
    {
        super(context);
        init();
    }

    public LoadingView2(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LoadingView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        mDefPaint = new Paint();
        mDefPaint.setColor(getResources().getColor(R.color.circle_light_yellow));
        mDefPaint.setAntiAlias(true);
        mDefPaint.setStyle(Paint.Style.FILL);

        mPointPaint = new Paint();
        mPointPaint.setColor(getResources().getColor(R.color.circle_yellow));
        mPointPaint.setAntiAlias(true);
        mDefPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        switch (widthMode)
        {
            case MeasureSpec.AT_MOST:
                widthSize = dp2px(100);
                break;
            case MeasureSpec.EXACTLY:
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        switch (heightMode)
        {
            case MeasureSpec.AT_MOST:
                heightSize = widthSize/10;
                break;
            case MeasureSpec.EXACTLY:
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        setMeasuredDimension(widthSize, heightSize);
        initDrawParams(widthSize);
    }

    private void initDrawParams(int widthSize)
    {
        mDefRadius = (int) (widthSize *0.6f/ 20 );
        mPointRadius = widthSize / 20;
        mLoadingContentHeight = mPointRadius * 2;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
//        Paint paint = new Paint();
//        paint.setColor(Color.TRANSPARENT);
//        canvas.drawRect(new Rect(0, 0, dp2px(100), dp2px(mDefRadius * 2)), paint);
//        canvas.drawCircle(dp2px(5), dp2px(5), dp2px(3), mDefPaint);
//        canvas.drawCircle(dp2px(10 + 20 + 5), dp2px(5), dp2px(3), mDefPaint);
//        canvas.drawCircle(dp2px(10 * 2 + 20 * 2 + 5), dp2px(5), dp2px(3), mDefPaint);
//        canvas.drawCircle(dp2px(10 * 3 + 20 * 3 + 5), dp2px(5), dp2px(3), mDefPaint);
//        canvas.drawCircle(dp2px((mCurrentIndex % 4) * 30 + 5), dp2px(5), dp2px(5), mPointPaint);

        canvas.drawCircle(mPointRadius, mPointRadius, mDefRadius, mDefPaint);
        canvas.drawCircle(mLoadingContentHeight + mLoadingContentHeight * 2 + mPointRadius, mPointRadius, mDefRadius, mDefPaint);
        canvas.drawCircle(mLoadingContentHeight * 2 + mLoadingContentHeight * 2 * 2 + mPointRadius, mPointRadius, mDefRadius, mDefPaint);
        canvas.drawCircle(mLoadingContentHeight * 3 + mLoadingContentHeight * 2 * 3 + mPointRadius, mPointRadius, mDefRadius, mDefPaint);
        canvas.drawCircle((mCurrentIndex % 4) * (mLoadingContentHeight * 3) + mPointRadius, mPointRadius, mPointRadius, mPointPaint);

        postInvalidateDelayed(1000);
    }


    @Override
    public void invalidate()
    {
        super.invalidate();
        mCurrentIndex++;
    }

    private int dp2px(int dp)
    {
        return (int) (dp * density());
    }

    private float density()
    {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }
}
