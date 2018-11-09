package com.zxy.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.zxy.demo.viewpager.IndicatorViewPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    IndicatorViewPager ivp_content;
    PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPagerAdapter = new PagerAdapter(new ArrayList<Integer>(Arrays.asList(R.drawable.a1)));//, R.drawable.a2, R.drawable.a3, R.drawable.a4
        ivp_content = findViewById(R.id.ivp_content);
        ivp_content.setAdapter(mPagerAdapter);
        ivp_content.setOffscreenPageLimit(5);
        ivp_content.setPageSpan(30);
        ivp_content.setCurrentItem(0);
    }

    public class PagerAdapter extends IndicatorViewPager.IndicatorViewPagerAdapter<Integer>
    {
//        List<View> mList = new ArrayList<>();
//        int[] mInts = new int[]{R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4};
//
//        {
//
//            for (int i = 0; i < 4; i++) {
//                View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.adapter_item, null);
//                ImageView iv = (ImageView) view.findViewById(R.id.iv);
//                iv.setImageResource(mInts[(i % mInts.length)]);
//                mList.add(view);
//            }
//        }

        public PagerAdapter(List<Integer> list)
        {
            super(list);
        }

        @Override
        public View getNewInstance(Integer o)
        {
            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.adapter_item, null);
            ImageView iv = (ImageView) view.findViewById(R.id.iv);
            iv.setImageResource(o);
            return view;
        }

        @Override
        public int getOffscreenPageLimit()
        {
            return 5;
        }
    }
}
