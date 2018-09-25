package com.zxy.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by zxy on 2018/9/25 10:21.
 */

public class ScrollFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_scroll, container, false);
        TextView tv = (TextView)view.findViewById(R.id.tv);
        tv.setText(AssetsUtil.getAssetsContent(getContext(),"nest_text"));
        return view;
    }
}
