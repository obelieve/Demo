package com.zxy.view_gradient;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.zxy.demo.R;

/**
 * Created by admin on 2019/3/20.
 */

public class GradientActivity extends Activity {


    GradientView gradientView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradient);
        gradientView = findViewById(R.id.gradientView);
        for (int i = 0; i <= 10; i++) {
            final int p = i;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    gradientView.setProgress(p * 0.1f);
                }
            }, 2000 * i);
        }
    }
}
