package com.zxy.demo;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * Created by zxy on 2018/10/12 17:39.
 */

public class HeadLinesView extends LinearLayout
{
    int SWITCH_HEIGHT = 45;

    int SWITCH_DURATION = 3000;
    int ANIMATION_DURATION = 1500;
    boolean isStop = false;
    int currentPosition = 0;

    BaseAdapter mAdapter;
    View firstView, secondView;

    int childHeight;

    public HeadLinesView(Context context)
    {
        super(context);
    }

    public HeadLinesView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        setOrientation(VERTICAL);
    }


    public void setAdapter(BaseAdapter adapter)
    {
        mAdapter = adapter;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        switch (mode)
        {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                getChildAt(0).measure(widthMeasureSpec, heightMeasureSpec);
                childHeight = getChildAt(0).getMeasuredHeight();
                //childHeight = DeviceInfoUtil.dpToPx(SWITCH_HEIGHT);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, mode);
                break;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void start()
    {
        removeAllViews();
        firstView = mAdapter.getView(currentPosition, null, this);
        if (mAdapter.getCount() < 2)
        {
            return;
        }
        currentPosition = currentPosition + 1;
        secondView = mAdapter.getView(currentPosition, null, this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(firstView, params);
        addView(secondView, params);
        postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(firstView, "translationY", firstView.getTranslationY() - childHeight);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(secondView, "translationY", secondView.getTranslationY() - childHeight);
                AnimatorSet set = new AnimatorSet();
                set.playTogether(animator1, animator2);
                set.setDuration(ANIMATION_DURATION);
                set.addListener(new Animator.AnimatorListener()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        firstView.setTranslationY(0);
                        secondView.setTranslationY(0);

                        removeView(firstView);
                        firstView = secondView;

                        currentPosition++;
                        secondView = mAdapter.getView(currentPosition % mAdapter.getCount(), null, HeadLinesView.this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, childHeight);
                        addView(secondView, params);
                    }

                    @Override
                    public void onAnimationStart(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation)
                    {

                    }

                });
                set.start();
                if (!isStop)
                {
                    postDelayed(this, SWITCH_DURATION);
                }
            }
        }, SWITCH_DURATION);
    }

    public void stop()
    {
        isStop = true;
    }
}
