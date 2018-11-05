package com.zxy.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zxy.demo.viewpager.IndicatorViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    IndicatorViewPager ivp_content;
    PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPagerAdapter = new PagerAdapter();
        ivp_content = findViewById(R.id.ivp_content);
        ivp_content.setAdapter(mPagerAdapter);
        ivp_content.setOffscreenPageLimit(3);
        ivp_content.setPageSpan(30);
        ivp_content.setCurrentItem(1);
        ivp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    ivp_content.setCurrentItem(mPagerAdapter.getCount() - 1 - 1, false);
                } else if (position == mPagerAdapter.getCount() - 1) {
                    ivp_content.setCurrentItem(0 + 1, false);
//                    ivp_content.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    },200);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class PagerAdapter extends android.support.v4.view.PagerAdapter {
        List<View> mList = new ArrayList<>();
        int[] mInts = new int[]{R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4};

        {

            View sview = LayoutInflater.from(getBaseContext()).inflate(R.layout.adapter_item, null);
            ImageView siv = (ImageView) sview.findViewById(R.id.iv);
            siv.setImageResource(mInts[mInts.length - 1]);
            mList.add(sview);
            for (int i = 0; i < 4; i++) {
                View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.adapter_item, null);
                ImageView iv = (ImageView) view.findViewById(R.id.iv);
                iv.setImageResource(mInts[(i % mInts.length)]);
                mList.add(view);
            }
            View eview = LayoutInflater.from(getBaseContext()).inflate(R.layout.adapter_item, null);
            ImageView eiv = (ImageView) eview.findViewById(R.id.iv);
            eiv.setImageResource(mInts[0]);
            mList.add(eview);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mList.get(position));
        }
    }
}
