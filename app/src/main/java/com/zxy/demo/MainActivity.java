package com.zxy.demo;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.zxy.utility.LogUtil;
import com.zxy.utility.SystemUtil;

public class MainActivity extends AppCompatActivity {

    AppBarLayout appbar;
    LinearLayout ll_content;
    ViewPager vp_content;
    Class[] mClasses = new Class[]{MainFragment.class, Fragment2.class, Fragment3.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar(false);
        SystemUtil.init(getApplicationContext());
        LogUtil.e("windows width =" + getResources().getDisplayMetrics().widthPixels + "  windows Height=" + getResources().getDisplayMetrics().heightPixels);
        LogUtil.e("nav = " + SystemUtil.getNavigationHeight() + " statusBar = " + SystemUtil.getStatusBarHeight());
        setContentView(R.layout.activity_main2);
        AndroidBug5497Workaround.assistActivity(findViewById(android.R.id.content));
        appbar = findViewById(R.id.appbar);
        ll_content = findViewById(R.id.ll_content);
        vp_content = findViewById(R.id.vp_content);
        vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                collapsing(position != 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp_content.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                try {
                    return (Fragment) mClasses[position].newInstance();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
                return new Fragment3();
            }

            @Override
            public int getCount() {
                return mClasses.length;
            }
        });

    }

    private void initStatusBar(boolean show) {
        if (show) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 设置状态栏底色白色
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().setStatusBarColor(Color.WHITE);
                // 设置状态栏字体黑色
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }


    public void collapsing(boolean collapsing) {
        int i0 = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL;
        int i1 = AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED;
        View appBarChildAt = appbar.getChildAt(0);
        AppBarLayout.LayoutParams appBarParams = (AppBarLayout.LayoutParams) appBarChildAt.getLayoutParams();
        if (collapsing) {
            appBarParams.setScrollFlags(i0 | i1);// 重置折叠效果
        } else {
            appBarParams.setScrollFlags(0);//这个加了之后不可滑动
        }
        appBarChildAt.setLayoutParams(appBarParams);
    }
}
