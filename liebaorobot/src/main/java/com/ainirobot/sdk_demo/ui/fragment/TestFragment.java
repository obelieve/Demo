package com.ainirobot.sdk_demo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ainirobot.sdk_demo.R;
import com.ainirobot.sdk_demo.model.BaseFragment;

/**
 * @author Orion
 * @time 2019/3/6
 */
public class TestFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mContainer = inflater.inflate(R.layout.test_layout, container, false);
        setOnClickListener(mContainer.findViewById(R.id.test_layout_test_button));
        return mContainer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_layout_test_button:
                break;
            default:
                break;
        }
    }


}
