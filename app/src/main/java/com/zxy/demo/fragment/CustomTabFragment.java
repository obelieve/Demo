package com.zxy.demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.zxy.demo.activity.TabActivity;
import com.zxy.demo.databinding.FragmentTabCustomBinding;
import com.zxy.demo.utils.String2TabLayoutHelper;
import com.zxy.demo.utils.StringTabLayoutHelper;
import com.zxy.frame.base.ApiBaseFragment;

import java.util.Arrays;


public class CustomTabFragment extends ApiBaseFragment<FragmentTabCustomBinding> {

    //TabLayout app:tabGravity="fill|center" 只影响app:tabMode="fixed"
    StringTabLayoutHelper mTab1LayoutHelper = new StringTabLayoutHelper();
    String2TabLayoutHelper mTab2LayoutHelper = new String2TabLayoutHelper();
    String[] mStrings = new String[]{"TAB1", "TAB2", "TAB3", "TAB4", "TAB5", "TAB6"};

    @Override
    protected void initView() {
        mViewBinding.vpContent.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
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
        mTab1LayoutHelper.init(mViewBinding.vpContent, mViewBinding.tlTab1, Arrays.asList(mStrings), 0);
        mTab2LayoutHelper.init(mViewBinding.vpContent, mViewBinding.tlTab2, Arrays.asList(mStrings), 0);
        mViewBinding.btnTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TabActivity.class));
            }
        });
    }

    public static class TabFragment extends Fragment {

        private String mTitle;

        public TabFragment(String title) {
            this();
            mTitle = title;
        }

        public TabFragment() {
            super();
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
