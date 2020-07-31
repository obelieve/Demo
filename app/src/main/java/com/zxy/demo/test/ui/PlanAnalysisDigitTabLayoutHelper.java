package com.zxy.demo.test.ui;

import android.graphics.Color;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.zxy.demo.R;
import com.zxy.frame.utils.tab.AbsTabLayoutHelper;

/**
 * Created by Admin
 * on 2020/7/31
 */
public class PlanAnalysisDigitTabLayoutHelper extends AbsTabLayoutHelper<String> {


    @Override
    public void selectTab(TabLayout.Tab tab, boolean selected) {
        if (null == tab.getCustomView()) {
            tab.setCustomView(getLayoutId());
        }
        View view = tab.getCustomView();
        TextView tvTitle = view.findViewById(R.id.tv_title);
        selectTvTitle(tvTitle, selected);
    }


    @Override
    public void setData(View view, String data) {
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(data);
    }


    public void selectTvTitle(TextView tv, boolean selected) {
        if (selected) {
            tv.setTextColor(Color.parseColor("#333333"));
            TextPaint tp = tv.getPaint();
            tp.setFakeBoldText(false);
        } else {
            tv.setTextColor(Color.parseColor("#C1C1C1"));
            TextPaint tp = tv.getPaint();
            tp.setFakeBoldText(false);
        }
        tv.setSelected(selected);
        tv.setTextSize(16);
    }


    @Override
    public int getLayoutId() {
        return R.layout.tablayout_helper_plan_analysis_digit;
    }
}
