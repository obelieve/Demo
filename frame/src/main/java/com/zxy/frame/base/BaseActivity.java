package com.zxy.frame.base;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zxy.frame.utils.StatusBarUtil;


public class BaseActivity extends AppCompatActivity {

    private boolean mStatusBarLight = true;
    private int mStatusBarColor = Color.TRANSPARENT;
    protected boolean mNeedLightStatusBar = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreenOrientation();
        if (mNeedLightStatusBar){
            configStatusBar(defStatusBarLight(), defStatusBarColor());
        }
    }

    protected void setScreenOrientation() {
        if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            try{
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public boolean defStatusBarLight() {
        return mStatusBarLight;
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
