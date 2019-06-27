package com.zxy.demo.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxy.utility.LogUtil;

public class BaseFragment extends Fragment {

    public int settingStatusBarColor() {
        return Color.TRANSPARENT;
    }

    public boolean settingStatusBarLight() {
        return false;
    }

}
