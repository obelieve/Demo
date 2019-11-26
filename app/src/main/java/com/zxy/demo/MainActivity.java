package com.zxy.demo;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.zxy.demo._issue.ForTAFragment;
import com.zxy.demo.fragment.MainFragment;
import com.zxy.frame.view.SlidingViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private final String[] mStrings = new String[]{
            "推荐", "精选", "Kindle","@其他人"};

    @BindView(R.id.tl_tab)
    TabLayout mTlTab;
    @BindView(R.id.vp_content)
    ViewPager mVpContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mVpContent.setOffscreenPageLimit(2);
        mVpContent.setAdapter(new MainAdapter(getSupportFragmentManager()){

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
                            fragment = new MainFragment();
                            mFragmentSparseArray.put(0, fragment);
                        } else {
                            fragment = mFragmentSparseArray.get(0);
                        }
                        break;
                    case 1:
                        if (mFragmentSparseArray.get(1) == null) {
                            fragment = new MainFragment();
                            mFragmentSparseArray.put(1, fragment);
                        } else {
                            fragment = mFragmentSparseArray.get(1);
                        }
                        break;
                    case 2:
                        if (mFragmentSparseArray.get(2) == null) {
                            fragment = new MainFragment();
                            mFragmentSparseArray.put(2, fragment);
                        } else {
                            fragment = mFragmentSparseArray.get(2);
                        }
                        break;
                    case 3:
                        fragment = new ForTAFragment();
                        break;
                    default:
                        if (mFragmentSparseArray.get(position) == null) {
                            fragment = new MainFragment();
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
                return mStrings.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mStrings[position];
            }
        });
        mTlTab.setupWithViewPager(mVpContent);
    }

    public static abstract class MainAdapter extends FragmentStatePagerAdapter {

        protected MainAdapter(FragmentManager fm) {
            super(fm);
        }

        public abstract RecyclerView getContentView(int position);

        protected ArrayList<Fragment> getFragments() {
            ArrayList<Fragment> list = new ArrayList<>();
            Field field = null;
            ArrayList temp = null;
            try {
                field = FragmentStatePagerAdapter.class.getDeclaredField("mFragments");
                field.setAccessible(true);
                temp = (ArrayList) field.get(this);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (temp != null) {
                for (int i = 0; i < temp.size(); i++) {
                    Object obj = temp.get(i);
                    if (obj instanceof Fragment) {
                        list.add((Fragment) obj);
                    }
                }
            }
            return list;
        }
    }
}



