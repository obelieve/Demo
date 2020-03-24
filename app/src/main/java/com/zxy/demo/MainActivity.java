package com.zxy.demo;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.zxy.utility.LogUtil;
import com.zxy.utility.SystemUtil;

public class MainActivity extends AppCompatActivity {

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
        //  AndroidBug5497Workaround.assistActivity(findViewById(android.R.id.content));
        vp_content = findViewById(R.id.vp_content);
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
        if(show){
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

    //2310 x 1080
    //118  102
    //2192

}
