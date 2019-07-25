package com.ainirobot.sdk_demo.model;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ainirobot.sdk_demo.utils.TestUtil;

/**
 * @author Orion
 * @time 2018/9/11
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void setOnClickListener(View view) {
        if (view != null) {
            view.setOnClickListener(this);
        }
    }

    protected void setOnClickListener(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                view.setOnClickListener(this);
            }
        }
    }

    public interface OnFragmentChangedListener {
        void onChanged(Fragment fragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        TestUtil.updateApi(null);
    }
}
