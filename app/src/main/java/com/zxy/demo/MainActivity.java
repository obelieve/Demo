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
    }

    public class PagerAdapter extends android.support.v4.view.PagerAdapter {
        List<View> mList = new ArrayList<>();

        {
            ImageView view1 = new ImageView(getBaseContext());
            view1.setImageResource(R.drawable.a1);
            ImageView view2 = new ImageView(getBaseContext());
            view2.setImageResource(R.drawable.a2);
            ImageView view3 = new ImageView(getBaseContext());
            view3.setImageResource(R.drawable.a3);
            ImageView view4 = new ImageView(getBaseContext());
            view4.setImageResource(R.drawable.a4);

            mList.add(view1);
            mList.add(view2);
            mList.add(view3);
            mList.add(view4);
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
