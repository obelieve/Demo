package com.zxy.demo.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.demo.base.App;
import com.zxy.demo.utils.StatusBarUtil;
import com.zxy.utility.LogUtil;

public class HomeTopView extends ConstraintLayout {

    private static int sRealHeight;
    private static int sDragHeight;
    private static int sMinTranslationY;
    private static int sMaxTranslationY;

    static {
        sRealHeight = App.getContext().getResources().getDimensionPixelSize(R.dimen.home_top_height);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            sRealHeight += StatusBarUtil.getStatusBarHeight();
        }
        sDragHeight = App.getContext().getResources().getDimensionPixelOffset(R.dimen.home_top_drag_height);
        sMinTranslationY = -sDragHeight;
        sMaxTranslationY = 0;
    }

    private TextView tv_place;
    private ImageView iv_msg;
    private View view_top;
    private View view_content;
    private View view_search;
    private ImageView iv_search;
    private TextView tv_search;
    private TabLayout tl_tab;


    public HomeTopView(Context context) {
        this(context, null, 0);
    }

    public HomeTopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static int getRealHeight() {
        return sRealHeight;
    }

    public static int getDragHeight() {
        return sDragHeight;
    }

    public static int getMinTranslationY() {
        return sMinTranslationY;
    }

    public static int getMaxTranslationY() {
        return sMaxTranslationY;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tv_place = findViewById(R.id.tv_place);
        iv_msg = findViewById(R.id.iv_msg);
        view_top = findViewById(R.id.view_top);
        view_content = findViewById(R.id.view_content);
        view_search = findViewById(R.id.view_search);
        iv_search = findViewById(R.id.iv_search);
        tv_search = findViewById(R.id.tv_search);
        tl_tab = findViewById(R.id.tl_tab);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            StatusBarUtil.fitsSystemWindows(this);
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) getLayoutParams();
            params.height = sRealHeight;
            setLayoutParams(params);
        }
    }

    @Override
    public void setTranslationY(float translationY) {
        super.setTranslationY(translationY);
        tv_place.setTranslationY(-translationY);
        iv_msg.setTranslationY(-translationY);
        if (translationY == getMinTranslationY()) {
            int color = ColorUtils.setAlphaComponent(Color.WHITE, 255);
            setBackgroundColor(color);
            view_search.setSelected(true);
            tl_tab.setTabTextColors(getResources().getColor(R.color.base_text_black),getResources().getColor(R.color.nav_text_selected));
            tl_tab.setSelectedTabIndicatorColor(getResources().getColor(R.color.nav_text_selected));
            tl_tab.setBackgroundColor(getResources().getColor(R.color.white));
        } else if (translationY == getMaxTranslationY()) {
            int color = ColorUtils.setAlphaComponent(Color.WHITE, 0);
            setBackgroundColor(color);
            view_search.setSelected(false);
            tl_tab.setTabTextColors(getResources().getColor(R.color.white),getResources().getColor(R.color.white));
            tl_tab.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
            tl_tab.setBackgroundColor(getResources().getColor(R.color.transparent));
        } else {
            int alpha = (int) (255 * (Math.abs(translationY * 1.0f / getMinTranslationY())));
            int color = ColorUtils.setAlphaComponent(Color.WHITE, alpha);
            setBackgroundColor(color);
            view_search.setSelected(false);
            tl_tab.setTabTextColors(getResources().getColor(R.color.base_text_black),getResources().getColor(R.color.nav_text_selected));
            tl_tab.setSelectedTabIndicatorColor(getResources().getColor(R.color.nav_text_selected));
            tl_tab.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//状态栏字体色
            if (translationY != 0) {
                StatusBarUtil.setWindowLightStatusBar((Activity) getContext(), true);
            } else {
                StatusBarUtil.setWindowLightStatusBar((Activity) getContext(), false);
            }
        }

    }
}
