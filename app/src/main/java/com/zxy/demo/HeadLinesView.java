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
    int SWITCH_HEIGHT = 200;

    int SWITCH_DURATION = 2000;
    int ANIMATION_DURATION = 1000;
    boolean isStop = false;
    int currentPosition = 0;

    BaseAdapter mAdapter;
    View firstView, secondView;

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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SWITCH_HEIGHT);
        addView(firstView, params);
        addView(secondView, params);
        postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(firstView, "translationY", firstView.getTranslationY() - SWITCH_HEIGHT);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(secondView, "translationY", secondView.getTranslationY() - SWITCH_HEIGHT);
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
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SWITCH_HEIGHT);
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
