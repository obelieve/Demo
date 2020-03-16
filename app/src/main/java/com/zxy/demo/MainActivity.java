package com.zxy.demo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.zxy.utility.LogUtil;
import com.zxy.utility.SystemUtil;

public class MainActivity extends AppCompatActivity {

    ViewPager vp_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                return position == 0 ? new MainFragment() : new Fragment2();
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

    }

    //2310 x 1080
    //118  102
    //2192

}
