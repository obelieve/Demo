package com.zxy.mall.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.zxy.frame.base.BaseFragment;
import com.zxy.mall.R;
import com.zxy.mall.view.ShoppingCartView;
import com.zxy.mall.view.header.StoreHeaderView;

import butterknife.BindView;

public class StoreFragment extends BaseFragment {

    @BindView(R.id.view_store_header)
    StoreHeaderView mViewStoreHeader;
    @BindView(R.id.tl_tab)
    TabLayout mTlTab;
    @BindView(R.id.vp_content)
    ViewPager mVpContent;
    @BindView(R.id.view_shopping_cart)
    ShoppingCartView mViewShoppingCart;

    String[] mTitleArr=new String[]{"商品","评价","商家"};

    @Override
    public int layoutId() {
        return R.layout.fragment_store;
    }

    @Override
    protected void initView() {
        mTlTab.setupWithViewPager(mVpContent);
        mVpContent.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Fragment fragment;
                switch (position) {
                    case 0:
                        fragment = new GoodsFragment();
                        break;
                    case 1:
                        fragment = new RatingFragment();
                        break;
                    case 2:
                    default:
                        fragment = new StoreInfoFragment();
                        break;
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return mTitleArr.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitleArr[position];
            }
        });
    }
}
