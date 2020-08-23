package com.zxy.frame.adapter.item_decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @Author zxy
 */
public class StaggeredGridItemDivider extends RecyclerView.ItemDecoration {

    private boolean mIsFirst = true;
    private float mDensity;

    private boolean mIsDP;
    private int mDividerWidth;
    private int mColor;
    private Paint mPaint;
    private Paint mNoPaint;

    private boolean mDividerLeftToTop;
    private boolean mLeftRightNoDivider;
    private boolean mTopBottomNoDivider;
    private List<Integer> mNoDividers = new ArrayList<>();

    public StaggeredGridItemDivider() {
        this(false, 1, Color.rgb(216, 216, 216));
    }

    public StaggeredGridItemDivider(int color) {
        this(true, 1, color);
    }

    /**
     * @param dividerWidth unit dp
     * @param color
     */
    public StaggeredGridItemDivider(boolean is_dp, int dividerWidth, int color) {
        mIsDP = is_dp;
        mDividerWidth = dividerWidth;
        mColor = color;
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mNoPaint = new Paint();
        mNoPaint.setColor(Color.TRANSPARENT);
    }

    public StaggeredGridItemDivider dividerToLeftTop(boolean dividerToLeftTop) {
        mDividerLeftToTop = dividerToLeftTop;
        return this;
    }

    public StaggeredGridItemDivider noDividerItem(int... args) {
        return noDividerItem(false, false, args);
    }

    public StaggeredGridItemDivider noDividerItem(boolean leftRightNoDivider, boolean topBottomNoDivider, int... position) {
        mLeftRightNoDivider = leftRightNoDivider;
        mTopBottomNoDivider = topBottomNoDivider;
        if (position != null && position.length > 0) {
            for (int i = 0; i < position.length; i++)
                mNoDividers.add(position[i]);
        }
        return this;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (!(parent.getLayoutManager() instanceof StaggeredGridLayoutManager)) {
            throw new RuntimeException("RecyclerView must be StaggeredGridLayoutManager!");
        }
        if (parent.getAdapter() == null) {
            throw new RuntimeException("RecyclerView Adapter is null !");
        }
        if (mIsFirst) {
            mDensity = view.getResources().getDisplayMetrics().density;
            mDividerWidth = mIsDP ? (int) (mDividerWidth * mDensity) : mDividerWidth;
            mIsFirst = false;
        }
        StaggeredGridLayoutManager lm = (StaggeredGridLayoutManager) parent.getLayoutManager();
        int spanCount = lm.getSpanCount();
        int position = parent.getChildAdapterPosition(view);
        StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams)view.getLayoutParams();
        int spanIndex = lp.getSpanIndex();
        int itemCount = parent.getAdapter().getItemCount();
        if (mDividerLeftToTop) {
            if (!(mLeftRightNoDivider && spanIndex == 0)) {
                outRect.left = mDividerWidth;
            }
            if (!(mTopBottomNoDivider && position < spanCount)) {
                outRect.top = mDividerWidth;
            }
            if (mNoDividers.size() > 0 && mNoDividers.contains(position)) {
                outRect.left = 0;
                outRect.top = 0;
            }
        } else {
            if (!(mLeftRightNoDivider && (spanIndex + 1) % spanCount == 0)) {
                outRect.right = mDividerWidth;
            }
            int mod = itemCount % spanCount;
            int lastRowPosition = mod == 0 ?
                    itemCount- spanCount : itemCount - mod;
            if (!(mTopBottomNoDivider && parent.getAdapter() != null &&
                    position >= lastRowPosition)) {
                outRect.bottom = mDividerWidth;
            }
            if (mNoDividers.size() > 0 && mNoDividers.contains(position)) {
                outRect.right = 0;
                outRect.bottom = 0;
            }
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        StaggeredGridLayoutManager lm = (StaggeredGridLayoutManager) parent.getLayoutManager();
        int spanCount = lm.getSpanCount();
        for (int i = 0; i < childCount; i++) {
            Paint paint = mPaint;
            View view = parent.getChildAt(i);
            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams)view.getLayoutParams();
            int spanIndex = lp.getSpanIndex();
            int position = parent.getChildAdapterPosition(view);
            int left = view.getLeft();
            int top = view.getTop();
            int right = view.getRight();
            int bottom = view.getBottom();
            if (mDividerLeftToTop) {
                if (!(mNoDividers.size() > 0 && mNoDividers.contains(position))) {
                    if (!(mLeftRightNoDivider && spanIndex == 0)) {
                        c.drawRect(left - mDividerWidth, top, left, bottom, paint);
                    }
                    if (!(mTopBottomNoDivider && position < spanCount)) {
                        c.drawRect(left - mDividerWidth, top - mDividerWidth, right, top, paint);
                    }
                }
            } else {
                if (!(mNoDividers.size() > 0 && mNoDividers.contains(position))) {
                    if (!(mLeftRightNoDivider && (spanIndex + 1) % spanCount == 0)) {
                        c.drawRect(right, top, right + mDividerWidth, bottom, paint);
                    }
                    int[] lastPos = lm.findLastCompletelyVisibleItemPositions(null);
                    boolean containPosition = false;
                    for(int pos=0;pos<lastPos.length;pos++){
                        if(lastPos[pos]==position){
                            containPosition = true;
                            break;
                        }
                    }
                    if (!(mTopBottomNoDivider && parent.getAdapter() != null &&
                            containPosition)) {
                        c.drawRect(left, bottom, right + mDividerWidth, bottom + mDividerWidth, paint);
                    }
                }
            }

        }
    }
}
