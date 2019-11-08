package com.zxy.frame.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zxy.frame.utils.StatusBarUtil;


public class BaseActivity extends AppCompatActivity {

    private int mStatusBarColor = Color.TRANSPARENT;
    private boolean mStatusBarLight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configStatusBar(defStatusBarLight(), defStatusBarColor());
    }

    public boolean defStatusBarLight() {
        return true;
    }

    public int defStatusBarColor() {
        return Color.TRANSPARENT;
    }


    public int getStatusBarColor() {
        return mStatusBarColor;
    }

    public boolean isStatusBarLight() {
        return mStatusBarLight;
    }

    public void configStatusBar(boolean light) {
        configStatusBar(light, Color.TRANSPARENT);
    }

    public void configStatusBar(boolean light, @ColorInt int color) {
        mStatusBarLight = light;
        mStatusBarColor = color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setStatusBarColor(this, color);
            StatusBarUtil.setWindowLightStatusBar(this, !light);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setStatusBarColor(this, Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            StatusBarUtil.setStatusBarTranslucentStatus(this);
        }
    }
}
