package com.github.obelieve.utils.others;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.github.obelieve.App;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.zxy.frame.utils.SystemInfoUtil;

@SuppressLint("RestrictedApi")
public class CustomSmartRefreshHeader extends LinearLayout implements RefreshHeader {

    private LottieAnimationView mProgressView;//刷新动画视图

    public CustomSmartRefreshHeader(Context context) {
        super(context);
        initView(context);
    }

    public CustomSmartRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public CustomSmartRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }

    private void initView(Context context) {
        setGravity(Gravity.CENTER);

        mProgressView = new LottieAnimationView(context);
        mProgressView.setImageAssetsFolder("refresh/images");
        mProgressView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mProgressView.setAnimation("refresh/refresh.json");
        mProgressView.setRepeatCount(Animation.INFINITE);
        addView(mProgressView, SystemInfoUtil.dp2px(App.getContext(), 40), SystemInfoUtil.dp2px(App.getContext(), 40));
        setMinimumHeight(SystemInfoUtil.dp2px(App.getContext(), 50));
    }

    @NonNull
    public View getView() {
        return this;//真实的视图就是自己，不能返回null
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int headHeight, int maxDragHeight) {
        mProgressView.playAnimation();//开始动画
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        mProgressView.cancelAnimation();//停止动画
        return 0;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
            case Refreshing:
                mProgressView.setVisibility(VISIBLE);//隐藏动画
                break;
            case ReleaseToRefresh:
                break;
        }
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int maxDragHeight) {
    }

    @Override
    public void onPulling(float percent, int offset, int height, int extendHeight) {
        
    }

    @Override
    public void onReleasing(float percent, int offset, int height, int extendHeight) {

    }
    

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
    }

    @Override
    public void setPrimaryColors(@ColorInt int... colors) {
    }
}
