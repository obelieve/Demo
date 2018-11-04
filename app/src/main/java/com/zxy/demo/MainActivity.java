package com.zxy.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zxy.demo.viewpager.IndicatorViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    IndicatorViewPager ivp_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivp_content = findViewById(R.id.ivp_content);
        ivp_content.setAdapter(new PagerAdapter());
        ivp_content.setCurrentItem(0);
    }

    public class PagerAdapter extends android.support.v4.view.PagerAdapter {
        List<View> mList = new ArrayList<>();
        int[] mInts = new int[]{R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4};

        {

            for (int i = 0; i < 4; i++) {
                ImageView view = new ImageView(getBaseContext());
                view.setImageResource(mInts[(i % mInts.length)]);
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mList.add(view);
            }
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
