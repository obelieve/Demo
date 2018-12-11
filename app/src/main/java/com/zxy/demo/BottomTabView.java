package com.zxy.demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telecom.Call;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


/**
 * Created by admin on 2018/11/18.
 */

public class BottomTabView extends FrameLayout implements View.OnClickListener {

    SelectionManage mSelectionManage = new SelectionManage();

    private LinearLayout ll_wallet, ll_discover, ll_news, ll_me;
    private Callback mCallback;

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public BottomTabView(@NonNull Context context) {
        this(context, null, 0);
    }

    public BottomTabView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomTabView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        removeAllViews();
        LayoutInflater.from(getContext()).inflate(R.layout.view_bottom_tab, this, true);
        ll_wallet = findViewById(R.id.ll_wallet);
        ll_discover = findViewById(R.id.ll_discover);
        ll_news = findViewById(R.id.ll_news);
        ll_me = findViewById(R.id.ll_me);
        ll_wallet.setOnClickListener(this);
        ll_discover.setOnClickListener(this);
        ll_news.setOnClickListener(this);
        ll_me.setOnClickListener(this);
        mSelectionManage.setMode(SelectionManage.Mode.SINGLE_MUST_ONE);
        mSelectionManage.setItems(ll_wallet, ll_discover, ll_news, ll_me);
        mSelectionManage.setCurrentItem(0);
        mSelectionManage.setOnSelectChangeListener(new SelectionManage.OnSelectChangeListener() {


            @Override
            public void onSelectChange(int index, View view, boolean select) {
                if (mCallback != null && select == true)
                    mCallback.onSelected(index, view);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_wallet:
                mSelectionManage.setCurrentItem(0);
                break;
            case R.id.ll_discover:
                mSelectionManage.setCurrentItem(1);
                break;
            case R.id.ll_news:
                mSelectionManage.setCurrentItem(2);
                break;
            case R.id.ll_me:
                mSelectionManage.setCurrentItem(3);
                break;
        }
    }

    public void setCurrentItem(int index) {
        mSelectionManage.setCurrentItem(index);
    }

    public interface Callback {
        void onSelected(int index, View view);
    }


}
