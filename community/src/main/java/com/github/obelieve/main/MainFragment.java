package com.github.obelieve.main;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.github.obelieve.App;
import com.github.obelieve.bean.VersionUpdateEntity;
import com.github.obelieve.community.R;
import com.github.obelieve.community.ui.CommunityFragment;
import com.github.obelieve.me.ui.MeFragment;
import com.github.obelieve.net.ApiServiceWrapper;
import com.github.obelieve.utils.ActivityUtil;
import com.zxy.frame.base.ApiBaseFragment;
import com.zxy.frame.net.ApiBaseResponse;
import com.zxy.frame.net.ApiBaseSubscribe;
import com.zxy.frame.net.ApiServiceException;
import com.zxy.frame.utils.info.SystemInfoUtil;
import com.zxy.frame.utils.helper.FragmentManagerHelper;
import com.zxy.frame.view.BottomTabView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Admin
 * on 2020/9/3
 */
public class MainFragment extends ApiBaseFragment {

    @BindView(R.id.view_bottom_tab)
    BottomTabView mBottomTabView;

    FragmentManagerHelper mFragmentManagerHelper;

    @Override
    public int layoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        mFragmentManagerHelper = new FragmentManagerHelper(getChildFragmentManager(), R.id.fl_content, new FragmentManagerHelper.FragmentFactory() {
            @Override
            public Fragment genFragment(int index) {
                Fragment fragment;
                switch (index) {
                    case 0:
                        fragment = new CommunityFragment();
                        break;
                    case 1:
                    default:
                        fragment = new MeFragment();
                        break;
                }
                return fragment;
            }
        });
        initTabMenu(getLocalTabMenus());
        initVersionUpdate();
    }

    private void initTabMenu(List<HomeTab.HomeTabMenus> tabMenusList) {
        for (HomeTab.HomeTabMenus tabMenus : tabMenusList) {
            LinearLayout ll = new LinearLayout(mActivity);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            ll.setGravity(Gravity.CENTER);
            ll.setLayoutParams(params);
            ll.setPadding(0, SystemInfoUtil.dp2px(mActivity, 6), 0, SystemInfoUtil.dp2px(mActivity, 8));
            ll.setOrientation(LinearLayout.VERTICAL);
            View view = LayoutInflater.from(mActivity).inflate(R.layout.view_bottom_tab_item, ll, true);
            LottieAnimationView lavMenu = view.findViewById(R.id.lav_menu);
            TextView tvMenu = view.findViewById(R.id.tv_menu);
            TabMenuEnum tabMenuEnum = getTabMenuEnum(tabMenus.getMenu_ident());
            if (!TextUtils.isEmpty(tabMenuEnum.getLottieAssetsFolder())) {
                lavMenu.setImageAssetsFolder(tabMenuEnum.getLottieAssetsFolder());
            }
            lavMenu.setAnimation(tabMenuEnum.getLottieFileName());
            lavMenu.setImageResource(tabMenuEnum.getNorResId());
            ll.setTag(tabMenus.getMenu_ident());
            ll.setId(tabMenuEnum.getId());
            tvMenu.setText(tabMenus.getMenu_name());
            mBottomTabView.addView(ll);
        }
        mBottomTabView.setup(new BottomTabView.Callback() {
            @Override
            public void onSelectChange(int index, View view, boolean select) {
                LottieAnimationView lottieAnimationView = view.findViewById(R.id.lav_menu);
                TabMenuEnum tabMenuEnum = getTabMenuEnum((String) view.getTag());
                if (select) {
                    lottieAnimationView.setImageAssetsFolder(tabMenuEnum.getLottieAssetsFolder());
                    lottieAnimationView.setAnimation(tabMenuEnum.getLottieFileName());
                    lottieAnimationView.playAnimation();
                } else {
                    lottieAnimationView.clearAnimation();
                    lottieAnimationView.setImageResource(tabMenuEnum.getNorResId());
                }
                if (select) {
                    mFragmentManagerHelper.switchFragment(index);
                }
            }
        });
    }

    private void initVersionUpdate() {
        ApiServiceWrapper.wrap(App.getServiceInterface().versionUpdate(), VersionUpdateEntity.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<VersionUpdateEntity>>() {
                    @Override
                    public void onError(ApiServiceException e) {

                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<VersionUpdateEntity> response, boolean isProcessed) {
                        if (response.getEntity().getIs_need() == 1)
                            ActivityUtil.gotoUpdateActivity(mActivity, response.getEntity());
                    }
                });
    }

    private List<HomeTab.HomeTabMenus> getLocalTabMenus() {
        List<HomeTab.HomeTabMenus> localHomeTabMenus = new ArrayList<>();
        HomeTab.HomeTabMenus tabMenus0 = new HomeTab.HomeTabMenus();
        tabMenus0.setMenu_ident("tab_bbs");
        tabMenus0.setMenu_name("社区");
        tabMenus0.setMenu_ico_name("ss_community");
        HomeTab.HomeTabMenus tabMenus1 = new HomeTab.HomeTabMenus();
        tabMenus1.setMenu_ident("tab_user");
        tabMenus1.setMenu_name("我的");
        tabMenus1.setMenu_ico_name("ss_my");
        localHomeTabMenus.addAll(Arrays.asList(tabMenus0, tabMenus1));
        return localHomeTabMenus;
    }

    public enum TabMenuEnum {

        COMMUNITY("community/community.json", "community/images/", R.drawable.ss_community_grey, R.drawable.ss_community, R.id.main_tab_community),
        MATCH("match/match.json", "", R.drawable.index_icon_match, R.drawable.index_icon_match_select, R.id.main_tab_match),
        DATA("data/data.json", "", R.drawable.index_icon_data, R.drawable.index_icon_data_select, R.id.main_tab_data),
        FIND("find/find.json", "find/images/", R.drawable.tj_ecommend_grey, R.drawable.tj_ecommend, R.id.main_tab_recommend),
        ME("me/me.json", "me/images/", R.drawable.index_icon_me, R.drawable.index_icon_me_select, R.id.main_tab_me);

        private String lottieAssetsFolder;
        private String lottieFileName;
        private int norResId;
        private int selectedResId;
        private int id;

        TabMenuEnum(String lottieFileName, String lottieAssetsFolder, int norResId, int selectedResId, int id) {
            this.lottieAssetsFolder = lottieAssetsFolder;
            this.lottieFileName = lottieFileName;
            this.norResId = norResId;
            this.selectedResId = selectedResId;
            this.id = id;
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

    public TabMenuEnum getTabMenuEnum(String menu_ident) {
        TabMenuEnum tabMenuEnum;
        if (TextUtils.isEmpty(menu_ident)) {
            return TabMenuEnum.ME;
        }
        switch (menu_ident) {
            case "tab_match":
                tabMenuEnum = TabMenuEnum.MATCH;
                break;
            case "tab_table":
                tabMenuEnum = TabMenuEnum.DATA;
                break;
            case "tab_bbs":
                tabMenuEnum = TabMenuEnum.COMMUNITY;
                break;
            case "tab_plan":
                tabMenuEnum = TabMenuEnum.FIND;
                break;
            case "tab_user":
            default:
                tabMenuEnum = TabMenuEnum.ME;
                break;
        }
        return tabMenuEnum;
    }
}
