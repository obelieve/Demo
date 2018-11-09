package com.zxy.demo.viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zxy.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自带指示下标ViewPager
 * Created by admin on 2018/11/a1.
 */

public class IndicatorViewPager extends FrameLayout {
    /**
     * 指示器最大数量
     */
    private final int MAX_COUNT = 9;
    //视图
    private ViewPager mVpContent;
    private LinearLayout mLlIndicator;
    //标识
    private int mCurrentIndex = -1;
    private int mAdapterCount = 0;
    private boolean mObservable = false;//监听ViewPager数据变化
    private DataSetObserver mDataSetObserver;
    private IndicatorViewPagerAdapter mViewPagerAdapter;
    //属性
    private int mIndicatorMarginRight;
    private int mIndicatorMarginBottom;
    private Drawable mSelectedDrawable;
    private Drawable mNoSelectedDrawable;


    public IndicatorViewPager(@NonNull Context context) {
        this(context, null, 0);
    }

    public IndicatorViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorViewPager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.IndicatorViewPager, defStyleAttr, 0);
        mSelectedDrawable = t.getDrawable(R.styleable.IndicatorViewPager_indicator_drawable_selected);
        mNoSelectedDrawable = t.getDrawable(R.styleable.IndicatorViewPager_indicator_drawable_no_selected);
        mIndicatorMarginRight = (int) t.getDimension(R.styleable.IndicatorViewPager_layout_indicatorMarginRight, pdToPx(20));
        mIndicatorMarginBottom = (int) t.getDimension(R.styleable.IndicatorViewPager_layout_indicatorMarginBottom, pdToPx(20));

        ((FrameLayout.LayoutParams) mLlIndicator.getLayoutParams()).setMargins(0, 0, mIndicatorMarginRight, mIndicatorMarginBottom);
        if (mSelectedDrawable == null) {
            mSelectedDrawable = context.getResources().getDrawable(R.drawable.shape_circle_white);
        }
        if (mNoSelectedDrawable == null) {
            mNoSelectedDrawable = context.getResources().getDrawable(R.drawable.shape_circle_stroke_white);
        }
        t.recycle();
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
                int count = mViewPagerAdapter.getOriginalCount();
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
                setCurrentIndicator(position % mViewPagerAdapter.getOriginalCount());

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 设置页面缓存
     *
     * @param limit
     */
    public void setOffscreenPageLimit(int limit)
    {
        mVpContent.setOffscreenPageLimit(limit);
    }

    /**
     * 设置屏幕显示多页效果
     *
     * @param pageMargin 页面间距
     */
    public void setPageSpan(int pageMargin)
    {
        setClipChildren(false);
        mVpContent.setClipChildren(false);
        mVpContent.setPageMargin(pageMargin);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mVpContent.getLayoutParams();
        params.setMargins(pageMargin * 2, 0, pageMargin * 2, 0);
        mVpContent.setLayoutParams(params);
    }

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener)
    {
        mVpContent.addOnPageChangeListener(listener);
    }

    private void setCurrentIndicator(int position) {
        if (mCurrentIndex != -1) {
            ImageView iv = (ImageView) mLlIndicator.getChildAt(mCurrentIndex);
            iv.setImageDrawable(mNoSelectedDrawable);
        }
        ImageView selectedIv = (ImageView) mLlIndicator.getChildAt(position);
        selectedIv.setImageDrawable(mSelectedDrawable);
        mCurrentIndex = position;
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
        mVpContent.setCurrentItem(item % mViewPagerAdapter.getOriginalCount() + mViewPagerAdapter.getOriginalCount() * 3 * mViewPagerAdapter.getOffscreenPageLimit() * 1000);
        setCurrentIndicator(item % mViewPagerAdapter.getOriginalCount());
    }

    public void setCurrentItem(int item, boolean smoothScroll)
    {
        mVpContent.setCurrentItem(item % mViewPagerAdapter.getOriginalCount() + mViewPagerAdapter.getOriginalCount() * 3 * mViewPagerAdapter.getOffscreenPageLimit() * 1000, smoothScroll);
        setCurrentIndicator(item % mViewPagerAdapter.getOriginalCount());
    }

    public int getCurrentItem()
    {
        return mVpContent.getCurrentItem();
    }

    public void setAdapter(IndicatorViewPagerAdapter adapter)
    {
        if (adapter == null) return;
        if (adapter.getOriginalCount() > MAX_COUNT)
        {
            throw new RuntimeException("IndicatorViewPager adapter max count is 9 !");
        }
        mViewPagerAdapter = adapter;
        mVpContent.setAdapter(mViewPagerAdapter);
        mAdapterCount = adapter.getOriginalCount();
        initIndicator(mAdapterCount);
        if (mObservable == false) {
            mObservable = true;
            mVpContent.getAdapter().registerDataSetObserver(mDataSetObserver);
        }
    }

    public void setIndicatorNoSelectedDrawable(@DrawableRes int noSelectedId) {
        mNoSelectedDrawable = getContext().getResources().getDrawable(noSelectedId);
        initIndicator(mAdapterCount);
    }

    public void setIndicatorSelectedDrawable(@DrawableRes int selectedId) {
        mSelectedDrawable = getContext().getResources().getDrawable(selectedId);
        initIndicator(mAdapterCount);
    }

    public void setIndicatorMarginRight(int marginRight) {
        mIndicatorMarginRight = marginRight;
        ((FrameLayout.LayoutParams) mLlIndicator.getLayoutParams()).setMargins(0, 0, mIndicatorMarginRight, mIndicatorMarginBottom);
    }

    public void setIndicatorMarginBottom(int marginBottom) {
        mIndicatorMarginBottom = marginBottom;
        ((FrameLayout.LayoutParams) mLlIndicator.getLayoutParams()).setMargins(0, 0, mIndicatorMarginRight, mIndicatorMarginBottom);
    }

    private void initIndicator(int count) {
        if (count == 0) return;
        mLlIndicator.removeAllViews();
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(pdToPx(12), 0, 0, 0);
            ImageView iv = new ImageView(getContext());
            iv.setLayoutParams(params);
            if (mCurrentIndex == i) {
                iv.setImageDrawable(mSelectedDrawable);
            } else {
                iv.setImageDrawable(mNoSelectedDrawable);
            }
            mLlIndicator.addView(iv);
        }
    }

    private int pdToPx(int dp) {
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return (int) (dp * dm.density);
    }

    public static abstract class IndicatorViewPagerAdapter<T> extends PagerAdapter
    {
        private List<T> mList;
        private List<View> mViews;

        public abstract View getNewInstance(T t);

        public abstract int getOffscreenPageLimit();

        public IndicatorViewPagerAdapter(List<T> list)
        {
            this.mList = list;
            mViews = new ArrayList<>();
            if (mList != null)
            {
                for (int i = 0; i < mList.size() * 3 * getOffscreenPageLimit(); i++)
                {
                    mViews.add(getNewInstance(mList.get(i % mList.size())));
                }
            }
        }

        @Override
        public int getCount()
        {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object)
        {

            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position)
        {
            View view = mViews.get(position % mViews.size());
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object)
        {
            View view = mViews.get(position % mViews.size());
            container.removeView(view);
        }


        public int getOriginalCount()
        {
            if (mList != null)
            {
                return mList.size();
            }
            return 0;
        }
    }
}
