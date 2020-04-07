package com.zxy.demo.utils;

import android.graphics.Color;
import android.text.TextPaint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.zxy.demo.R;
import com.zxy.frame.utils.tab.AbsTabLayoutHelper;

public class String2TabLayoutHelper extends AbsTabLayoutHelper<String> {

    @Override
    public void selectTab(TabLayout.Tab tab, boolean selected) {
        if (null == tab.getCustomView()) {
            tab.setCustomView(getLayoutId());
        }
        View view = tab.getCustomView();
        TextView tvTitle = view.findViewById(R.id.tv_title);
        LinearLayout llContent = view.findViewById(R.id.ll_content);
        selectView(llContent, selected);
        selectTvTitle(tvTitle, selected);
    }

    public void selectView(View view, boolean selected) {
        view.setSelected(selected);
    }

    @Override
    public void setData(View view, String data) {
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(data);
    }


    public void selectTvTitle(TextView tv, boolean selected) {
        if (selected) {
            tv.setTextColor(Color.parseColor("#ffffff"));
            TextPaint tp = tv.getPaint();
            tp.setFakeBoldText(true);
        } else {
            tv.setTextColor(Color.parseColor("#999999"));
            TextPaint tp = tv.getPaint();
            tp.setFakeBoldText(false);
        }
        tv.setTextSize(16);
    }


    @Override
    public int getLayoutId() {
        return R.layout.view_tab_custom_text2;
    }
}
