package com.zxy.view_one;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;
import com.zxy.demo.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by zxy on 2018/10/26 16:11.
 */

public class OneActivity extends Activity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        final PKProgressBar progress_pk = findViewById(R.id.progress_pk);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                int left = new Random().nextInt(1000000);
                int right = new Random().nextInt(1000000);
                progress_pk.setLeftAndRightProgress(left, right);
                new Handler().postDelayed(this, 1000);

            }
        }, 1000);
        initWheelView();
    }

    private void initWheelView()
    {
        WheelView2 wheelView = findViewById(R.id.wheelview);
        wheelView.setCyclic(false);
//        wheelView.setDividerColor(getResources().getColor(android.R.color.transparent));
//        wheelView.setTextColorCenter(Color.parseColor("#fee204"));
//        wheelView.setTextColorOut(Color.parseColor("#8F7213"));
        wheelView.setTextSize(24);
        final ArrayList<String> mOptionsItems = new ArrayList<>();
        mOptionsItems.add("1");
        mOptionsItems.add("2");
        mOptionsItems.add("3");
        mOptionsItems.add("4");
        mOptionsItems.add("5");
        mOptionsItems.add("6");
        mOptionsItems.add("7");
        mOptionsItems.add("8");
        mOptionsItems.add("9");

        wheelView.setAdapter(new ArrayWheelAdapter(mOptionsItems));
        wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Toast.makeText(OneActivity.this, "" + mOptionsItems.get(index), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int dp2px(int dp)
    {
        return (int) (dp * density());
    }

    private float density()
    {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }
}
