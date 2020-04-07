package com.zxy.demo._issue;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.zxy.demo.R;
import com.zxy.demo.utils.String2TabLayoutHelper;
import com.zxy.demo.utils.StringTabLayoutHelper;
import com.zxy.frame.base.BaseFragment;

import java.util.Arrays;

import butterknife.BindView;

public class CustomTabFragment extends BaseFragment {

    @BindView(R.id.tl_tab1)
    TabLayout mTlTab1;
    @BindView(R.id.tl_tab2)
    TabLayout mTlTab2;
    @BindView(R.id.vp_content)
    ViewPager mVpContent;

    StringTabLayoutHelper mTab1LayoutHelper = new StringTabLayoutHelper();
    String2TabLayoutHelper mTab2LayoutHelper = new String2TabLayoutHelper();
    String[] mStrings = new String[]{"TAB1","TAB2","TAB3","TAB4","TAB5","TAB6"};

    @Override
    public int layoutId() {
        return R.layout.fragment_tab_type;
    }

    @Override
    protected void initView() {
        mVpContent.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return new TabFragment(mStrings[position]);
            }

            @Override
            public int getCount() {
                return mStrings.length;
            }
        });
        mTab1LayoutHelper.init(mVpContent,mTlTab1,Arrays.asList(mStrings),0);
        mTab2LayoutHelper.init(mVpContent,mTlTab2,Arrays.asList(mStrings),0);
    }

    public static class TabFragment extends Fragment{

        private String mTitle;

        public TabFragment(){
        }

        public TabFragment(String title){
            mTitle = title;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            TextView tv = new TextView(container.getContext());
            tv.setText(mTitle);
            tv.setTextSize(48);
            return tv;
        }
    }
}
