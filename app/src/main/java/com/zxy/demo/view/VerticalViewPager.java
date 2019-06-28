package com.zxy.demo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.Serializable;

public class VerticalViewPager extends ViewPager implements Serializable {

    private int baseScrollX;
    private int currentScrollState;

    public VerticalViewPager(Context context) {
        this(context, null);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        setPageTransformer(false, new DefaultTransformer());// vertical scroll trick
        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    baseScrollX = getScrollX();
                }
                currentScrollState = state;
            }
        });

    }

    public int getBaseScrollX() {
        return baseScrollX;
    }

    public void setBaseScrollX(int baseScrollX) {
        this.baseScrollX = baseScrollX;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (currentScrollState == ViewPager.SCROLL_STATE_IDLE) {
            baseScrollX = getScrollX();
        }
    }

    /**
     * Fake drag {@link VerticalViewPager} <br>
     * Usage: set this to be the  {@link View.OnTouchListener} of {@link #VerticalViewPager}'s children <br>
     * Created by chadguo
     */
    public static class VerticalVPOnTouchListener implements View.OnTouchListener {
        private static final String TAG = "VerticalViewPager";
        private VerticalViewPager mVerticalViewPager;//the container ViewPager
        private float lastMotionY = Float.MIN_VALUE;
        private float downY = Float.MIN_VALUE;

        /**
         * @param viewPager the container
         */
        public VerticalVPOnTouchListener(VerticalViewPager viewPager) {
            this.mVerticalViewPager = viewPager;
        }

        /**
         * 1.dispatch ACTION_DOWN,ACTION_UP,ACTION_CANCEL to child<br>
         * 2.hack ACTION_MOVE
         *
         * @param v
         * @param e
         * @return
         */
        @Override
        public boolean onTouch(View v, MotionEvent e) {
            //Log.i(TAG, "onTouchEvent " + ", action " + e.getAction() + ", e.rawY " + e.getRawY() + ",lastMotionY " + lastMotionY + ",downY " + downY);
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downY = e.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:

                    if (downY == Float.MIN_VALUE && lastMotionY == Float.MIN_VALUE) {
                        //not start from MOTION_DOWN, the child dispatch this first motion
                        downY = e.getRawY();
                        break;
                    }

                    float diff = e.getRawY() - (lastMotionY == Float.MIN_VALUE ? downY : lastMotionY);
                    lastMotionY = e.getRawY();
                    diff = diff / 2; //slow down viewpager scroll
                    //Log.e(TAG, "scrollX " + mVerticalViewPager.getScrollX() + ",basescrollX " + mVerticalViewPager.getBaseScrollX());

                    if (mVerticalViewPager.getScrollX() != mVerticalViewPager.getBaseScrollX()) {
                        if (fakeDragVp(v, e, diff)) return true;
                    } else {
                        if (ViewCompat.canScrollVertically(v, (-diff) > 0 ? 1 : -1)) {
                            //Log.e(TAG, "scroll vertically  " + diff + ", move.lastMotionY " + e.getY());
                            break;
                        } else {
                            mVerticalViewPager.beginFakeDrag();
                            fakeDragVp(v, e, diff);
                            adjustDownMotion(v, e);
                            return true;
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (mVerticalViewPager.isFakeDragging()) {
                        try {
                            mVerticalViewPager.endFakeDrag();
                        } catch (Exception e1) {
                            //Log.e(TAG, "", e1);
                        }
                    }
                    reset();
                    break;
            }

            return false;
        }

        private boolean fakeDragVp(View v, MotionEvent e, float diff) {
            if (mVerticalViewPager.isFakeDragging()) {
                float step = diff;
                int expScrollX = (int) (mVerticalViewPager.getScrollX() - step);
                if (isDiffSign(expScrollX - mVerticalViewPager.getBaseScrollX(), mVerticalViewPager.getScrollX() - mVerticalViewPager.getBaseScrollX())) {
                    step = mVerticalViewPager.getScrollX() - mVerticalViewPager.getBaseScrollX();
                }
                mVerticalViewPager.fakeDragBy(step);
                //Log.e(TAG, "fake drag, diff " + diff + ",step " + step + ",scrollX " + mVerticalViewPager.getScrollX());
                adjustDownMotion(v, e);

                return true;
            }
            return false;
        }

        /**
         * fake a ACTION_DOWN to adjust child's motion track
         *
         * @param v
         * @param e
         */
        private void adjustDownMotion(View v, MotionEvent e) {
            MotionEvent fakeDownEvent = MotionEvent.obtain(e);
            fakeDownEvent.setAction(MotionEvent.ACTION_DOWN);
            v.dispatchTouchEvent(fakeDownEvent);
        }

        private boolean isDiffSign(float a, float b) {
            return Math.abs(a + b) < Math.abs(a - b);
        }


        public void reset() {
            downY = Float.MIN_VALUE;
            lastMotionY = Float.MIN_VALUE;
        }
    }

    public static class DefaultTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {
            float alpha = 0;
            if (0 <= position && position <= 1) {
                alpha = 1 - position;
            } else if (-1 < position && position < 0) {
                alpha = position + 1;
            }
            view.setAlpha(alpha);
            view.setTranslationX(view.getWidth() * -position);
            float yPosition = position * view.getHeight();
            view.setTranslationY(yPosition);
        }
    }

    public static class StackTransformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View page, float position) {
            page.setTranslationX(page.getWidth() * -position);
            page.setTranslationY(position < 0 ? position * page.getHeight() : 0f);
        }
    }

    public static class ZoomOutTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.90f;

        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();
            float alpha = 0;
            if (0 <= position && position <= 1) {
                alpha = 1 - position;
            } else if (-1 < position && position < 0) {
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float verticalMargin = pageHeight * (1 - scaleFactor) / 2;
                float horizontalMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horizontalMargin - verticalMargin / 2);
                } else {
                    view.setTranslationX(-horizontalMargin + verticalMargin / 2);
                }

                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                alpha = position + 1;
            }

            view.setAlpha(alpha);
            view.setTranslationX(view.getWidth() * -position);
            float yPosition = position * view.getHeight();
            view.setTranslationY(yPosition);
        }

    }

}
