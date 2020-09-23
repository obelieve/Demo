package com.github.obelieve.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.zxy.frame.utils.SelectionManage;


/**
 * Created by admin on 2018/11/18.
 */

public class BottomTabView extends LinearLayout implements View.OnClickListener {

    SelectionManage mSelectionManage = new SelectionManage();

    private View[] mItemViews;
    private ViewPager mViewPager;
    private Callback mCallback;

    public BottomTabView(@NonNull Context context) {
        this(context, null, 0);
    }

    public BottomTabView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomTabView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
    }

    private void init() {
        mItemViews = new View[getChildCount()];
        for (int i = 0; i < getChildCount(); i++) {
            mItemViews[i] = getChildAt(i);
            mItemViews[i].setOnClickListener(this);
        }
        mSelectionManage.setMode(SelectionManage.Mode.SINGLE_MUST_ONE);
        mSelectionManage.setItems(mItemViews);
        mSelectionManage.setCurrentItem(0);
        mSelectionManage.setOnSelectChangeListener(new SelectionManage.OnSelectChangeListener() {


            @Override
            public void onSelectChange(int index, View view, boolean select) {
                if (mViewPager != null && select == true) {
                    mViewPager.setCurrentItem(index);
                }
                if(mCallback!=null)
                    mCallback.onSelectChange(index,view,select);
            }
        });
    }

    public void setup(Callback callback){
        setup(0,callback);
    }

    public void setup(int curIndex,Callback callback){
        mCallback = callback;
        if(curIndex<0||curIndex>=getChildCount()){
            curIndex = 0;
        }
        mItemViews = new View[getChildCount()];
        for (int i = 0; i < getChildCount(); i++) {
            mItemViews[i] = getChildAt(i);
            mItemViews[i].setOnClickListener(this);
        }
        mSelectionManage.setMode(SelectionManage.Mode.SINGLE_MUST_ONE);
        mSelectionManage.setItems(mItemViews);
        mSelectionManage.setOnSelectChangeListener(new SelectionManage.OnSelectChangeListener() {


            @Override
            public void onSelectChange(int index, View view, boolean select) {
                if(mCallback!=null)
                    mCallback.onSelectChange(index,view,select);
            }
        });
        mSelectionManage.setCurrentItem(curIndex);
    }

    public void setupWithViewPager(ViewPager viewPager,Callback callback) {
        mCallback = callback;
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mSelectionManage.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        init();
    }

    @Override
    public void onClick(View v) {
        for (int index = 0; index < mItemViews.length; index++) {
            if (v.getId() == mItemViews[index].getId()) {
                mSelectionManage.setCurrentItem(index);
                break;
            }
        }
    }

    public interface Callback{
         void onSelectChange(int index, View view, boolean select);
    }
}
