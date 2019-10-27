package com.zxy.demo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxy.demo.R;
import com.zxy.demo.base.BaseActivity;
import com.zxy.demo.base.BaseFragment;
import com.zxy.demo.fragment.home.HomeCategory1Fragment;
import com.zxy.demo.fragment.home.HomeCategory2Fragment;
import com.zxy.demo.fragment.home.HomeCategory3Fragment;
import com.zxy.demo.view.HomeTopView;
import com.zxy.demo.view.HomeViewPager;
import com.zxy.demo.view.HomeViewPager.HomeAdapter;

import java.util.ArrayList;

/**
 * Created by zxy on 2018/10/30 10:35.
 */

public class HomeFragment extends BaseFragment {

    private final String[] mStrings = new String[]{
            "推荐", "掌柜推荐", "限时抢购", "特价专区", "人气热卖",
            "本周更新", "水果", "肉禽蛋", "海鲜水产", "粮油调味",
            "熟食卤味", "面点冰品", "牛奶面包", "酒水冲饮", "休闲零食",
            "日用清洁", "护理美妆", "进口商品", "鲜花礼品", "地方特产",
            "母婴", "宠物"};

    private HomeTopView mClTop;
    private TabLayout mTlTab;
    private HomeViewPager mVpContent;
    private HomeAdapter mHomeAdapter;

    private boolean mStatusBarLight = true;
    private int mStatusBarColor = Color.TRANSPARENT;

    private HomeTopView.StatusBarChangedNotifier mStatusBarChangedNotifier = new HomeTopView.StatusBarChangedNotifier() {
        @Override
        public void notifyStatusBarChanged(boolean light, int color) {
            mStatusBarLight = light;
            mStatusBarColor = color;
            if (getActivity() instanceof BaseActivity && !isDisableStatusBarUpdate()) {
                BaseActivity activity = (BaseActivity) getActivity();
                activity.configStatusBar(light, color);
            }
        }
    };

    @Override
    public boolean settingStatusBarLight() {
        return mStatusBarLight;
    }

    @Override
    public int settingStatusBarColor() {
        return mStatusBarColor;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 放在onCreate(...)进行初始化PagerAdapter。为了在切换时重新onCreateView(...)。
         * 会发生View.onSaveInstanceState和View.onRestoreInstanceState发生。
         * ViewPager的onSaveInstanceState和onRestoreInstanceState会对FragmentManager的Fragment实例进行恢复，所以会导致
         * setAdapter(...)是新的Adapter和新的Fragment。但是显示是旧的Fragment(getItem(...)会直接返回之前恢复的Fragment)。
         */
        mHomeAdapter = new HomeAdapter(getChildFragmentManager()) {
            SparseArray<Fragment> mFragmentSparseArray = new SparseArray<>();

            @Override
            public RecyclerView getContentView(int position) {
                ArrayList<Fragment> list = getFragments();//避免onRestoreInstanceState时，Fragment是之前的。
                View view = list != null ? list.get(position).getView() : null;
                if (view instanceof ViewGroup) {
                    ViewGroup vp = ((ViewGroup) view);
                    for (int i = 0; i < vp.getChildCount(); i++) {
                        if (vp.getChildAt(i) instanceof RecyclerView) {
                            return (RecyclerView) vp.getChildAt(i);
                        }
                    }
                }
                return null;
            }

            @Override
            public Fragment getItem(int position) {
                Fragment fragment;
                switch (position) {
                    case 0:
                        if (mFragmentSparseArray.get(0) == null) {
                            fragment = new HomeCategory1Fragment();
                            mFragmentSparseArray.put(0, fragment);
                        } else {
                            fragment = mFragmentSparseArray.get(0);
                        }
                        break;
                    case 1:
                        if (mFragmentSparseArray.get(1) == null) {
                            fragment = new HomeCategory2Fragment();
                            mFragmentSparseArray.put(1, fragment);
                        } else {
                            fragment = mFragmentSparseArray.get(1);
                        }
                        break;
                    case 2:
                        if (mFragmentSparseArray.get(2) == null) {
                            fragment = new HomeCategory3Fragment();
                            mFragmentSparseArray.put(2, fragment);
                        } else {
                            fragment = mFragmentSparseArray.get(2);
                        }
                    default:
                        if (mFragmentSparseArray.get(position) == null) {
                            fragment = new HomeCategory3Fragment();
                            mFragmentSparseArray.put(position, fragment);
                        } else {
                            fragment = mFragmentSparseArray.get(position);
                        }
                        break;

                }
                return fragment;
            }

            @Override
            public int getCount() {
                return 5;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mStrings[position];
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mClTop = view.findViewById(R.id.cl_top);
        mTlTab = view.findViewById(R.id.tl_tab);
        mVpContent = view.findViewById(R.id.vp_content);
        mClTop.setHomeContentView(mVpContent, mStatusBarChangedNotifier);
        mVpContent.setAdapter(mHomeAdapter);
        mTlTab.setupWithViewPager(mVpContent);
        return view;
    }
}
