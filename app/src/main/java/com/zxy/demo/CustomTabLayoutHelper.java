package com.zxy.demo;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

class CustomTabLayoutHelper extends AbsTabLayoutHelper<String> {

    @Override
    public void selectTab(TabLayout.Tab tab, boolean selected) {
        if (null == tab.getCustomView()) {
            tab.setCustomView(getLayoutId());
        }
        View view = tab.getCustomView();
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvNum = view.findViewById(R.id.tv_num);

        selectTvTitle(tvTitle, selected);
        selectTvNum(tvNum, selected);
    }

    @Override
    public void setData(View view, String s) {
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvNum = view.findViewById(R.id.tv_num);
        tvTitle.setText(s);
        tvNum.setText(s);
    }

    public void selectTvTitle(TextView tv, boolean selected) {
        if (selected) {
            tv.setTextSize(14);
            tv.setTextColor(Color.parseColor("#333333"));
        } else {
            tv.setTextSize(14);
            tv.setTextColor(Color.parseColor("#999999"));
        }
    }

    public void selectTvNum(TextView tv, boolean selected) {
        if (selected) {
            tv.setTextSize(11);
            tv.setTextColor(Color.parseColor("#333333"));
        } else {
            tv.setTextSize(11);
            tv.setTextColor(Color.parseColor("#999999"));
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.tab_custom;
    }
}
