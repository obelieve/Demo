package com.zxy.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.zxy.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashView extends FrameLayout {

    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.view_indicator)
    IndicatorView viewIndicator;
    @BindView(R.id.tv_next)
    TextView tvNext;

    PagerAdapter mAdapter;
    int[] resInts = new int[]{R.drawable.splash_1, R.drawable.splash_2, R.drawable.splash_3};
    Callback mCallback;

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public SplashView(@NonNull Context context) {
        this(context, null);
    }

    public SplashView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_splash, this, true);
        ButterKnife.bind(this, view);
        ImageView[] views = new ImageView[resInts.length];
        for (int i = 0; i < views.length; i++) {
            views[i] = new ImageView(getContext());
            views[i].setLayoutParams(new ViewPager.LayoutParams());
            views[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
            views[i].setImageResource(resInts[i]);
        }
        viewIndicator.setCountAndIndex(resInts.length, 0)
                .setIndicatorGap(5)
                .setSelectedDrawable(R.drawable.bg_indicator_main)
                .setUnSelectedDrawable(R.drawable.bg_indicator_cccccc)
                .build();
        mAdapter = new PagerAdapter() {

            @Override
            public int getCount() {
                return resInts.length;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                container.addView(views[position]);
                return views[position];
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        };
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewIndicator.refreshCurrentIndex(position);
                if (position == resInts.length - 1) {
                    tvNext.setVisibility(VISIBLE);
                    viewIndicator.setVisibility(GONE);
                } else {
                    tvNext.setVisibility(GONE);
                    viewIndicator.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vpContent.setAdapter(mAdapter);
    }

    @OnClick(R.id.tv_next)
    public void onViewClicked() {
        if(mCallback!=null){
            mCallback.onNext();
        }
    }

    public interface Callback {
        void onNext();
    }
}
