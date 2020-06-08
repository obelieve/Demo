package com.zxy.demo.fragment;

import com.zxy.demo.R;
import com.zxy.frame.base.BaseFragment;

/**
 * Tips: fontFamily、typeface 都用于设置字体，都设置是优先使用fontFamily
 * typeface设置：Typeface.createFromAsset(context.getAssets(), fontAssetName)
 * fontFamily设置：res/font 目录下
 * Created by Admin
 * on 2020/6/8
 */
public class FontFragment extends BaseFragment {
    @Override
    public int layoutId() {
        return R.layout.fragment_font;
    }

    @Override
    protected void initView() {

    }
}
