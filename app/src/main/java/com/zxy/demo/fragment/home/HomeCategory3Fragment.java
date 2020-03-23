package com.zxy.demo.fragment.home;

import android.os.Bundle;
import androidx.annotation.NonNull;;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxy.demo.base.BaseFragment;

public class HomeCategory3Fragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView tv = new TextView(container.getContext());
        tv.setText("类型3");
        return tv;
    }
}
