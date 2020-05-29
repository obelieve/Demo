package com.zxy.demo.fragment;

import android.graphics.Color;
import android.view.View;

import com.zxy.demo.R;
import com.zxy.frame.base.BaseFragment;
import com.zxy.frame.utils.ShadowDrawable;
import com.zxy.frame.utils.ViewUtil;

import butterknife.BindView;

/**
 * Created by Admin
 * on 2020/5/29
 */
public class ViewRenderFragment extends BaseFragment {

    @BindView(R.id.view_shadow)
    View viewShadow;

    @Override
    public int layoutId() {
        return R.layout.fragment_view_render;
    }

    @Override
    protected void initView() {
        ViewUtil.setShadowBackground(viewShadow, ShadowDrawable.gen().radius(10).shadowColor(Color.parseColor("#332995FF")).build(getContext()));
    }
}
