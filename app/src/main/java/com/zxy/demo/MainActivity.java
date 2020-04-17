package com.zxy.demo;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.tabs.TabLayout;
import com.zxy.demo.fragment.MainFragment;
import com.zxy.frame.base.BaseActivity;
import com.zxy.frame.view.BottomTabView;
import com.zxy.utility.SystemUtil;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tl_tab)
    TabLayout mTlTab;
    @BindView(R.id.vp_content)
    ViewPager mVpContent;

    @BindView(R.id.view_bottom_tab)
    BottomTabView mBottomTabView;
    boolean mLoadXmlTab = false;

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        mVpContent.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                Fragment fragment;
                try {
                    fragment = (Fragment) MainEnum.values()[position].getClazz().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    fragment = new MainFragment();
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return MainEnum.values().length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return MainEnum.values()[position].getName();
            }
        });
        mTlTab.setupWithViewPager(mVpContent);
        initTab();
        mVpContent.setCurrentItem(MainEnum.getCurrentIndex());
    }

    private void initTab() {
        if (mLoadXmlTab) {
            /**
             * XML加载tab
             */
            LayoutInflater.from(mActivity).inflate(R.layout.view_bottom_tab_content, mBottomTabView, true);
            mBottomTabView.setupWithViewPager(mVpContent);
        } else {
            /**
             * 动态加载tab
             */
            BottomTabViewHelper.dynamicAddTabView(mBottomTabView);
            mBottomTabView.setupWithViewPager(mVpContent, new BottomTabView.Callback() {
                @Override
                public void onSelectChange(int index, View view, boolean select) {
                    LottieAnimationView lottieAnimationView = view.findViewById(R.id.lav_menu);
                    BottomTabViewHelper.TabMenuEnum tabMenuEnum = BottomTabViewHelper.getTabMenuEnum(index);
                    if (select) {
                        lottieAnimationView.setImageAssetsFolder(tabMenuEnum.getLottieFileName());
                        lottieAnimationView.setAnimation(tabMenuEnum.getLottieAssetsFolder());
                        lottieAnimationView.playAnimation();
                    } else {
                        lottieAnimationView.clearAnimation();
                        lottieAnimationView.setImageResource(tabMenuEnum.getNorResId());
                    }
                }
            });
        }
    }


    public static class BottomTabViewHelper {
        public enum TabMenuEnum {

            COMMUNITY("首页", "community/community.json", "community/images", R.drawable.ic_nav_home_normal, R.drawable.ic_nav_home_selected, R.id.tab_nav_home),
            MATCH("分类", "match/match.json", "match/images", R.drawable.ic_nav_category_normal, R.drawable.ic_nav_category_selected, R.id.tab_nav_category),
            DATA("发现", "data/data.json", "data/images", R.drawable.ic_nav_discovery_normal, R.drawable.ic_nav_discovery_selected, R.id.tab_nav_discovery),
            FIND("购物车", "find/find.json", "find/images", R.drawable.ic_nav_shopping_cart_normal, R.drawable.ic_nav_shopping_cart_selected, R.id.tab_nav_shopping_cart),
            ME("我的", "me/me.json", "me/images", R.drawable.ic_nav_me_normal, R.drawable.ic_nav_me_selected, R.id.tab_nav_me);

            private String name;
            private String lottieAssetsFolder;
            private String lottieFileName;
            private int norResId;
            private int selectedResId;
            private int id;

            TabMenuEnum(String name, String lottieAssetsFolder, String lottieFileName, int norResId, int selectedResId, int id) {
                this.name = name;
                this.lottieAssetsFolder = lottieAssetsFolder;
                this.lottieFileName = lottieFileName;
                this.norResId = norResId;
                this.selectedResId = selectedResId;
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public String getLottieAssetsFolder() {
                return lottieAssetsFolder;
            }

            public String getLottieFileName() {
                return lottieFileName;
            }

            public int getNorResId() {
                return norResId;
            }

            public int getSelectedResId() {
                return selectedResId;
            }

            public int getId() {
                return id;
            }
        }

        public static TabMenuEnum getTabMenuEnum(int index) {
            TabMenuEnum tabMenuEnum;
            switch (index) {
                case 0:
                    tabMenuEnum = TabMenuEnum.COMMUNITY;
                    break;
                case 1:
                    tabMenuEnum = TabMenuEnum.MATCH;
                    break;
                case 2:
                    tabMenuEnum = TabMenuEnum.DATA;
                    break;
                case 3:
                    tabMenuEnum = TabMenuEnum.FIND;
                    break;
                case 4:
                default:
                    tabMenuEnum = TabMenuEnum.ME;
                    break;
            }
            return tabMenuEnum;
        }

        public static void dynamicAddTabView(BottomTabView bottomTabView) {
            for (int i = 0; i < 5; i++) {
                LinearLayout ll = new LinearLayout(bottomTabView.getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                params.weight = 1;
                ll.setGravity(Gravity.CENTER);
                ll.setLayoutParams(params);
                ll.setPadding(0, SystemUtil.dp2px(6), 0, SystemUtil.dp2px(8));
                ll.setOrientation(LinearLayout.VERTICAL);
                View view = LayoutInflater.from(bottomTabView.getContext()).inflate(R.layout.view_bottom_tab_item, ll, true);
                LottieAnimationView lavMenu = view.findViewById(R.id.lav_menu);
                TextView tvMenu = view.findViewById(R.id.tv_menu);
                TabMenuEnum tabMenuEnum = getTabMenuEnum(i);
                lavMenu.setImageAssetsFolder(tabMenuEnum.getLottieAssetsFolder());
                lavMenu.setAnimation(tabMenuEnum.getLottieFileName());
                lavMenu.setImageResource(tabMenuEnum.getNorResId());
                ll.setId(tabMenuEnum.getId());
                tvMenu.setText(tabMenuEnum.getName());
                bottomTabView.addView(ll);
            }
        }
    }

}



