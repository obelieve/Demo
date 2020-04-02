package com.zxy.demo;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;


public class TabWrapper {

//    private Context sContext = getContext();


    public void initTab(TabLayout tabLayout, int count, String[] titles, String[] nums, int index) {
        Context context = tabLayout.getContext();
        for (int i = 0; i < count; i++) {
//            View view = LayoutInflater.from(context).inflate(R.layout.tab_custom_view_follow, null);
//            TextView tvTitle = view.findViewById(R.id.tv_title);
//            tvTitle.setTextColor(context.getResources().getColor(R.color.tab_text));
//            tvTitle.setTextAppearance(context, R.style.TabLayoutTextSize_normal_14sp);
//            tvTitle.setText(titles[i]);
//
//            TextView tvNum = view.findViewById(R.id.tv_num);
//            tvNum.setTextColor(context.getResources().getColor(R.color.tab_text));
//            tvNum.setTextAppearance(context, R.style.TabLayoutTextSize_normal_11sp);
//            tvNum.setText(nums[i]);
//            tabLayout.addTab(tabLayout.newTab().setCustomView(view), i == index);
        }
    }

    public void selectedTab(TabLayout.Tab tab, String title, int num) {
//        View view = tab.getCustomView();
//        if (null == view) {
//            tab.setCustomView(R.layout.tab_custom_view_follow);
//            view = tab.getCustomView();
//        }
//        RelativeLayout rlTab = view.findViewById(R.id.rl_tab);
//        rlTab.setSelected(true);
//        TextView tvTitle = view.findViewById(R.id.tv_title);
//        tvTitle.setTextColor(sContext.getResources().getColor(R.color.white));
//        tvTitle.setTextAppearance(sContext, R.style.TabLayoutTextSize_bold_14sp);
//
//        TextView tvNum = view.findViewById(R.id.tv_num);
//        tvNum.setTextColor(sContext.getResources().getColor(R.color.white));
//        tvNum.setTextAppearance(sContext, R.style.TabLayoutTextSize_bold_11sp);
//
//        if (tab.getPosition() == 0) {
//            tvTitle.setText(title);
//            tvNum.setText(String.valueOf(num));
//        } else {
//            tvTitle.setText(title);
//            tvNum.setText(String.valueOf(num));
//        }
    }

    public void unselectedTab(TabLayout.Tab tab, String title, int num) {
//        View view = tab.getCustomView();
//        if (null == view) {
//            tab.setCustomView(R.layout.tab_custom_view_follow);
//        }
//        RelativeLayout rlTab = view.findViewById(R.id.rl_tab);
//        rlTab.setSelected(false);
//        TextView tvTitle = tab.getCustomView().findViewById(R.id.tv_title);
//        tvTitle.setTextColor(sContext.getResources().getColor(R.color.color_333333));
//        tvTitle.setTextAppearance(sContext, R.style.TabLayoutTextSize_normal_14sp);
//
//        TextView tvNum = tab.getCustomView().findViewById(R.id.tv_num);
//        tvNum.setTextColor(sContext.getResources().getColor(R.color.color_333333));
//        tvNum.setTextAppearance(sContext, R.style.TabLayoutTextSize_normal_11sp);
//
//        if (tab.getPosition() == 0) {
//            tvTitle.setText(title);
//            tvNum.setText(num);
//        } else {
//            tvTitle.setText(title);
//            tvNum.setText(num);
//        }
    }


    public void selectRl(RelativeLayout rl, boolean selected) {
        rl.setSelected(selected);
    }

    public void selectTvTitle(TextView tv, boolean selected) {
//        if (selected) {
//            tv.setTextColor(sContext.getResources().getColor(R.color.white));
//            tv.setTextAppearance(sContext, R.style.TabLayoutTextSize_bold_14sp);
//        } else {
//            tv.setTextColor(sContext.getResources().getColor(R.color.color_333333));
//            tv.setTextAppearance(sContext, R.style.TabLayoutTextSize_normal_14sp);
//        }
    }

    public void selectTvNum(TextView tv, boolean selected) {
//        if (selected) {
//            tv.setTextColor(sContext.getResources().getColor(R.color.white));
//            tv.setTextAppearance(sContext, R.style.TabLayoutTextSize_bold_11sp);
//        } else {
//            tv.setTextColor(sContext.getResources().getColor(R.color.color_333333));
//            tv.setTextAppearance(sContext, R.style.TabLayoutTextSize_normal_11sp);
//        }
    }
}
