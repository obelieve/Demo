package com.zxy.demo.fragment;

import android.view.View;

import com.zxy.demo.R;
import com.zxy.frame.base.ApiBaseFragment;

import butterknife.BindView;

/**
 * Created by Admin
 * on 2020/5/29
 */
public class ViewRenderFragment extends ApiBaseFragment {

    @BindView(R.id.view_shadow)
    View viewShadow;

    @Override
    public int layoutId() {
        return R.layout.fragment_view_render;
    }

    @Override
    protected void initView() {

    }



}
