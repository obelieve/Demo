package com.zxy.demo.data_struct;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SelectionManage {

    private Mode mMode = Mode.SELECT_SINGLE_MUST_ONE;
    private int mCurrentIndex = -1;

    private List<View> mViews = new ArrayList<>();
    private List<Boolean> mBooleans = new ArrayList<>();
    private OnSelectChangeListener mOnSelectChangeListener;

    public void setMode(Mode mode) {
        reset();
        mMode = mode;
    }

    public void setItems(View... views) {
        reset();
        for (View view : views) {
            mViews.add(view);
            mBooleans.add(false);
        }
    }

    public void setOnSelectChangeListener(OnSelectChangeListener onSelectChangeListener) {
        mOnSelectChangeListener = onSelectChangeListener;
    }

    public void setCurrentItem(int index) {
        if (index >= 0 && index < mViews.size()) {
            switch (mMode) {
                case SELECT_SINGLE:
                    if (mCurrentIndex != index) {
                        setLastSelect(mCurrentIndex, false);
                        setCurrentSelect(index, true);
                    } else {
                        if (mBooleans.get(mCurrentIndex) == true) {
                            setCurrentSelect(mCurrentIndex, false);
                        } else {
                            setCurrentSelect(mCurrentIndex, true);
                        }
                    }
                    break;
                case SELECT_SINGLE_MUST_ONE:
                    if (mCurrentIndex != index) {
                        setLastSelect(mCurrentIndex, false);
                        setCurrentSelect(index, true);
                    }
                    break;
            }
        }
    }

    public void setCurrentItem(View view) {
        setCurrentItem(mViews.indexOf(view));
    }

    private void setCurrentSelect(int index, boolean select) {
        mBooleans.set(index, select);
        mViews.get(index).setSelected(select);
        switch (mMode) {
            case SELECT_SINGLE:
            case SELECT_SINGLE_MUST_ONE:
                mCurrentIndex = index;
                break;
        }
        if (mOnSelectChangeListener != null) {
            mOnSelectChangeListener.onSelected(index, mViews.get(index), select);
        }
    }

    private void setLastSelect(int index, boolean select) {
        if (index != -1) {
            mBooleans.set(index, select);
            mViews.get(index).setSelected(select);
        }
    }

    private void reset() {
        mCurrentIndex = -1;
        mViews.clear();
        mBooleans.clear();
    }

    public interface OnSelectChangeListener {

        void onSelected(int index, View view, boolean select);
    }

    public enum Mode {
        SELECT_SINGLE,
        SELECT_SINGLE_MUST_ONE
    }
}
