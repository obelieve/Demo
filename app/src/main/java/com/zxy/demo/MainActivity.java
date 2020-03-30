package com.zxy.demo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.zxy.demo._issue.ForTAFragment;
import com.zxy.demo._issue.LoginFragment;
import com.zxy.demo._issue.SplashFragment;
import com.zxy.demo._issue.VersionUpdateFragment;
import com.zxy.demo._issue.ZDialogFragment;
import com.zxy.demo.fragment.MainFragment;
import com.zxy.frame.base.BaseActivity;
import com.zxy.frame.view.BottomTabView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    private final String[] mStrings = new String[]{"加载刷新", "Dialog", "@其他人", "引导页", "登录", "版本升级"};
    private final Class[] mClasses = new Class[]{MainFragment.class, ZDialogFragment.class, ForTAFragment.class, SplashFragment.class, LoginFragment.class, VersionUpdateFragment.class};

    @BindView(R.id.tl_tab)
    TabLayout mTlTab;
    @BindView(R.id.vp_content)
    ViewPager mVpContent;

    @BindView(R.id.view_bottom_tab)
    BottomTabView mBottomTabView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        mNeedLightStatusBar = false;
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().setStatusBarColor(Color.BLUE);
//        // 设置状态栏字体黑色
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);//全屏且布局不会占用状态栏空间
//        Window window = getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mVpContent.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {


            @Override
            public Fragment getItem(int position) {
                Fragment fragment;
                try {
                    fragment = (Fragment) mClasses[position].newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    fragment = new MainFragment();
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return mStrings.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mStrings[position];
            }
        });
        mTlTab.setupWithViewPager(mVpContent);
        mBottomTabView.setupWithViewPager(mVpContent);
    }

}



