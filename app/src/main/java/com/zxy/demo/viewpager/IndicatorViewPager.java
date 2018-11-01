package com.zxy.demo.viewpager;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zxy.demo.R;

/**
 * Created by admin on 2018/11/a1.
 */

public class IndicatorViewPager extends FrameLayout {

    private final int MAX_COUNT = 9;

    private ViewPager mVpContent;
    private LinearLayout mLlIndicator;

    private int mNoSelectedId = R.drawable.shape_circle_stroke_white;
    private int mSelectedId = R.drawable.shape_circle_white;

    private int mCurrentIndex = 0;
    private int mAdapterCount = 0;
    private boolean mObservable = false;
    private DataSetObserver mDataSetObserver;

    public IndicatorViewPager(@NonNull Context context) {
        this(context, null, 0);
    }

    public IndicatorViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorViewPager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        removeAllViews();
        LayoutInflater.from(getContext()).inflate(R.layout.viewpager_indicator, this, true);
        mVpContent = findViewById(R.id.vp_content);
        mLlIndicator = findViewById(R.id.ll_indicator);
        initViewPager();
    }

    private void initViewPager() {
        mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                int count = mVpContent.getAdapter().getCount();
                if (mAdapterCount != count) {
                    mAdapterCount = count;
                    initIndicator(mAdapterCount);
                }
            }
        };
        mVpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ImageView iv = (ImageView) mLlIndicator.getChildAt(mCurrentIndex);
                iv.setImageResource(mNoSelectedId);
                ImageView selectedIv = (ImageView) mLlIndicator.getChildAt(position);
                selectedIv.setImageResource(mSelectedId);
                mCurrentIndex = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mVpContent.getAdapter() != null && mObservable == true) {
            mObservable = false;
            mVpContent.getAdapter().unregisterDataSetObserver(mDataSetObserver);
        }
    }

    public void setCurrentItem(int item) {
        mVpContent.setCurrentItem(item);
    }

    public void setAdapter(PagerAdapter adapter) {
        mVpContent.setAdapter(adapter);
        if (mVpContent.getAdapter() != null) {
            mAdapterCount = mVpContent.getAdapter().getCount();
            initIndicator(mAdapterCount);
            if (mObservable == false) {
                mObservable = true;
                mVpContent.getAdapter().registerDataSetObserver(mDataSetObserver);
            }
        }
    }

    public void setIndicatorDrawable(@IdRes int noSelectedId, @IdRes int selectedId) {
        mNoSelectedId = noSelectedId;
        mSelectedId = selectedId;
    }

    private void initIndicator(int count) {
        if (count == 0) return;
        removeAllViews();
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(pdToPx(12), 0, 0, 0);
            ImageView iv = new ImageView(getContext());
            iv.setLayoutParams(params);
            iv.setImageResource(mNoSelectedId);
            mLlIndicator.addView(iv);
        }
    }

    private int pdToPx(int dp) {
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return (int) (dp * dm.density);
    }
}
