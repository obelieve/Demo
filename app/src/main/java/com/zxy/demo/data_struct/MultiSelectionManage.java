package com.zxy.demo.data_struct;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MultiSelectionManage {

    private Mode mMode = Mode.MULTI_MUST_ONE;
    private List<Integer> mCurrentIndexes = new ArrayList<>();

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
                case MULTI:
                    if (mCurrentIndexes.contains(index)) {
                        mBooleans.set(index, false);
                        mViews.get(index).setSelected(false);
                        mCurrentIndexes.remove((Integer) index);
                        if (mOnSelectChangeListener != null) {
                            mOnSelectChangeListener.onSelected(index, mViews.get(index), false);
                        }
                    } else {
                        mBooleans.set(index, true);
                        mViews.get(index).setSelected(true);
                        mCurrentIndexes.add(index);
                        if (mOnSelectChangeListener != null) {
                            mOnSelectChangeListener.onSelected(index, mViews.get(index), true);
                        }
                    }
                    break;
                case MULTI_MUST_ONE:
                    if (mCurrentIndexes.size() > 0) {
                        if (mCurrentIndexes.size() == 1) {
                            if (!mCurrentIndexes.contains(index)) {
                                mBooleans.set(index, true);
                                mViews.get(index).setSelected(true);
                                mCurrentIndexes.add(index);
                                if (mOnSelectChangeListener != null) {
                                    mOnSelectChangeListener.onSelected(index, mViews.get(index), true);
                                }
                            }
                        } else {
                            if (!mCurrentIndexes.contains(index)) {
                                mBooleans.set(index, true);
                                mViews.get(index).setSelected(true);
                                mCurrentIndexes.add(index);
                                if (mOnSelectChangeListener != null) {
                                    mOnSelectChangeListener.onSelected(index, mViews.get(index), true);
                                }
                            } else {
                                mBooleans.set(index, false);
                                mViews.get(index).setSelected(false);
                                mCurrentIndexes.remove((Integer)index);
                                if (mOnSelectChangeListener != null) {
                                    mOnSelectChangeListener.onSelected(index, mViews.get(index), false);
                                }
                            }
                        }
                    } else {
                        mBooleans.set(index, true);
                        mViews.get(index).setSelected(true);
                        mCurrentIndexes.add(index);
                        if (mOnSelectChangeListener != null) {
                            mOnSelectChangeListener.onSelected(index, mViews.get(index), true);
                        }
                    }
                    break;
            }
        }
    }

    public void setCurrentItem(View view) {
        setCurrentItem(mViews.indexOf(view));
    }

    public List<Integer> getSelectedIndexes() {
        return mCurrentIndexes;
    }

    public List<View> getSelectedViews() {
        List<View> list = new ArrayList<>();
        for (Integer i : mCurrentIndexes) {
            list.add(mViews.get(i));
        }
        return list;
    }

    private void reset() {
        mCurrentIndexes.clear();
        mViews.clear();
        mBooleans.clear();
    }

    public interface OnSelectChangeListener {

        void onSelected(int index, View view, boolean select);
    }

    public enum Mode {
        MULTI,
        MULTI_MUST_ONE
    }
}
