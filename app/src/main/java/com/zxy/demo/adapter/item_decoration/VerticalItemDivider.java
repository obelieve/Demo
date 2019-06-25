package com.zxy.demo.adapter.item_decoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

public class VerticalItemDivider extends RecyclerView.ItemDecoration {

    private boolean mIsFirst = true;
    private boolean mIsDP;
    private int mDividerWidth;
    private int mColor;
    private Paint mPaint;


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
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mIsFirst && mIsDP) {
            mDividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDividerWidth, view.getResources().getDisplayMetrics());
            mIsFirst = false;
        }
        outRect.bottom = mDividerWidth;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int top = view.getBottom();
            int bottom = top + mDividerWidth;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }
}
