package com.zxy.demo.adapter.item_decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class VerticalItemDivider extends RecyclerView.ItemDecoration {

    private boolean mIsFirst = true;
    private boolean mIsDP;
    private int mDividerWidth;
    private int mColor;
    private Paint mPaint;
    private float mDensity;

    private Paint mNoPaint;
    private boolean mHeaderNoDivider = false;
    private boolean mFooterNoDivider = false;
    private List<Integer> mNoDividers = new ArrayList<>();
    private boolean mMarginDP;
    private int mMarginLeft;
    private int mMarginRight;


    public VerticalItemDivider(int color) {
        this(true, 1, color);
    }

    /**
     * @param dividerWidth unit dp
     * @param color
     */
    public VerticalItemDivider(boolean is_dp, int dividerWidth, int color) {
        mIsDP = is_dp;
        mDividerWidth = dividerWidth;
        mColor = color;
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mNoPaint = new Paint();
        mNoPaint.setColor(Color.TRANSPARENT);
    }

    public void setNoDividerItem(int... args) {
        setNoDividerItem(false, false, args);
    }

    public void setNoDividerItem(boolean headerNoDivider, boolean footerNoDivider, int... position) {
        mHeaderNoDivider = headerNoDivider;
        mFooterNoDivider = footerNoDivider;
        if (position != null && position.length > 0) {
            for (int i = 0; i < position.length; i++)
                mNoDividers.add(position[i]);
        }
    }

    public void setMarginLR(boolean isDP, int left, int right) {
        mMarginDP = isDP;
        mMarginLeft = left;
        mMarginRight = right;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mIsFirst && mIsDP) {
            mDensity = view.getResources().getDisplayMetrics().density;
            mDividerWidth = (int) (mDividerWidth * mDensity);
            mIsFirst = false;
        }
        outRect.bottom = mDividerWidth;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft()+(int)(mMarginDP?mDensity*mMarginLeft:mMarginLeft);
        int right = parent.getWidth() - parent.getPaddingRight()-(int)(mMarginDP?mDensity*mMarginRight:mMarginRight);
        for (int i = 0; i < childCount; i++) {
            Paint paint = mPaint;
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            if ((mHeaderNoDivider && position == 0) || (mFooterNoDivider && position == parent.getAdapter().getItemCount() - 1) ||
                    mNoDividers.size() > 0 && mNoDividers.contains(position)) {
                paint = mNoPaint;
            }
            int top = view.getBottom();
            int bottom = top + mDividerWidth;
            c.drawRect(left, top, right, bottom, paint);
        }
    }
}
